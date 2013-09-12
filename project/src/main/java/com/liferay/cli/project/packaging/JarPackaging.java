package com.liferay.cli.project.packaging;

import static com.liferay.cli.project.Path.SRC_MAIN_JAVA;
import static com.liferay.cli.project.Path.SRC_MAIN_RESOURCES;
import static com.liferay.cli.project.Path.SRC_TEST_JAVA;
import static com.liferay.cli.project.Path.SRC_TEST_RESOURCES;

import com.liferay.cli.model.JavaPackage;
import com.liferay.cli.project.GAV;
import com.liferay.cli.project.Path;
import com.liferay.cli.project.ProjectOperations;

import java.util.Arrays;
import java.util.Collection;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

/**
 * The {@link PackagingProvider} that creates a JAR file.
 *
 * @author Andrew Swan
 * @since 1.2.0
 */
@Component
@Service
public class JarPackaging extends AbstractCorePackagingProvider {

    public static final String NAME = "jar";

    /**
     * Constructor invoked by the OSGi container
     */
    public JarPackaging() {
        super(NAME, NAME, "jar-pom-template.xml");
    }

    @Override
    protected String createPom(final JavaPackage topLevelPackage,
            final String nullableProjectName, final String javaVersion,
            final GAV parentPom, final String moduleName,
            final ProjectOperations projectOperations) {

        final String pomPath = super.createPom(topLevelPackage,
                nullableProjectName, javaVersion, parentPom, moduleName,
                projectOperations);
        return pomPath;
    }

    public Collection<Path> getPaths() {
        return Arrays.asList(SRC_MAIN_JAVA, SRC_MAIN_RESOURCES, SRC_TEST_JAVA, SRC_TEST_RESOURCES);
    }

    @Override
    public boolean isDefault() {
        return true;
    }
}
