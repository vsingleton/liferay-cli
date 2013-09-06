
package com.liferay.cli.project.server;

import com.liferay.cli.process.manager.ProcessManager;
import com.liferay.cli.project.AbstractProjectOperations;
import com.liferay.cli.project.Path;
import com.liferay.cli.project.Property;
import com.liferay.cli.project.maven.Pom;
import com.liferay.cli.project.packaging.PackagingProviderRegistry;
import com.liferay.cli.support.logging.HandlerUtils;
import com.liferay.cli.support.util.DomUtils;
import com.liferay.cli.support.util.XmlUtils;

import java.util.Collections;
import java.util.Set;
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
public class ServerOperationsImpl extends AbstractProjectOperations implements ServerOperations
{
    private static final Logger LOGGER = HandlerUtils.getLogger( ServerOperationsImpl.class );

    @Reference
    private PackagingProviderRegistry packagingProviderRegistry;
    @Reference
    private ProcessManager processManager;

    @Override
    public void serverSetup( final ServerType serverType, final ServerVersion serverVersion, final ServerEdition serverEdition )
    {
        final Pom rootPom = pomManagementService.getRootPom();
        final Pom focusedModule = pomManagementService.getFocusedModule();
        final Set<Property> pomProperties = rootPom.getPomProperties();
        final String rootPath = rootPom.getPath();

        final Document rootPomDocument = XmlUtils.readXml( fileManager.getInputStream( rootPath ) );
        final Element parentPomRoot = rootPomDocument.getDocumentElement();
        final Element propertiesElement = DomUtils.createChildIfNotExists("properties", parentPomRoot, rootPomDocument);

        final Element serverTypeElement = DomUtils.createChildIfNotExists("server.type", propertiesElement, rootPomDocument);
        serverTypeElement.setTextContent( serverType.getDisplayName() );

        final Element serverVersionElement = DomUtils.createChildIfNotExists("server.version", propertiesElement, rootPomDocument);
        serverVersionElement.setTextContent( getLatestAvailableServerVersion( serverVersion.getDisplayName() ) );

        final Element serverEditionElement = DomUtils.createChildIfNotExists("server.edition", propertiesElement, rootPomDocument);
        serverEditionElement.setTextContent( getServerEditionValue( serverEdition ) );

        final String updatedProperties = getDescriptionOfChange( "updated", Collections.singleton( rootPom.getDisplayName() ), "property", "properties" );

        fileManager.createOrUpdateTextFileIfRequired( getFocusedModule().getPath(), XmlUtils.nodeToString( rootPomDocument ), updatedProperties, false );
    }

    //TODO finish impl
    private String getServerEditionValue( ServerEdition serverEdition )
    {
        return "CE";
    }

    //TODO finish impl
    private String getLatestAvailableServerVersion( String serverVersion )
    {
        return serverVersion;
    }

    public String getProjectRoot()
    {
        return pathResolver.getRoot( Path.ROOT.getModulePathId( pomManagementService.getFocusedModuleName() ) );
    }

    public boolean isServerSetupAvailable()
    {
//        return isProjectAvailable( getRootName() );
        return getRootName() != null;
    }
}
