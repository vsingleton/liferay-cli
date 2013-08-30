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
LFR_HOME=`dirname "$PRG"`

# Absolute path
LFR_HOME=`cd "$LFR_HOME/.." ; pwd`

# echo Resolved LFR_HOME: $LFR_HOME
# echo "JAVA_HOME $JAVA_HOME"

cygwin=false;
case "`uname`" in
    CYGWIN*)
        cygwin=true
        ;;
esac

# Build a classpath containing our two magical startup JARs (we look for " /" as per LFR-905)
LFR_CP=`echo "$LFR_HOME"/bin/*.jar | sed 's/ \//:\//g'`
# echo LFR_CP: $LFR_CP

# Store file locations in variables to facilitate Cygwin conversion if needed

LFR_OSGI_FRAMEWORK_STORAGE="$LFR_HOME/cache"
# echo "LFR_OSGI_FRAMEWORK_STORAGE: $LFR_OSGI_FRAMEWORK_STORAGE"

LFR_AUTO_DEPLOY_DIRECTORY="$LFR_HOME/bundle"
# echo "LFR_AUTO_DEPLOY_DIRECTORY: $LFR_AUTO_DEPLOY_DIRECTORY"

LFR_CONFIG_FILE_PROPERTIES="$LFR_HOME/conf/config.properties"
# echo "LFR_CONFIG_FILE_PROPERTIES: $LFR_CONFIG_FILE_PROPERTIES"

cygwin=false;
case "`uname`" in
    CYGWIN*)
        cygwin=true
        ;;
esac

if [ "$cygwin" = "true" ]; then
    export LFR_HOME=`cygpath -wp "$LFR_HOME"`
    export LFR_CP=`cygpath -wp "$LFR_CP"`
    export LFR_OSGI_FRAMEWORK_STORAGE=`cygpath -wp "$LFR_OSGI_FRAMEWORK_STORAGE"`
    export LFR_AUTO_DEPLOY_DIRECTORY=`cygpath -wp "$LFR_AUTO_DEPLOY_DIRECTORY"`
    export LFR_CONFIG_FILE_PROPERTIES=`cygpath -wp "$LFR_CONFIG_FILE_PROPERTIES"`
    # echo "Modified LFR_HOME: $LFR_HOME"
    # echo "Modified LFR_CP: $LFR_CP"
    # echo "Modified LFR_OSGI_FRAMEWORK_STORAGE: $LFR_OSGI_FRAMEWORK_STORAGE"
    # echo "Modified LFR_AUTO_DEPLOY_DIRECTORY: $LFR_AUTO_DEPLOY_DIRECTORY"
    # echo "Modified LFR_CONFIG_FILE_PROPERTIES: $LFR_CONFIG_FILE_PROPERTIES"
fi

# make sure to disable the flash message feature for the default OSX terminal, we recommend to use a ANSI compliant terminal such as iTerm if flash message support is desired
APPLE_TERMINAL=false;
if [ "$TERM_PROGRAM" = "Apple_Terminal" ]; then
	APPLE_TERMINAL=true
fi

ANSI="-DLFR.console.ansi=true"
# Hop, hop, hop...
java -Dis.apple.terminal=$APPLE_TERMINAL $LFR_OPTS $ANSI -DLFR.args="$*" -DdevelopmentMode=false -Dorg.osgi.framework.storage="$LFR_OSGI_FRAMEWORK_STORAGE" -Dfelix.auto.deploy.dir="$LFR_AUTO_DEPLOY_DIRECTORY" -Dfelix.config.properties="file:$LFR_CONFIG_FILE_PROPERTIES" -cp "$LFR_CP" com.liferay.cli.bootstrap.Main
EXITED=$?
# echo LFR exited with code $EXITED
