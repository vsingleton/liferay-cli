package com.liferay.cli.shell.converters;

import java.math.BigInteger;
import java.util.List;

import com.liferay.cli.shell.Completion;
import com.liferay.cli.shell.Converter;
import com.liferay.cli.shell.MethodTarget;

/**
 * {@link Converter} for {@link BigInteger}.
 * 
 * @author Stefan Schmidt
 * @since 1.0
 */
public class BigIntegerConverter implements Converter<BigInteger> {

    public BigInteger convertFromText(final String value,
            final Class<?> requiredType, final String optionContext) {
        return new BigInteger(value);
    }

    public boolean getAllPossibleValues(final List<Completion> completions,
            final Class<?> requiredType, final String existingData,
            final String optionContext, final MethodTarget target) {
        return false;
    }

    public boolean supports(final Class<?> requiredType,
            final String optionContext) {
        return BigInteger.class.isAssignableFrom(requiredType);
    }
}