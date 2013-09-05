package com.liferay.cli.shell.osgi;

import com.liferay.cli.shell.CliCommand;
import com.liferay.cli.shell.CliOption;
import com.liferay.cli.shell.CommandMarker;
import com.liferay.cli.shell.ShellSettingsProvider;
import com.liferay.cli.shell.converters.StaticFieldConverter;
import com.liferay.cli.support.logging.HandlerUtils;

import java.util.logging.Logger;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;

@Component(immediate = true)
@Service
public class DefaultShellSettingsProvider implements ShellSettingsProvider, CommandMarker
{
    private static final Logger LOGGER = HandlerUtils.getLogger(DefaultShellSettingsProvider.class);

//    private ComponentContext context;
    private ShellMode shellMode = ShellMode.BASIC;

    @Reference private StaticFieldConverter staticFieldConverter;

    protected void activate(final ComponentContext context) {
//        this.context = context;
        staticFieldConverter.add(ShellMode.class);
    }

    protected void deactivate(final ComponentContext context) {
//        this.context = null;
        staticFieldConverter.remove(ShellMode.class);
    }

    @Override
    public Object get( String settingsKey )
    {
        if( ADVANCED_MODE.equals( settingsKey ) )
        {
            return shellMode.compareTo( ShellMode.ADVANCED ) == 0;
        }

        return null;
    }

    @CliCommand( value = "shell setting", help = "Change Ray's shell settings" )
    public void changeSetting(
        @CliOption( key = "mode", mandatory = false, help = "Ray's interactive mode" ) final ShellMode mode )
        throws Exception
    {
        if( mode != null )
        {
            this.shellMode = mode;
        }

        LOGGER.info("Current shell settings:\n");
        LOGGER.info("*mode=" + this.shellMode.getKey() );
    }

}
