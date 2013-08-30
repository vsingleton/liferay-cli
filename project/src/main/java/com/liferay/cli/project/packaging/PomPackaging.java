package com.liferay.cli.project.packaging;

import java.util.Collection;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import com.liferay.cli.model.JavaPackage;

import com.liferay.cli.project.Path;
import com.liferay.cli.project.ProjectOperations;

/**
 * The Maven "pom" {@link PackagingProvider}
 * 
 * @author Andrew Swan
 * @since 1.2.0
 */
@Component
@Service
public class PomPackaging extends AbstractCorePackagingProvider {

    /**
     * Constructor
     */
    public PomPackaging() {
        super("pom", "parent-pom-template.xml");
    }

    @Override
    protected void createOtherArtifacts(final JavaPackage topLevelPackage,
            final String module, final ProjectOperations projectOperations) {
        // No artifacts are applicable for POM modules
    }

    public Collection<Path> getPaths() {
        return null;
    }
}
