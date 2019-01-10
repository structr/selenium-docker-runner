#!/bin/bash
#
# test.sh - run Structr deployment and selenium tests in a dockerized setup
#
# prerequisites:
# - docker
# - curl
#

BASE_URL=http://localhost
PORT=11223
USERNAME=admin
PASSWORD=admin
VERSION=3.1.1
NETWORK=selenium-tests
MAX_TRIES=60
WAIT=5
LOG=/dev/null

# command line parameters
SOURCE="$1"
TESTSUITE="$2"
REUSE_EXISTING=yes

function started() {
	curl -si $BASE_URL:$PORT/structr/rest >/dev/null
}

function cancel() {
	echo "Error: Structr not responding, cancelling server set up"
	docker stop $CONTAINER
}

function usage() {
	echo "usage: $0 <deployment source> <testsuite.side> [-u]"
	echo "parameters:"
	echo "    <deployment source> - structr deployment export to test"
	echo "    <testsuite.side>    - Selenium test suite to run"
	echo "options:"
	echo "    -u  - force update even if deployed image exists"

	exit 0
}

if [ -z "$SOURCE" ]; then
	usage
fi

if [ -z "$TESTSUITE" ]; then
	usage
fi

if [ -n "$3" ]; then
	LOG="$3"
fi

# check for existing image, reuse if it exists (and not forced to update)
if docker inspect --type=image structr:selenium >/dev/null 2>&1; then

	# forced to update? don't use existing image
	if [ "$3" == "-u" ]; then
		REUSE_EXISTING=no
	fi

else

	# no existing image, create new one
	REUSE_EXISTING=no

fi

echo "Creating isolated network.." |tee -a $LOG
docker network create $NETWORK >>$LOG

if [ "$REUSE_EXISTING" == "yes" ]; then

	echo "Re-using existing container structr:selenium" |tee -a $LOG
	CONTAINER=$(docker create --network $NETWORK --net-alias structr -p $PORT:8082 structr:selenium)

	echo "Starting container.." |tee -a $LOG
	docker start $CONTAINER >>$LOG

	echo "Waiting for Structr to become available..." |tee -a $LOG
	sleep $WAIT
	until started || [ $MAX_TRIES -eq 0 ]; do
		echo -n "."
		sleep $WAIT
	done

	echo "."

	if [ $MAX_TRIES -eq 0 ]; then
		cancel
	fi

else

	echo "Creating new container from version $VERSION" |tee -a $LOG
	CONTAINER=$(docker create --network $NETWORK --net-alias structr -p $PORT:8082 structr/structr:$VERSION)

	echo "Copying resources.." |tee -a $LOG
	docker cp license.key $CONTAINER:/var/lib/structr/license.key >>$LOG
	docker cp $SOURCE $CONTAINER:/tmp/webapp >>$LOG

	echo "Starting container.." |tee -a $LOG
	docker start $CONTAINER >>$LOG

	echo "Waiting for Structr to become available..." |tee -a $LOG
	sleep $WAIT
	until started || [ $MAX_TRIES -eq 0 ]; do
		echo -n "."
		sleep $WAIT
	done
	
	echo "."

	if [ $MAX_TRIES -eq 0 ]; then
		cancel
	fi

	echo "Deploying webapp from $SOURCE.." |tee -a $LOG
	curl -si -HX-User:$USERNAME -HX-Password:$PASSWORD $BASE_URL:$PORT/structr/rest/maintenance/deploy -d '{ mode: import, source: "/tmp/webapp" }' >>$LOG

	echo "Storing Structr image for later use.." |tee -a $LOG
	docker commit $CONTAINER structr:selenium >>$LOG

fi

echo "Creating test container.." |tee -a $LOG
docker image build -q -t selenium-test . >>$LOG
TESTCONTAINER=$(docker create --network $NETWORK selenium-test)
docker cp $TESTSUITE $TESTCONTAINER:/tmp/ >>$LOG
docker start -a $TESTCONTAINER |tee -a $LOG

if [ "$3" == "-logs" ]; then
	echo "Fetching logs.."
	docker logs $CONTAINER >>$LOG
fi

echo "Stopping container.." |tee -a $LOG
docker stop $CONTAINER >>$LOG
docker stop $TESTCONTAINER >>$LOG

echo "Removing image and containers" |tee -a $LOG
docker image rm selenium-test >>$LOG
docker container rm $CONTAINER >>$LOG
docker container rm $TESTCONTAINER >>$LOG

echo "Removing isolated network" |tee -a $LOG
docker network rm $NETWORK >>$LOG

echo "Removing test directory.." |tee -a $LOG
rm -rf $CONTAINER >>$LOG

