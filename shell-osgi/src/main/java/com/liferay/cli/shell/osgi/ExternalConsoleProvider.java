package com.liferay.cli.shell.osgi;


public interface ExternalConsoleProvider
{

    ExternalConsole getConsole();

    String getId();

    boolean isValid();

}
