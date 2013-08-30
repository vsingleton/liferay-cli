package com.liferay.cli.shell.osgi.converters;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import com.liferay.cli.shell.converters.LocaleConverter;

/**
 * OSGi component launcher for {@link LocaleConverter}.
 * 
 * @author Stefan Schmidt
 * @since 1.1
 */
@Component
@Service
public class LocaleConverterComponent extends LocaleConverter {
}
