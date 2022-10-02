#!/bin/bash
# https://pythonspeed.com/articles/faster-db-tests/

DIRECTORY=`dirname $0`
DIRECTORY=$(realpath $DIRECTORY)

docker run --name postgres-hibernate-array \
 -e 'POSTGRES_PASSWORD=Cent-Quick-Space-Bath-8' \
 -e POSTGRES_USER=jdbc \
 -p 5432:5432 \
 --mount type=tmpfs,destination=/var/lib/postgresql/data \
 -v ${DIRECTORY}/sql/postgres:/docker-entrypoint-initdb.d \
 -d postgres:14.5-alpine
