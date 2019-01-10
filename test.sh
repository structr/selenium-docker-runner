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
MAX_TRIES=5

# command line parameters
SOURCE=$1
TESTSUITE=$2

function started() {
	curl -si $BASE_URL:$PORT/structr/rest >/dev/null
}

function cancel() {
	echo "Error: Structr not responding, cancelling server set up"
	docker stop $CONTAINER
}

function usage() {
	echo "usage: $0 <deployment source> <testsuite.side> [-logs]"
}

if [ -z "$SOURCE" ]; then
	usage
	exit 0
fi

if [ -z "$TESTSUITE" ]; then
	usage
	exit 0
fi

docker network create $NETWORK

CONTAINER=$(docker create --network $NETWORK --net-alias structr -p $PORT:8082 structr/structr:$VERSION)

echo "Created new image $CONTAINER with version $VERSION"
echo "Copying resources.."
docker cp license.key $CONTAINER:/var/lib/structr/license.key
docker cp $SOURCE $CONTAINER:/tmp/webapp

echo "Starting container.."
docker start $CONTAINER

echo "Waiting for Structr to become available..."
sleep 10
until started || [ $MAX_TRIES -eq 0 ]; do
	echo "$((MAX_TRIES--)) attempts remaining.."
	sleep 10
done

if [ $MAX_TRIES -eq 0 ]; then
	cancel
fi

echo "Deploying webapp from $SOURCE.."
curl -si -HX-User:$USERNAME -HX-Password:$PASSWORD $BASE_URL:$PORT/structr/rest/maintenance/deploy -d '{ mode: import, source: "/tmp/webapp" }'

echo "Creating test image.."
docker image build -q -t selenium-test .
TESTCONTAINER=$(docker create --network $NETWORK selenium-test)
docker cp $TESTSUITE $TESTCONTAINER:/tmp/
docker start -a $TESTCONTAINER

if [ "$3" == "-logs" ]; then
	echo "Fetching logs.."
	docker logs $CONTAINER
fi

echo "Stopping container.."
docker stop $CONTAINER
docker stop $TESTCONTAINER

echo "Removing containers"
docker container rm $CONTAINER
docker container rm $TESTCONTAINER

echo "Removing isolated network"
docker network rm $NETWORK

echo "Removing test directory.."
rm -rf $CONTAINER

