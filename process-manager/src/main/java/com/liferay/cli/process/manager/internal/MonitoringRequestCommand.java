package com.liferay.cli.process.manager.internal;

import org.apache.commons.lang3.Validate;
import com.liferay.cli.file.monitor.FileMonitorService;
import com.liferay.cli.file.monitor.MonitoringRequest;

import com.liferay.cli.process.manager.CommandCallback;

/**
 * Represents a {@link CommandCallback} to start or stop monitoring a particular
 * file path.
 * 
 * @author Ben Alex
 * @since 1.0
 */
public class MonitoringRequestCommand implements CommandCallback<Boolean> {

    private final boolean add;
    private final FileMonitorService fileMonitorService;
    private final MonitoringRequest monitoringRequest;

    public MonitoringRequestCommand(
            final FileMonitorService fileMonitorService,
            final MonitoringRequest monitoringRequest, final boolean add) {
        Validate.notNull(fileMonitorService, "File monitor service required");
        Validate.notNull(monitoringRequest, "Request required");
        this.fileMonitorService = fileMonitorService;
        this.monitoringRequest = monitoringRequest;
        this.add = add;
    }

    public Boolean callback() {
        boolean result;
        if (add) {
            result = fileMonitorService.add(monitoringRequest);
        }
        else {
            result = fileMonitorService.remove(monitoringRequest);
        }
        fileMonitorService.scanAll();
        return result;
    }
}
