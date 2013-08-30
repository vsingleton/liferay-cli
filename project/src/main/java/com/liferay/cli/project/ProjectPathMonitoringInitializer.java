package com.liferay.cli.project;

import static com.liferay.cli.file.monitor.event.FileOperation.CREATED;
import static com.liferay.cli.file.monitor.event.FileOperation.DELETED;
import static com.liferay.cli.file.monitor.event.FileOperation.MONITORING_FINISH;
import static com.liferay.cli.file.monitor.event.FileOperation.MONITORING_START;
import static com.liferay.cli.file.monitor.event.FileOperation.RENAMED;
import static com.liferay.cli.file.monitor.event.FileOperation.UPDATED;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;
import com.liferay.cli.file.monitor.DirectoryMonitoringRequest;
import com.liferay.cli.file.monitor.MonitoringRequest;
import com.liferay.cli.file.monitor.NotifiableFileMonitorService;
import com.liferay.cli.file.monitor.event.FileOperation;
import com.liferay.cli.file.undo.UndoManager;
import com.liferay.cli.metadata.MetadataDependencyRegistry;
import com.liferay.cli.metadata.MetadataNotificationListener;

@Component(immediate = true)
@Service
public class ProjectPathMonitoringInitializer implements
        MetadataNotificationListener {

    private static final FileOperation[] MONITORED_OPERATIONS = {
            MONITORING_START, MONITORING_FINISH, CREATED, RENAMED, UPDATED,
            DELETED };

    @Reference private NotifiableFileMonitorService fileMonitorService;
    @Reference private MetadataDependencyRegistry metadataDependencyRegistry;
    @Reference private PathResolver pathResolver;
    private boolean pathsRegistered;
    @Reference private UndoManager undoManager;

    protected void activate(final ComponentContext context) {
        metadataDependencyRegistry.addNotificationListener(this);
    }

    protected void deactivate(final ComponentContext context) {
        metadataDependencyRegistry.removeNotificationListener(this);
    }

    private void monitorPathIfExists(final LogicalPath logicalPath) {
        final String canonicalPath = pathResolver.getRoot(logicalPath);
        // The path can be blank if a sub-folder contains a POM that doesn't
        // belong to a module
        if (StringUtils.isNotBlank(canonicalPath)) {
            final File directory = new File(canonicalPath);
            if (directory.isDirectory()) {
                final MonitoringRequest request = new DirectoryMonitoringRequest(
                        directory, true, MONITORED_OPERATIONS);
                new UndoableMonitoringRequest(undoManager, fileMonitorService,
                        request, true);
            }
        }
    }

    private void monitorProjectPaths() {
        for (final LogicalPath logicalPath : pathResolver.getPaths()) {
            if (requiresMonitoring(logicalPath)) {
                monitorPathIfExists(logicalPath);
            }
        }
    }

    public void notify(final String upstreamDependency,
            final String downstreamDependency) {
        if (pathsRegistered) {
            return;
        }
        monitorProjectPaths();
        pathsRegistered = true;
    }

    private boolean requiresMonitoring(final LogicalPath logicalPath) {
        if (logicalPath.isProjectRoot()) {
            return false; // already monitored by ProcessManager
        }
        if (StringUtils.isBlank(logicalPath.getModule())) {
            return true; // non-root path within root module
        }
        return logicalPath.isModuleRoot();
    }
}
