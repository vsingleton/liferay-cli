package com.liferay.cli.shell.osgi;


public interface ExternalConsole
{

    void execute( String workingDir, String cmd, String argLine );

}
