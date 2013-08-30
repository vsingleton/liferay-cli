package com.liferay.cli.metadata;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.liferay.cli.metadata.DefaultMetadataService;

public class DefaultMetadataServiceTest {

    private static final String TO_STRING_FOR_NEW_INSTANCE = "com.liferay.cli.metadata.DefaultMetadataService:"
            + "[validGets=0,"
            + "recursiveGets=0,"
            + "cachePuts=0,"
            + "cacheHits=0,"
            + "cacheMisses=0,"
            + "cacheEvictions=0,"
            + "cacheCurrentSize=0," + "cacheMaximumSize=100000]";

    @Test
    public void testToStringOfNewInstance() {
        assertEquals(TO_STRING_FOR_NEW_INSTANCE,
                new DefaultMetadataService().toString());
    }
}
