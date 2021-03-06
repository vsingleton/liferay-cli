
package com.liferay.cli.project.server;

import com.liferay.cli.model.JavaPackage;
import com.liferay.cli.project.Dependency;
import com.liferay.cli.project.GAV;
import com.liferay.cli.project.MavenOperationsImpl;
import com.liferay.cli.project.Path;
import com.liferay.cli.project.maven.Module;
import com.liferay.cli.project.maven.Pom;
import com.liferay.cli.project.packaging.JarPackaging;
import com.liferay.cli.project.packaging.PackagingProvider;
import com.liferay.cli.project.packaging.ServerPackaging;
import com.liferay.cli.shell.osgi.ExternalConsoleProvider;
import com.liferay.cli.shell.osgi.ExternalConsoleProviderRegistry;
import com.liferay.cli.support.logging.HandlerUtils;
import com.liferay.cli.support.util.DomUtils;
import com.liferay.cli.support.util.FileUtils;
import com.liferay.cli.support.util.XmlUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
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
    private ExternalConsoleProviderRegistry externalShellProviderRegistry;

    @Override
    public void serverSetup( final ServerType serverType, final ServerVersion serverVersion, final ServerEdition serverEdition )
    {
        final Pom rootPom = pomManagementService.getRootPom();
        final JavaPackage rootTopLevelPackage = new JavaPackage( rootPom.getGroupId() );
        final GAV parentGAV = new GAV( rootPom.getGroupId(), rootPom.getArtifactId(), rootPom.getVersion() );
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

        fileManager.createOrUpdateTextFileIfRequired(
            pomManagementService.getRootPom().getPath(), XmlUtils.nodeToString( rootPomDocument ), updatedProperties, false );

        // add <modules>
        addLogfixModule( rootPom, rootTopLevelPackage, parentGAV );

        pomManagementService.setFocusedModule( rootPom );

        addServerModule( rootPom, rootTopLevelPackage, parentGAV, serverVersion );

        pomManagementService.setFocusedModule( rootPom );

        fileManager.commit();
    }

    private void addServerModule( Pom rootPom, JavaPackage rootTopLevelPackage, GAV parentGAV, ServerVersion serverVersion )
    {
        final String serverModuleName = "server";
        final String serverArtifactId = rootPom.getArtifactId() + "-server";

        final PackagingProvider serverPackagingProvider = packagingProviderRegistry.getPackagingProvider( ServerPackaging.NAME );

        createModule( rootTopLevelPackage, parentGAV, serverModuleName, serverPackagingProvider, 6, serverArtifactId );

        final Pom serverPom = pomManagementService.getPomFromModuleName( serverModuleName );

        final Document serverPomDocument = XmlUtils.readXml( fileManager.getInputStream( serverPom.getPath() ) );
        final Element serverPomRoot = serverPomDocument.getDocumentElement();

        // add <properties> element
        final Element serverPropertiesElement = DomUtils.createChildIfNotExists("properties", serverPomRoot, serverPomDocument);

        final Element liferayVersionElement = DomUtils.createChildIfNotExists("liferay.version", serverPropertiesElement, serverPomDocument);
        liferayVersionElement.setTextContent( getLatestAvailableServerVersion( serverVersion.getDisplayName() ) );

        final String updatedServerProperties =
            getDescriptionOfChange( "updated", Collections.singleton( serverPom.getDisplayName() ), "property", "properties" );

        fileManager.createOrUpdateTextFileIfRequired(
            serverPom.getPath(), XmlUtils.nodeToString( serverPomDocument ), updatedServerProperties, false );

        final String logModuleName = "../logfix";

        addModuleDeclaration( logModuleName, serverPomDocument, serverPomRoot );
        final String addModuleMessage = getDescriptionOfChange(ADDED, Collections.singleton( logModuleName ), "module", "modules");

        fileManager.createOrUpdateTextFileIfRequired(
            serverPom.getPath(), XmlUtils.nodeToString( serverPomDocument ), addModuleMessage, false );

        GAV plugin = new GAV( "org.apache.tomcat.maven", "tomcat7-maven-plugin", "2.1" );
        Dependency dep = new Dependency( parentGAV.getGroupId(), parentGAV.getArtifactId() + "-logfix", parentGAV.getVersion() );

        addPluginDependency( serverModuleName, plugin, dep, serverPomDocument, serverPomRoot );

        final String addDepMsg = getDescriptionOfChange(ADDED, Collections.singleton( plugin.getArtifactId() ), "dependency", "dependencies");

        fileManager.createOrUpdateTextFileIfRequired( serverPom.getPath(), XmlUtils.nodeToString( serverPomDocument ), addDepMsg, false);
    }

    private void addLogfixModule(final Pom rootPom, final JavaPackage rootTopLevelPackage, final GAV parentGAV )
    {
     // first module is for fixing slf4j log
        final PackagingProvider jarPackagingProvider = packagingProviderRegistry.getPackagingProvider( JarPackaging.NAME );
        final String logModuleName = "logfix";
        final String logfixArtifactId = rootPom.getArtifactId() + "-logfix";

        createModule( rootTopLevelPackage, parentGAV, logModuleName, jarPackagingProvider, 6, logfixArtifactId );

        // add log fix file
        addLogServiceReference();
    }

    private void addLogServiceReference()
    {
        final String serviceFile =
            pathResolver.getFocusedIdentifier( Path.SRC_MAIN_SERVICES, "org.apache.commons.logging.LogFactory" );

        final InputStream serviceFileInputStream =
            FileUtils.getInputStream( getClass(), "org.apache.commons.logging.LogFactory" );

        OutputStream outputStream = null;

        try
        {
            outputStream = fileManager.createFile( serviceFile ).getOutputStream();
            IOUtils.copy( serviceFileInputStream, outputStream );
        }
        catch( final IOException e )
        {
            LOGGER.warning( "Unable to install log4j logging configuration" );
        }
        finally
        {
            IOUtils.closeQuietly( serviceFileInputStream );
            IOUtils.closeQuietly( outputStream );
        }
    }

    @Override
    public void serverRun()
    {
        final Pom serverPom = pomManagementService.getPomFromModuleName( "server" );
        final String workingDir = new File( serverPom.getPath() ).getParent(); // TODO RAY handle unexpected results better

        final String serverArtifactId = serverPom.getArtifactId();
        String logfixArtifactId = null;

        for(Module module : serverPom.getModules())
        {
            if( module.getName().endsWith( "logfix" ) )
            {
                Pom logfixPom = pomManagementService.getPomFromModuleName( "logfix" );
                logfixArtifactId = logfixPom.getArtifactId();

                break;
            }
        }

        final String mavenCommandArgs = getServerRunMavenCommand( logfixArtifactId, serverArtifactId );

        ExternalConsoleProvider externalShellProvider = externalShellProviderRegistry.getExternalShellProvider();

        externalShellProvider.getConsole().execute(workingDir, "mvn", mavenCommandArgs );
    }

    private String getServerRunMavenCommand( String logfixArtifactId, String serverArtifactId )
    {
        StringBuffer sb = new StringBuffer();

        sb.append( "-Dmaven.test.skip verify tomcat7:run-war -pl :");
        sb.append( logfixArtifactId );
        sb.append( ",:" );
        sb.append( serverArtifactId );
        sb.append( " -am" );

        return sb.toString();
    }

    @Override
    public boolean isServerRunAvailable()
    {
        return externalShellProviderRegistry.getExternalShellProvider() != null && checkValidServerSetup();
    }

    private boolean checkValidServerSetup()
    {
        //TODO RAY finish impl
        return true;
    }

    private String getServerEditionValue( ServerEdition serverEdition )
    {
        //TODO RAY finish impl
        return "CE";
    }

    //TODO RAY finish impl
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
