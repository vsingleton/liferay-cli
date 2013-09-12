package com.liferay.cli.shell.osgi;

import org.apache.commons.lang3.SystemUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;


@Component
@Service
public class OSXExternalConsoleProvider extends BaseExternalConsoleProvider
{

    public static final String ID = "osx";

    public OSXExternalConsoleProvider()
    {
        super( ID );
    }

    @Override
    public ExternalConsole getConsole()
    {
        return new XtermConsole();
    }

    @Override
    public boolean isValid()
    {
        return SystemUtils.IS_OS_MAC_OSX;
    }

}
