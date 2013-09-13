package com.liferay.cli.shell.osgi;

import org.apache.commons.lang3.SystemUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;


/**
 * @author Gregory Amerson
 */
@Component
@Service
public class WindowsExternalShellProvider extends BaseExternalConsoleProvider
{

    public static final String ID = "windows";

    public WindowsExternalShellProvider()
    {
        super( ID );
    }

    @Override
    public ExternalConsole getConsole()
    {
        return new CmdConsole();
    }

    @Override
    public boolean isValid()
    {
        return SystemUtils.IS_OS_WINDOWS;
    }

}
