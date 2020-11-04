#!/bin/sh
# execute server after db is available

set -e

host="$1"

#until PGPASSWORD=$POSTGRES_PASSWORD psql -h "$host" -U "tumemes" -c '\q'; do
#  >&2 echo "Postgres is unavailable - sleeping"
#  sleep 1
#done
sleep 10s
  
>&2 echo "Postgres is up - starting server"
exec java -jar /service.jar server /config.yml
