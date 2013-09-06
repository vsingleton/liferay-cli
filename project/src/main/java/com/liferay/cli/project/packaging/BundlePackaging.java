package com.liferay.cli.project.packaging;

import static com.liferay.cli.project.Path.SRC_MAIN_JAVA;
import static com.liferay.cli.project.Path.SRC_MAIN_RESOURCES;

import com.liferay.cli.model.JavaPackage;
import com.liferay.cli.project.GAV;
import com.liferay.cli.project.LogicalPath;
import com.liferay.cli.project.Path;
import com.liferay.cli.project.ProjectOperations;

import java.util.Arrays;
import java.util.Collection;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

/**
 * The {@link PackagingProvider} that creates an OSGi bundle.
 *
 * @author Andrew Swan
 * @since 1.2.0
 */
@Component
@Service
public class BundlePackaging implements CorePackagingProvider {

    public String createArtifacts(final JavaPackage topLevelPackage,
            final String nullableProjectName, final String javaVersion,
            final GAV parentPom, final String module,
            final ProjectOperations projectOperations) {
        return createArtifacts( topLevelPackage, nullableProjectName, nullableProjectName, javaVersion, parentPom,
            module, projectOperations );
    }

    public String getId() {
        return "bundle";
    }

    public Collection<Path> getPaths() {
        return Arrays.asList(SRC_MAIN_JAVA, SRC_MAIN_RESOURCES);
    }

    public boolean isDefault() {
        return false;
    }

    @Override
    public String createArtifacts(
        JavaPackage topLevelPackage, String projectName, String artifactId, String javaVersion, GAV parentPom,
        String moduleName, ProjectOperations projectOperations )
    {
        // Already created by the creator addon
        return projectOperations.getPathResolver().getIdentifier( LogicalPath.getInstance(Path.ROOT, ""), "pom.xml");
    }
}
