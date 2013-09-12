package com.liferay.cli.project.packaging;

import static com.liferay.cli.project.Path.SRC_MAIN_JAVA;
import static com.liferay.cli.project.Path.SRC_MAIN_WEBAPP;
import static com.liferay.cli.project.Path.SRC_TEST_JAVA;
import static com.liferay.cli.project.Path.SRC_TEST_RESOURCES;

import com.liferay.cli.project.Path;

import java.util.Arrays;
import java.util.Collection;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

/**
 * The core {@link PackagingProvider} for web modules.
 *
 * @author Andrew Swan
 * @since 1.2.0
 */
@Component
@Service
public class WarPackaging extends AbstractCorePackagingProvider {

    public static final String NAME = "war";

    public WarPackaging() {
        super(NAME, NAME, "war-pom-template.xml");
    }

    public Collection<Path> getPaths() {
        return Arrays.asList(SRC_MAIN_JAVA, SRC_TEST_JAVA, SRC_TEST_RESOURCES, SRC_MAIN_WEBAPP);
    }
}
