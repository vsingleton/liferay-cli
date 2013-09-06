package com.liferay.cli.project;

import com.liferay.cli.model.JavaPackage;
import com.liferay.cli.shell.CliAvailabilityIndicator;
import com.liferay.cli.shell.CliCommand;
import com.liferay.cli.shell.CliOption;
import com.liferay.cli.shell.CommandMarker;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

/**
 * Shell commands for {@link RayOperations} commands.
 *
 * @author Gregory Amerson
 */
@Component
@Service
public class RayCommands implements CommandMarker {

    private static final String PROJECT_COMMAND = "project";

    @Reference private RayOperations rayOperations;

    @CliCommand( value = PROJECT_COMMAND, help = "Creates a new Liferay project" )
    public void createProject(
        @CliOption(
            key = { "", "projectName" },
            help = "The name of the project (last segment of package name used as default)" ) final String projectName,
        @CliOption(
            key = { "topLevelPackage" },
            mandatory = false,
            optionContext = "update",
            help = "The uppermost package name (this becomes the <groupId> in Maven and also the '~' value when using Ray's shell)" ) final JavaPackage topLevelPackage )
    {
        rayOperations.createProject( projectName, topLevelPackage );
    }

    @CliAvailabilityIndicator( PROJECT_COMMAND )
    public boolean isCreateProjectAvailable()
    {
        return rayOperations.isCreateProjectAvailable();
    }

}
