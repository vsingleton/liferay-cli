package com.liferay.cli.project;

import com.liferay.cli.model.JavaPackage;

/**
 * Provides Liferay project operations.
 *
 * @author Gregory Amerson
 */
public interface RayOperations extends ProjectOperations {

    /**
     * Creates a Maven-based project
     *
     * @param topLevelPackage the top-level Java package (required)
     * @param projectName the name of the project (can be blank to generate it
     *            from the top-level package)
     */
    void createProject(JavaPackage topLevelPackage, String projectName);


    /**
     * Indicates whether a new Maven project can be created
     *
     * @return see above
     */
    boolean isCreateProjectAvailable();
}