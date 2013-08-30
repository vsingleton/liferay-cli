package com.liferay.cli.shell.osgi.converters;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import com.liferay.cli.shell.converters.BigDecimalConverter;

/**
 * OSGi component launcher for {@link BigDecimalConverter}.
 * 
 * @author Ben Alex
 * @since 1.1
 */
@Component
@Service
public class BigDecimalConverterComponent extends BigDecimalConverter {
}