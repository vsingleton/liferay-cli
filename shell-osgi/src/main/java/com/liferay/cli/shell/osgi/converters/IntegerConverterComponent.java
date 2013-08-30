package com.liferay.cli.shell.osgi.converters;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import com.liferay.cli.shell.converters.IntegerConverter;

/**
 * OSGi component launcher for {@link IntegerConverter}.
 * 
 * @author Ben Alex
 * @since 1.1
 */
@Component
@Service
public class IntegerConverterComponent extends IntegerConverter {
}