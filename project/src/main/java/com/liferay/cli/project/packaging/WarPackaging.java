package com.liferay.cli.project.packaging;

import static com.liferay.cli.project.Path.SPRING_CONFIG_ROOT;
import static com.liferay.cli.project.Path.SRC_MAIN_JAVA;
import static com.liferay.cli.project.Path.SRC_MAIN_WEBAPP;
import static com.liferay.cli.project.Path.SRC_TEST_JAVA;
import static com.liferay.cli.project.Path.SRC_TEST_RESOURCES;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import com.liferay.cli.model.JavaPackage;

import com.liferay.cli.project.Path;
import com.liferay.cli.project.ProjectOperations;

/**
 * The core {@link PackagingProvider} for web modules.
 * 
 * @author Andrew Swan
 * @since 1.2.0
 */
@Component
@Service
public class WarPackaging extends AbstractCorePackagingProvider {

    public WarPackaging() {
        super("war", "war-pom-template.xml");
    }

    @Override
    protected void createOtherArtifacts(final JavaPackage topLevelPackage,
            final String module, final ProjectOperations projectOperations) {
        super.createOtherArtifacts(topLevelPackage, module, projectOperations);
        if (StringUtils.isBlank(module)) {
            // This is a single-module web project
            final String fullyQualifiedModuleName = getFullyQualifiedModuleName(
                    module, projectOperations);
            applicationContextOperations.createMiddleTierApplicationContext(
                    topLevelPackage, fullyQualifiedModuleName);
        }
    }

    public Collection<Path> getPaths() {
        return Arrays.asList(SRC_MAIN_JAVA, SRC_TEST_JAVA, SRC_TEST_RESOURCES,
                SPRING_CONFIG_ROOT, SRC_MAIN_WEBAPP);
    }
}
