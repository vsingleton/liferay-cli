package com.liferay.cli.shell.osgi;

import org.apache.commons.lang3.SystemUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;


/**
 * @author Gregory Amerson
 */
@Component
@Service
public class UnixExternalConsoleProvider extends BaseExternalConsoleProvider
{

    public static final String ID = "unix";

    public UnixExternalConsoleProvider()
    {
        super( ID );
    }

    @Override
    public ExternalConsole getConsole()
    {
        //TODO RAY don't hardcode xterm location
        return new XtermConsole( "/usr/bin/xterm" );
    }

    @Override
    public boolean isValid()
    {
        return SystemUtils.IS_OS_UNIX;
    }

}
