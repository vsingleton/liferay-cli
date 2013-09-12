package com.liferay.cli.shell.osgi;


/**
 * A registry for {@link ExternalConsoleProvider}s.
 *
 * @author Gregory Amerson
 */
public interface ExternalConsoleProviderRegistry
{

    /**
     * Returns the {@link ExternalConsoleProvider} that can be used on current OS
     *
     * @return a non-<code>null</code> instance
     */
    ExternalConsoleProvider getExternalShellProvider();

}