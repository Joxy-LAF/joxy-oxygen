#!/bin/bash

# Scriptname
NM=`basename $0`

# Read params
VERBOSE=0
G_VERBOSE=""
while getopts "v" option "$@"
do
  case ${option} in
	v) VERBOSE=1; G_VERBOSE=" -v " ;;
  esac
done


function addslashesforsed
{
    filename=$1

    sed $filename \
        -e 's#\\#\\\\#' \
        -e 's#/#\\/#' \
        -e 's#\.#\\.#' \
        -e 's#\*#\\*#' \
        -e 's#\[#\\[#'

    return $?
}


# Try to determine Java homedirectory
JAVA_HOME="/usr/lib/jvm/java-7-openjdk"
JAVA_HOME_ALT="/usr/lib/jvm/java-6-openjdk"
if [ ! -d $JAVA_HOME ]
then
  JAVA_HOME="/usr/lib/jvm/java-7-openjdk-amd64"
  JAVA_HOME_ALT="/usr/lib/jvm/java-6-openjdk-amd64"
  if [ ! -d $JAVA_HOME ]
  then
	JAVA_HOME="/usr/lib/jvm/java-6-openjdk"
	JAVA_HOME_ALT=""
	if [ ! -d $JAVA_HOME ]
	then
	  JAVA_HOME=""
	  if [ ! -d $JAVA_HOME ]
	  then
		JAVA_HOME=""
	  fi
	fi
  fi
fi
# If two Java installations are found, let the user pick one
if [ -d $JAVA_HOME_ALT ]
then
  echo -ne "$NM: Two Java installations were found:\n    [0] $JAVA_HOME\n    [1] $JAVA_HOME_ALT\n  Please pick one of these (default is [0]): "
  read choice
  if [ " $choice" = " 1" ]
  then
	JAVA_HOME=$JAVA_HOME_ALT
  fi
fi
# No Java installation? :(
if [ -z $JAVA_HOME ]
then
  echo -e "$NM: [EE] Could not find Java home. Looked at:\n\t'/usr/lib/jvm/java-6-openjdk',\n\t'/usr/lib/jvm/java-6-openjdk-amd64'\n\t'/usr/lib/jvm/java-7-openjdk',\n\t'/usr/lib/jvm/java-7-openjdk-amd64'."
  echo "$NM: [EE] Please edit $0 if your Java installation is located somewhere else."
  exit -1
else
  echo "$NM: [II] JAVA_HOME='$JAVA_HOME'"
fi

# Check library path
LIST_SEP="\n    - "
LIST_SEP_ESC=`echo "$LIST_SEP" | addslashesforsed`
CMD="$JAVA_HOME/jre/bin/java getLibraryPath"
if [ $VERBOSE -eq 1 ]
then
  echo "$NM: [II] Running '$CMD'..."
fi
JAVA_LB_PATH=`$CMD | awk -F: -v ORS="$LIST_SEP" '{for (i = 1; i <= NF; i++) print $i}' | sed '$d'`


# START OF COMPILE PIECE
echo "$NM: [II] Now compiling..."

CMD="g++ $G_VERBOSE -I$JAVA_HOME/include -I$JAVA_HOME/include/linux -I/usr/include/qt4 -O0 -g -Wall -Werror -shared -fPIC -lQtGui -lQtCore -o libnativeTextRenderer.so joxy_utils_JoxyGraphics.cpp joxy_utils_JoxyGraphics.h"
if [ $VERBOSE -eq 1 ]
then
  echo "$NM: [II] Running '$CMD'..."
fi
$CMD

if [ $? -eq 0 ]
then
  echo "$NM: [II] Done. File 'libnativeTextRenderer.so' created."
  echo -e "$NM: [II] You should move this file to the Java library path. \n  Currently, the following directories are present in your Java library path:$LIST_SEP$JAVA_LB_PATH\n  You should pick one of these folders to move the shared library to."
else
  echo "$NM: [EE] Compiling not succesfull. Please check g++ output or Joxy wiki."
fi
