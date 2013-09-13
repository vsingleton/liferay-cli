package com.liferay.cli.shell.osgi;


/**
 * @author Gregory Amerson
 */
public interface ExternalConsoleProvider
{

    ExternalConsole getConsole();

    String getId();

    boolean isValid();

}
