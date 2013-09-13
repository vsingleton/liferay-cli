package com.liferay.cli.shell.osgi;


/**
 * @author Gregory Amerson
 */
public interface ExternalConsole
{

    void execute( String workingDir, String cmd, String argLine );

}
