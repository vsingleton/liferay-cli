package com.liferay.cli.shell.osgi.converters;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import com.liferay.cli.shell.converters.CharacterConverter;

/**
 * OSGi component launcher for {@link CharacterConverter}.
 * 
 * @author Ben Alex
 * @since 1.1
 */
@Component
@Service
public class CharacterConverterComponent extends CharacterConverter {
}