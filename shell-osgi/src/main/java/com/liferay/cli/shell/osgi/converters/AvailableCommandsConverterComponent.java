package com.liferay.cli.shell.osgi.converters;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import com.liferay.cli.shell.converters.AvailableCommandsConverter;
import com.liferay.cli.shell.converters.StaticFieldConverterImpl;

/**
 * OSGi component launcher for {@link StaticFieldConverterImpl}.
 * 
 * @author Ben Alex
 * @since 1.1
 */
@Component
@Service
public class AvailableCommandsConverterComponent extends
        AvailableCommandsConverter {
}