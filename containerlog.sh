#!/bin/sh
docker logs -f $(docker ps --format "{{.ID}} {{.Ports}}" | grep 11223 | cut -d' ' -f1)
