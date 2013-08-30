package com.liferay.cli.model;

/**
 * A type safe type that provides a key for tagging and validating
 * {@link com.liferay.cli.model.CustomDataAccessor} objects.
 * 
 * @author James Tyrrell
 * @since 1.1.3
 */
public interface CustomDataKey<T> extends Criteria<T> {

    String name();
}
