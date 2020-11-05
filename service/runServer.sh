#!/bin/sh
# execute server after db is available

set -e

sleep 10s
echo "Postgres is up - starting server"
exec java -jar /service.jar server /config.yml
