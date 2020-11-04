#!/bin/bash

set -e

psql -v ON_ERROR_STOP=true -d tumemes -a -U tumemes --password test -f /createSchema.sql
