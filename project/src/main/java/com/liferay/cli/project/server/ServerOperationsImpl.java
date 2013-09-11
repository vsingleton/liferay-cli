
package com.liferay.cli.project.server;

import com.liferay.cli.model.JavaPackage;
import com.liferay.cli.process.manager.ProcessManager;
import com.liferay.cli.project.GAV;
import com.liferay.cli.project.MavenOperationsImpl;
import com.liferay.cli.project.Path;
import com.liferay.cli.project.maven.Pom;
import com.liferay.cli.project.packaging.PackagingProvider;
import com.liferay.cli.project.packaging.ServerPackaging;
import com.liferay.cli.support.logging.HandlerUtils;
import com.liferay.cli.support.util.DomUtils;
import com.liferay.cli.support.util.XmlUtils;

import java.util.Collections;
import java.util.logging.Logger;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Implementation of {@link ServerOperations}.
 *
 * @author Gregory Amerson
 */
@Component( immediate = true )
@Service
public class ServerOperationsImpl extends MavenOperationsImpl implements ServerOperations
{
    private static final Logger LOGGER = HandlerUtils.getLogger( ServerOperationsImpl.class );

    @Reference
    private ProcessManager processManager;

    @Override
    public void serverSetup( final ServerType serverType, final ServerVersion serverVersion, final ServerEdition serverEdition )
    {
        final Pom rootPom = pomManagementService.getRootPom();
        final String rootPath = rootPom.getPath();

        final Document rootPomDocument = XmlUtils.readXml( fileManager.getInputStream( rootPath ) );
        final Element parentPomRoot = rootPomDocument.getDocumentElement();

        // add <properties> element
        final Element propertiesElement = DomUtils.createChildIfNotExists("properties", parentPomRoot, rootPomDocument);

        final Element serverTypeElement = DomUtils.createChildIfNotExists("server.type", propertiesElement, rootPomDocument);
        serverTypeElement.setTextContent( serverType.getDisplayName() );

        final Element serverVersionElement =
            DomUtils.createChildIfNotExists( "server.version", propertiesElement, rootPomDocument );
        serverVersionElement.setTextContent( getLatestAvailableServerVersion( serverVersion.getDisplayName() ) );

        final Element serverEditionElement =
            DomUtils.createChildIfNotExists( "server.edition", propertiesElement, rootPomDocument );
        serverEditionElement.setTextContent( getServerEditionValue( serverEdition ) );

        final String updatedProperties =
            getDescriptionOfChange(
                "updated", Collections.singleton( rootPom.getDisplayName() ), "property", "properties" );

        // add <modules> element

        GAV parentGAV = new GAV( rootPom.getGroupId(), rootPom.getArtifactId(), rootPom.getVersion() );
        JavaPackage serverTopLevelPackage = new JavaPackage( rootPom.getGroupId() );
        String moduleName = "server";
        String serverArtifactId = rootPom.getArtifactId() + "-server";

        PackagingProvider serverPackagingProvider = packagingProviderRegistry.getPackagingProvider( ServerPackaging.NAME );

        createModule( serverTopLevelPackage, parentGAV, moduleName, serverPackagingProvider, 6, serverArtifactId );

        Pom serverPom = pomManagementService.getPomFromModuleName( moduleName );

        final Document serverPomDocument = XmlUtils.readXml( fileManager.getInputStream( serverPom.getPath() ) );
        final Element serverPomRoot = serverPomDocument.getDocumentElement();

        // add <properties> element
        final Element serverPropertiesElement = DomUtils.createChildIfNotExists("properties", serverPomRoot, serverPomDocument);

        final Element liferayVersionElement = DomUtils.createChildIfNotExists("liferay.version", serverPropertiesElement, serverPomDocument);
        liferayVersionElement.setTextContent( getLatestAvailableServerVersion( serverVersion.getDisplayName() ) );

        final String updatedServerProperties =
            getDescriptionOfChange( "updated", Collections.singleton( serverPom.getDisplayName() ), "property", "properties" );

        fileManager.createOrUpdateTextFileIfRequired(
            pomManagementService.getRootPom().getPath(), XmlUtils.nodeToString( rootPomDocument ), updatedProperties, false );

        fileManager.createOrUpdateTextFileIfRequired(
            serverPom.getPath(), XmlUtils.nodeToString( serverPomDocument ), updatedServerProperties, false );
    }

    //TODO finish impl
    private String getServerEditionValue( ServerEdition serverEdition )
    {
        return "CE";
    }

    //TODO finish impl
    private String getLatestAvailableServerVersion( String serverVersion )
    {
        if( "6.1".equals( serverVersion ) )
        {
            return "6.1.2";
        }

        return serverVersion;
    }

    public String getProjectRoot()
    {
        return pathResolver.getRoot( Path.ROOT.getModulePathId( pomManagementService.getFocusedModuleName() ) );
    }

    public boolean isServerSetupAvailable()
    {
        return getRootName() != null;
    }
}
