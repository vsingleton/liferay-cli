package com.liferay.cli.shell.osgi;

import java.io.File;
import java.io.IOException;


/**
 * @author Gregory Amerson
 */
public class XtermConsole implements ExternalConsole
{

    private String xtermCmd;

    public XtermConsole( String xtermCmd )
    {
        this.xtermCmd = xtermCmd;
    }

    @Override
    public void execute( String workingDir, String cmd, String argLine )
    {
        Runtime runtime = Runtime.getRuntime();

        try
        {
            //TODO RAY don't hardcode xterm location
            runtime.exec( this.xtermCmd + " -ls -T liferay_console -e " + cmd + " " + argLine, null, new File( workingDir ) );
        }
        catch( IOException e )
        {
            // TODO RAY log this
            e.printStackTrace();
        }
    }

}
