
package com.liferay.cli.shell.osgi;

import com.liferay.cli.support.DisplayName;
import com.liferay.cli.support.KeyType;


/**
 * Provides levels for Ray's shell mode
 *
 * @author Gregory Amerson
 */
public class ShellMode extends KeyType
{

    @DisplayName( "basic" )
    public static final ShellMode BASIC = new ShellMode( "BASIC" );

    @DisplayName( "advanced" )
    public static final ShellMode ADVANCED = new ShellMode( "ADVANCED" );

    public ShellMode( final String key )
    {
        super( key );
    }

}
