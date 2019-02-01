#!/bin/bash
#
# console.sh - record selenium tests against a dockerized Structr setup
#

# check for existence of structr-selenium-dsl and build the project
if [ ! -e structr-selenium-dsl-0.1-SNAPSHOT.jar ]; then 

	echo "Building selenium test runner.."
	cd structr-selenium-dsl
	mvn clean package
	cp target/structr-selenium-dsl-0.1-SNAPSHOT.jar ..
	cd ..
fi

java -jar structr-selenium-dsl-0.1-SNAPSHOT.jar -e firefox -v -i $*
