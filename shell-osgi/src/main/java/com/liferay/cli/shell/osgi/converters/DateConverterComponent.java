package com.liferay.cli.shell.osgi.converters;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import com.liferay.cli.shell.converters.DateConverter;

/**
 * OSGi component launcher for {@link DateConverter}.
 * 
 * @author Ben Alex
 * @since 1.1
 */
@Component
@Service
public class DateConverterComponent extends DateConverter {
}