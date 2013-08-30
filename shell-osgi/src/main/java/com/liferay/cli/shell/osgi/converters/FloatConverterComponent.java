package com.liferay.cli.shell.osgi.converters;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import com.liferay.cli.shell.converters.FloatConverter;

/**
 * OSGi component launcher for {@link FloatConverter}.
 * 
 * @author Ben Alex
 * @since 1.1
 */
@Component
@Service
public class FloatConverterComponent extends FloatConverter {
}