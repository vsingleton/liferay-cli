package com.liferay.cli.shell.osgi;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Provides levels for Ray's shell mode
 *
 * @author Gregory Amerson
 */
public class ShellMode implements Comparable<ShellMode> {

    public static final ShellMode BASIC = new ShellMode("BASIC");
    public static final ShellMode ADVANCED = new ShellMode("ADVANCED");
    private final String key;

    public ShellMode(final String key) {
        Validate.notBlank(key, "Key required");
        this.key = key;
    }

    public final int compareTo(final ShellMode o) {
        if (o == null) {
            return -1;
        }
        return key.compareTo(o.key);
    }

    @Override
    public final boolean equals(final Object obj) {
        return obj instanceof ShellMode && compareTo((ShellMode) obj) == 0;
    }

    public String getKey() {
        return key;
    }

    @Override
    public final int hashCode() {
        return key.hashCode();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("key", key);
        return builder.toString();
    }
}
