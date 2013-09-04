#!/bin/sh

PRG="$0"

while [ -h "$PRG" ]; do
    ls=`ls -ld "$PRG"`
    link=`expr "$ls" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
        PRG="$link"
    else
        PRG=`dirname "$PRG"`/"$link"
    fi
done
ray_HOME=`dirname "$PRG"`

# Absolute path
ray_HOME=`cd "$ray_HOME/.." ; pwd`

# echo Resolved ray_HOME: $ray_HOME
# echo "JAVA_HOME $JAVA_HOME"

cygwin=false;
case "`uname`" in
    CYGWIN*)
        cygwin=true
        ;;
esac

# Build a classpath containing our two magical startup JARs (we look for " /" as per ray-905)
ray_CP=`echo "$ray_HOME"/bin/*.jar | sed 's/ \//:\//g'`
# echo ray_CP: $ray_CP

# Store file locations in variables to facilitate Cygwin conversion if needed

ray_OSGI_FRAMEWORK_STORAGE="$ray_HOME/cache"
# echo "ray_OSGI_FRAMEWORK_STORAGE: $ray_OSGI_FRAMEWORK_STORAGE"

ray_AUTO_DEPLOY_DIRECTORY="$ray_HOME/bundle"
# echo "ray_AUTO_DEPLOY_DIRECTORY: $ray_AUTO_DEPLOY_DIRECTORY"

ray_CONFIG_FILE_PROPERTIES="$ray_HOME/conf/config.properties"
# echo "ray_CONFIG_FILE_PROPERTIES: $ray_CONFIG_FILE_PROPERTIES"

cygwin=false;
case "`uname`" in
    CYGWIN*)
        cygwin=true
        ;;
esac

if [ "$cygwin" = "true" ]; then
    export ray_HOME=`cygpath -wp "$ray_HOME"`
    export ray_CP=`cygpath -wp "$ray_CP"`
    export ray_OSGI_FRAMEWORK_STORAGE=`cygpath -wp "$ray_OSGI_FRAMEWORK_STORAGE"`
    export ray_AUTO_DEPLOY_DIRECTORY=`cygpath -wp "$ray_AUTO_DEPLOY_DIRECTORY"`
    export ray_CONFIG_FILE_PROPERTIES=`cygpath -wp "$ray_CONFIG_FILE_PROPERTIES"`
    # echo "Modified ray_HOME: $ray_HOME"
    # echo "Modified ray_CP: $ray_CP"
    # echo "Modified ray_OSGI_FRAMEWORK_STORAGE: $ray_OSGI_FRAMEWORK_STORAGE"
    # echo "Modified ray_AUTO_DEPLOY_DIRECTORY: $ray_AUTO_DEPLOY_DIRECTORY"
    # echo "Modified ray_CONFIG_FILE_PROPERTIES: $ray_CONFIG_FILE_PROPERTIES"
fi

# make sure to disable the flash message feature for the default OSX terminal, we recommend to use a ANSI compliant terminal such as iTerm if flash message support is desired
APPLE_TERMINAL=false;
if [ "$TERM_PROGRAM" = "Apple_Terminal" ]; then
	APPLE_TERMINAL=true
fi

ANSI="-Dray.console.ansi=true"
# Hop, hop, hop...
java -Dis.apple.terminal=$APPLE_TERMINAL $ray_OPTS $ANSI -Dray.args="$*" -DdevelopmentMode=false -Dorg.osgi.framework.storage="$ray_OSGI_FRAMEWORK_STORAGE" -Dfelix.auto.deploy.dir="$ray_AUTO_DEPLOY_DIRECTORY" -Dfelix.config.properties="file:$ray_CONFIG_FILE_PROPERTIES" -cp "$ray_CP" com.liferay.cli.bootstrap.Main
EXITED=$?
# echo ray exited with code $EXITED
