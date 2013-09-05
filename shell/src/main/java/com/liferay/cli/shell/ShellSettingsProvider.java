package com.liferay.cli.shell;


public interface ShellSettingsProvider
{
    String ADVANCED_MODE = "ADVANCED_MODE";

    public Object get(String settingsKey);
}
