@echo off
setlocal enabledelayedexpansion

for %%? in ("%~dp0..") do set LFR_HOME=%%~f?
rem echo Resolved LFR_HOME: "%LFR_HOME%"

rem Build a classpath containing our two magical startup JARs
for %%a in ("%LFR_HOME%\bin\*.jar") do set LFR_CP=!LFR_CP!%%a;

rem Hop, hop, hop...
java -Dflash.message.disabled=false -Djline.nobell=true %LFR_OPTS% -DLFR.args="%*" -DdevelopmentMode=false -Dorg.osgi.framework.storage="%LFR_HOME%\cache" -Dfelix.auto.deploy.dir="%LFR_HOME%\bundle" -Dfelix.config.properties="file:%LFR_HOME%\conf\config.properties" -Dlfr.console.ansi=true -cp "%LFR_CP%" com.liferay.cli.bootstrap.Main
rem echo LFR exited with code %errorlevel%

:end
