
package com.liferay.cli.support;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Provides levels for Ray's shell mode
 *
 * @author Gregory Amerson
 */
public class KeyType implements Comparable<KeyType>
{
    private final String key;

    public KeyType( final String key )
    {
        Validate.notBlank( key, "Key required" );
        this.key = key;
    }

    public final int compareTo( final KeyType o )
    {
        if( o == null )
        {
            return -1;
        }
        return key.compareTo( o.key );
    }

    @Override
    public final boolean equals( final Object obj )
    {
        return obj instanceof KeyType && compareTo( (KeyType) obj ) == 0;
    }

    public String getDisplayName()
    {
        return getKey().toLowerCase();
    }

    public String getKey()
    {
        return key;
    }

    @Override
    public final int hashCode()
    {
        return key.hashCode();
    }

    @Override
    public String toString()
    {
        final ToStringBuilder builder = new ToStringBuilder( this );
        builder.append( "key", key );
        return builder.toString();
    }
}
