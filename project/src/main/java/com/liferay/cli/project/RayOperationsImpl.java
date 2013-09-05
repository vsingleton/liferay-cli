package com.liferay.cli.project;

import com.liferay.cli.model.JavaPackage;
import com.liferay.cli.process.manager.ProcessManager;
import com.liferay.cli.project.packaging.JarPackaging;
import com.liferay.cli.project.packaging.PackagingProvider;
import com.liferay.cli.project.packaging.PackagingProviderRegistry;

import org.apache.commons.lang3.Validate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

/**
 * Implementation of {@link RayOperations}.
 *
 * @author Gregory Amerson
 */
@Component(immediate = true)
@Service
public class RayOperationsImpl extends AbstractProjectOperations implements RayOperations
{
//    private static final Logger LOGGER = HandlerUtils .getLogger(RayOperationsImpl.class);

    @Reference private PackagingProviderRegistry packagingProviderRegistry;
    @Reference private ProcessManager processManager;

    public void createProject( final JavaPackage topLevelPackage, final String projectName )
    {
        PackagingProvider packagingProvider = packagingProviderRegistry.getPackagingProvider( JarPackaging.NAME );
        Validate.isTrue( isCreateProjectAvailable(), "Project creation is unavailable at this time" );
        packagingProvider.createArtifacts( topLevelPackage, projectName, getJavaVersion( 6 ), null, "", this );
    }

    /**
     * Returns the project's target Java version in POM format
     *
     * @param majorJavaVersion the major version provided by the user; can be
     *            <code>null</code> to auto-detect it
     * @return a non-blank string
     */
    private String getJavaVersion(final Integer majorJavaVersion) {
        if (majorJavaVersion != null && majorJavaVersion >= 6
                && majorJavaVersion <= 7) {
            return String.valueOf(majorJavaVersion);
        }
        // To be running Roo they must be on Java 6 or above
        return "1.6";
    }

    public String getProjectRoot() {
        return pathResolver.getRoot(Path.ROOT
                .getModulePathId(pomManagementService.getFocusedModuleName()));
    }

    public boolean isCreateModuleAvailable() {
        return true;
    }

    public boolean isCreateProjectAvailable() {
        return !isProjectAvailable(getFocusedModuleName());
    }

}
