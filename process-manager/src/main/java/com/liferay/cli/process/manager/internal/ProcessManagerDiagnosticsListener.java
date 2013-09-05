package com.liferay.cli.process.manager.internal;

import com.liferay.cli.process.manager.ProcessManager;
import com.liferay.cli.process.manager.event.ProcessManagerStatus;
import com.liferay.cli.process.manager.event.ProcessManagerStatusListener;
import com.liferay.cli.process.manager.event.ProcessManagerStatusProvider;
import com.liferay.cli.shell.CliCommand;
import com.liferay.cli.shell.CliOption;
import com.liferay.cli.shell.CommandMarker;
import com.liferay.cli.shell.osgi.AbstractFlashingObject;

import java.util.logging.Level;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;

/**
 * Allows monitoring of {@link ProcessManager} for development mode users.
 *
 * @author Ben Alex
 * @author Stefan Schmidt
 * @since 1.1
 */
@Service
@Component(immediate = true)
public class ProcessManagerDiagnosticsListener extends AbstractFlashingObject
        implements ProcessManagerStatusListener, CommandMarker {

    private boolean isDebug = false;
    @Reference private ProcessManagerStatusProvider processManagerStatusProvider;

    protected void activate(final ComponentContext context) {
        processManagerStatusProvider.addProcessManagerStatusListener(this);
        isDebug = System.getProperty("ray-args") != null && isDevelopmentMode();
    }

    protected void deactivate(final ComponentContext context) {
        processManagerStatusProvider.removeProcessManagerStatusListener(this);
    }

    public void onProcessManagerStatusChange(
            final ProcessManagerStatus oldStatus,
            final ProcessManagerStatus newStatus) {
        if (isDebug) {
            flash(Level.FINE, newStatus.name(), MY_SLOT);
        }
    }

    @CliCommand(value = "process manager debug", help = "Indicates if process manager debugging is desired", advanced = true)
    public void processManagerDebug(
            @CliOption(key = { "", "enabled" }, mandatory = false, specifiedDefaultValue = "true", unspecifiedDefaultValue = "true", help = "Activates debug mode") final boolean debug) {
        isDebug = debug;
    }
}
