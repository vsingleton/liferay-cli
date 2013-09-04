@echo off
setlocal enabledelayedexpansion

for %%? in ("%~dp0..") do set RAY_HOME=%%~f?
rem echo Resolved RAY_HOME: "%RAY_HOME%"

rem Build a classpath containing our two magical startup JARs
for %%a in ("%RAY_HOME%\bin\*.jar") do set RAY_CP=!RAY_CP!%%a;

rem Hop, hop, hop...
java -Dflash.message.disabled=false -Djline.nobell=true %RAY_OPTS% -DRAY.args="%*" -DdevelopmentMode=false -Dorg.osgi.framework.storage="%RAY_HOME%\cache" -Dfelix.auto.deploy.dir="%RAY_HOME%\bundle" -Dfelix.config.properties="file:%RAY_HOME%\conf\config.properties" -Dray.console.ansi=true -cp "%RAY_CP%" com.liferay.cli.bootstrap.Main
rem echo RAY exited with code %errorlevel%

:end
