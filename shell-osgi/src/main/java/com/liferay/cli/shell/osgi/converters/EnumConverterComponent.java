package com.liferay.cli.shell.osgi.converters;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import com.liferay.cli.shell.converters.EnumConverter;

/**
 * OSGi component launcher for {@link EnumConverter}.
 * 
 * @author Ben Alex
 * @since 1.1
 */
@Component
@Service
public class EnumConverterComponent extends EnumConverter {
}