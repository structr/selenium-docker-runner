# selenium-docker-runner

### Prerequisites
- Docker

### Usage

	Usage
	    test.sh [-c] [-d <webapp>] [-h] [-n <image name>] [-l <logfile>] [-r] [-u] [-v <version> [-t <testsuite>]

	Options
	    -c              - dump configuration and exit
	    -d <webapp>     - deploy the webapp from the given directory
	    -h              - print this message and exit
	    -l <logfile>    - use given log file (default: /dev/null)
	    -n <image name> - use given test image name (default: structr:selenium)
	    -r              - recording mode (dont run tests, just start the instance)
	    -t <testsuite>  - run tests in the given directory
	    -u              - update test image (don't resuse existing images)
	    -v <version>    - use given Structr version (default: 3.1.1)

	Docker reference
	    list containers         - docker container ls [-a]
	    run shell on container  - docker exec -ti <name> /bin/sh
	    list images             - docker image ls [-a]
	    manage containers       - docker container rm <name> | create <name> | start <name> | stop <name>
	    manage images           - docker image rm <id> | create | start <id> | stop <id>
	    manage networks         - docker network ls | rm | create | start <name> | stop <name>
	    fetch logs / stdout     - docker logs [-f] <name>


