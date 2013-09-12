package com.liferay.cli.shell.osgi;


public abstract class BaseExternalConsoleProvider implements ExternalConsoleProvider
{
    private String id;

    public BaseExternalConsoleProvider( String id )
    {
        this.id = id;
    }

    @Override
    public String getId()
    {
        return this.id;
    }

}
