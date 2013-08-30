package com.liferay.cli.shell.osgi.converters;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import com.liferay.cli.shell.converters.BigIntegerConverter;

/**
 * OSGi component launcher for {@link BigIntegerConverter}.
 * 
 * @author Ben Alex
 * @since 1.1
 */
@Component
@Service
public class BigIntegerConverterComponent extends BigIntegerConverter {
}