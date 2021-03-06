package com.liferay.cli.shell.osgi;

import java.io.File;
import java.io.IOException;


/**
 * @author Gregory Amerson
 */
public class CmdConsole implements ExternalConsole
{

    @Override
    public void execute( String workingDir, String cmd, String argLine )
    {
        Runtime runtime = Runtime.getRuntime();

        try
        {
            runtime.exec("cmd.exe /c start cmd.exe /k " + cmd + " " + argLine, null, new File( workingDir ) );
        }
        catch( IOException e )
        {
            // TODO RAY log this
            e.printStackTrace();
        }
    }

}
