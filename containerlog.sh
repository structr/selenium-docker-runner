#!/bin/sh
docker exec -ti $(docker ps | grep structr |   cut -d' ' -f37- | xargs) /bin/sh -c "tail -f /var/lib/structr/logs/server.log"
