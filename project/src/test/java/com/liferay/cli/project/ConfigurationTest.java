package com.liferay.cli.project;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.w3c.dom.Element;

import com.liferay.cli.project.Configuration;

/**
 * Unit test of the {@link Configuration} class
 * 
 * @author Andrew Swan
 * @since 1.2.0
 */
public class ConfigurationTest {

    @Test
    public void testInstanceDoesNotEqualNull() {
        assertFalse(new Configuration(mock(Element.class)).equals(null));
    }
}
