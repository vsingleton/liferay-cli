
package com.liferay.cli.shell.osgi;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.ReferenceStrategy;
import org.apache.felix.scr.annotations.Service;

/**
 * The {@link ExternalConsoleProviderRegistry} implementation.
 *
 * @author Gregory Amerson
 */
@Component
@Service
@Reference( name = "externalShellProvider",
            strategy = ReferenceStrategy.EVENT,
            policy = ReferencePolicy.DYNAMIC,
            referenceInterface = ExternalConsoleProvider.class,
            cardinality = ReferenceCardinality.MANDATORY_MULTIPLE )
public class ExternalConsoleProviderRegistryImpl implements ExternalConsoleProviderRegistry
{

    private final Object mutex = new Object();

    // Using a map avoids each ExternalShellProvider having to implement equals()
    // properly (when removing)
    private final Map<String, ExternalConsoleProvider> externalShellProviders =
        new HashMap<String, ExternalConsoleProvider>();

    protected void bindExternalShellProvider( final ExternalConsoleProvider externalShellProvider )
    {
        synchronized( mutex )
        {
            final ExternalConsoleProvider previousExternalShellProvider =
                externalShellProviders.put( externalShellProvider.getId(), externalShellProvider );
            Validate.isTrue(
                previousExternalShellProvider == null, "More than one ExternalShellProvider with ID = '%s'",
                externalShellProvider.getId() );
        }
    }

    public ExternalConsoleProvider getExternalShellProvider()
    {
        ExternalConsoleProvider retval = null;

        for( final ExternalConsoleProvider externalShellProvider : externalShellProviders.values() )
        {
            if( externalShellProvider.isValid() )
            {
                retval = externalShellProvider;
                break;
            }
        }

        Validate.validState( retval != null, "Should have found a valid ExternalShellProvider" );

        return retval;
    }

    public ExternalConsoleProvider getExternalShellProvider( final String id )
    {
        for( final ExternalConsoleProvider externalShellProvider : externalShellProviders.values() )
        {
            if( externalShellProvider.getId().equalsIgnoreCase( id ) )
            {
                return externalShellProvider;
            }
        }
        return null;
    }

    protected void unbindExternalShellProvider( final ExternalConsoleProvider externalShellProvider )
    {
        synchronized( mutex )
        {
            externalShellProviders.remove( externalShellProvider.getId() );
        }
    }
}
