
package com.liferay.cli.project.server;

import com.liferay.cli.process.manager.ProcessManager;
import com.liferay.cli.project.AbstractProjectOperations;
import com.liferay.cli.project.Path;
import com.liferay.cli.project.packaging.PackagingProviderRegistry;
import com.liferay.cli.support.logging.HandlerUtils;

import java.util.logging.Logger;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

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
    public void serverSetup( ServerType serverType, ServerVersion serverVersion, ServerEdition serverEdition )
    {
        LOGGER.info( "serverType: " + serverType + " serverVersion: " + serverVersion + "serverEdition: " + serverEdition );
    }

    public String getProjectRoot()
    {
        return pathResolver.getRoot( Path.ROOT.getModulePathId( pomManagementService.getFocusedModuleName() ) );
    }

}
