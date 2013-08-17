#!/bin/bash

# Scriptname
NM=`basename $0`

# From http://stackoverflow.com/a/246128/962603
BASEDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )"/../../.. && pwd )"

OUTPUT="$BASEDIR/install/libjoxy.so"

# Read params
VERBOSE=0 # if this script should be verbose
G_VERBOSE="" # option to pass to g++, to make it verbose
while getopts "dv" option "$@"
do
	case ${option} in
		d) VERBOSE=1 ;;
		v) VERBOSE=1; G_VERBOSE=" -v " ;;
	esac
done


function addslashesforsed() {
	filename=$1

	sed $filename \
		-e 's#\\#\\\\#' \
		-e 's#/#\\/#' \
		-e 's#\.#\\.#' \
		-e 's#\*#\\*#' \
		-e 's#\[#\\[#'

	return $?
}

function log() {
	# Options for echo?
	local OPTIONS=
	if [[ "${1:0:1}" = "-" ]]; then
		OPTIONS=$1
		shift
	fi
	
	# Log level?
	local PREFIX=" [I]"
	if [[ "$1" = "debug" ]]; then
		if [[ $VERBOSE -eq 1 ]]; then
			PREFIX=" [D]"
			shift
		else
			return
		fi
	elif [[ "$1" = "error" ]]; then
		PREFIX=" [E]"
		shift
	elif [[ "$1" = "info" ]]; then
		PREFIX=" [I]"
		shift
	elif [[ "$1" = "warning" ]]; then
		PREFIX=" [W]"
		shift
	fi
	
	# Actual echo statement
	echo $OPTIONS "$NM:$PREFIX $1"
}

# Try to determine Java homedirectory
if [ -z "$JAVA_HOME" ] || [ ! -d $JAVA_HOME ]; then
	declare -a POTENTIAL_JAVA_HOMES=('/usr/lib/jvm/java-7-openjdk' '/usr/lib/jvm/java-6-openjdk' '/usr/lib/jvm/java-7-openjdk-i386' '/usr/lib/jvm/java-6-openjdk-i386' '/usr/lib/jvm/java-7-openjdk-amd64' '/usr/lib/jvm/java-6-openjdk-amd64' '/usr/lib/jvm/java-7' '/usr/lib/jvm/java-6')
	declare -a FOUND_JAVA_HOMES=()
	
	for POTENTIAL_JAVA_HOME in ${POTENTIAL_JAVA_HOMES[@]}; do
		if [ -d $POTENTIAL_JAVA_HOME ]; then
			FOUND_JAVA_HOMES=(${FOUND_JAVA_HOMES[@]} $POTENTIAL_JAVA_HOME)
		fi
	done
	
	# If multiple Java installations are found, let the user pick one
	if [ ${#FOUND_JAVA_HOMES[@]} -gt 1 ]; then
		log "Multiple Java installations were found:"
		INDEX=0
		for FOUND_JAVA_HOME in ${FOUND_JAVA_HOMES[@]}; do
			log "  [$INDEX]: ${FOUND_JAVA_HOMES[$INDEX]}"
			INDEX=$((INDEX + 1))
		done
		
		# Read the user's choice
		choice=nan
		while [[ "$choice" = *[!0-9]* || ( ! -z "$choice" && ( $choice -lt 0 || $choice -ge ${#FOUND_JAVA_HOMES[@]} ) ) ]]; do
			log -n "Please pick one of these (default is [0]): "
			read choice
		done
		
		# Using default?
		if [[ -z "$choice" ]]; then choice=0; fi
		JAVA_HOME=${FOUND_JAVA_HOMES[$choice]}
	fi
fi

# No Java installation? :(
if [ -z "$JAVA_HOME" ] || [ ! -d $JAVA_HOME ]; then
	log error "Could not find Java home. Looked at:"
	for LOOKED_AT in ${POTENTIAL_JAVA_HOMES[@]}; do
		log error "  $LOOKED_AT"
	done
	log error "Please edit $0 if your Java installation is located somewhere else."
	exit 1
else
	log debug "JAVA_HOME='$JAVA_HOME'"
fi

# START OF COMPILE PIECE
log info "Now compiling..."

CMD="g++ $G_VERBOSE -I$JAVA_HOME/include -I$JAVA_HOME/include/linux -I/usr/include/qt4 -O0 -g -Wall -Werror -shared -z defs -fPIC -o "${OUTPUT}" $BASEDIR/src/main/cpp/joxy_utils_JoxyGraphics.cpp $BASEDIR/src/main/cpp/joxy_utils_JoxyGraphics.h -lQtGui -lQtCore"
log debug "Running '$CMD'..."
$CMD

if [ $? -eq 0 ]; then
  log info "Done. File '${OUTPUT}' created."
  log info "You should move this file to the Java library path."
  log info "Currently, the following directories are present in your Java library path:"
  $JAVA_HOME/jre/bin/java -cp $BASEDIR/src/main/scripts/ getLibraryPath | awk -F: '{for (i = 1; i <= NF; i++) print $i}' | sed 's/^/'$NM': [I]   /'
  log info "You should pick one of these folders to move the shared library to."
else
  log error "Compiling not succesful. Please check g++ output or Joxy wiki."
fi
