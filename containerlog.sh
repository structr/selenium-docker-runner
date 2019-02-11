#!/bin/sh
docker exec -ti $(docker ps | grep structr |   cut -d' ' -f37) /bin/sh -c "tail -f /var/lib/structr/logs/server.log"
