@echo off
setlocal enabledelayedexpansion

for %%? in ("%~dp0..") do set LFR_HOME=%%~f?
rem echo Resolved LFR_HOME: "%LFR_HOME%"

if not exist "%LFR_HOME%\bootstrap\target\osgi\bin"    mkdir "%LFR_HOME%\bootstrap\target\osgi\bin" 
if not exist "%LFR_HOME%\bootstrap\target\osgi\bundle" mkdir "%LFR_HOME%\bootstrap\target\osgi\bundle" 
if not exist "%LFR_HOME%\bootstrap\target\osgi\conf"   mkdir "%LFR_HOME%\bootstrap\target\osgi\conf" 

copy "%LFR_HOME%\bootstrap\src\main\bin\*.*"  "%LFR_HOME%\bootstrap\target\osgi\bin"  > NUL
copy "%LFR_HOME%\bootstrap\src\main\conf\*.*" "%LFR_HOME%\bootstrap\target\osgi\conf" > NUL

rem Most LFR bundles are not special and belong in "bundle"
copy "%LFR_HOME%\target\all\*.jar" "%LFR_HOME%\bootstrap\target\osgi\bundle" > NUL

rem Move the startup-related JAR from the "bundle" directory to the "bin" directory
move "%LFR_HOME%\bootstrap\target\osgi\bundle\com.liferay.cli.bootstrap-*.jar" "%LFR_HOME%\bootstrap\target\osgi\bin" > NUL 2>&1
move "%LFR_HOME%\bootstrap\target\osgi\bundle\org.apache.felix.framework-*.jar" "%LFR_HOME%\bootstrap\target\osgi\bin" > NUL 2>&1

rem Build a classpath containing our two magical startup JARs
for %%a in ("%LFR_HOME%\bootstrap\target\osgi\bin\*.jar") do set LFR_CP=!LFR_CP!%%a;

rem Hop, hop, hop...
java -Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n -Djline.nobell=true %LFR_OPTS% -Dlfr.args="%*" -DdevelopmentMode=true -Dorg.osgi.framework.storage="%LFR_HOME%\bootstrap\target\osgi\cache" -Dfelix.auto.deploy.dir="%LFR_HOME%\bootstrap\target\osgi\bundle" -Dfelix.config.properties="file:%LFR_HOME%\bootstrap\target\osgi\conf\config.properties" -Dlfr.console.ansi=true -cp "%LFR_CP%" com.liferay.cli.bootstrap.Main
echo LFR exited with code %errorlevel%
