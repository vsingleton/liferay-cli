@echo off
setlocal enabledelayedexpansion

for %%? in ("%~dp0..") do set ray_HOME=%%~f?
rem echo Resolved ray_HOME: "%ray_HOME%"

if not exist "%ray_HOME%\bootstrap\target\osgi\bin"    mkdir "%ray_HOME%\bootstrap\target\osgi\bin" 
if not exist "%ray_HOME%\bootstrap\target\osgi\bundle" mkdir "%ray_HOME%\bootstrap\target\osgi\bundle" 
if not exist "%ray_HOME%\bootstrap\target\osgi\conf"   mkdir "%ray_HOME%\bootstrap\target\osgi\conf" 

copy "%ray_HOME%\bootstrap\src\main\bin\*.*"  "%ray_HOME%\bootstrap\target\osgi\bin"  > NUL
copy "%ray_HOME%\bootstrap\src\main\conf\*.*" "%ray_HOME%\bootstrap\target\osgi\conf" > NUL

rem Most ray bundles are not special and belong in "bundle"
copy "%ray_HOME%\target\all\*.jar" "%ray_HOME%\bootstrap\target\osgi\bundle" > NUL

rem Move the startup-related JAR from the "bundle" directory to the "bin" directory
move "%ray_HOME%\bootstrap\target\osgi\bundle\com.liferay.cli.bootstrap-*.jar" "%ray_HOME%\bootstrap\target\osgi\bin" > NUL 2>&1
move "%ray_HOME%\bootstrap\target\osgi\bundle\org.apache.felix.framework-*.jar" "%ray_HOME%\bootstrap\target\osgi\bin" > NUL 2>&1

rem Build a classpath containing our two magical startup JARs
for %%a in ("%ray_HOME%\bootstrap\target\osgi\bin\*.jar") do set ray_CP=!ray_CP!%%a;

rem Hop, hop, hop...
rem java -javaagent:d:/jrebel.jar -Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=y -Djline.nobell=true %ray_OPTS% -Dray.args="%*" -DdevelopmentMode=true -Dorg.osgi.framework.storage="%ray_HOME%\bootstrap\target\osgi\cache" -Dfelix.auto.deploy.dir="%ray_HOME%\bootstrap\target\osgi\bundle" -Dfelix.config.properties="file:%ray_HOME%\bootstrap\target\osgi\conf\config.properties" -Dray.console.ansi=true -cp "%ray_CP%" com.liferay.cli.bootstrap.Main
java -Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n -Djline.nobell=true %ray_OPTS% -Dray.args="%*" -DdevelopmentMode=true -Dorg.osgi.framework.storage="%ray_HOME%\bootstrap\target\osgi\cache" -Dfelix.auto.deploy.dir="%ray_HOME%\bootstrap\target\osgi\bundle" -Dfelix.config.properties="file:%ray_HOME%\bootstrap\target\osgi\conf\config.properties" -Dray.console.ansi=true -cp "%ray_CP%" com.liferay.cli.bootstrap.Main
echo ray exited with code %errorlevel%
