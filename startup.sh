#!/usr/bin/env bash

# Export environment variables
. ./export-env-dev.sh

# Create network
docker network create spotfinder

# Create MongoDB container (remove port for production if server is into a container => same network communication)
docker run -d --network spotfinder --name mongo-spotfinder \
  -e MONGO_INITDB_ROOT_USERNAME="root" \
  -e MONGO_INITDB_ROOT_PASSWORD="root" \
  -p 27017:27017 mongo

# Create mongo-express container
docker run -d --network spotfinder --name mongo-express-spotfinder \
  -e ME_CONFIG_MONGODB_SERVER="mongo-spotfinder" \
  -e ME_CONFIG_MONGODB_ADMINUSERNAME="root" \
  -e ME_CONFIG_MONGODB_ADMINPASSWORD="root" \
  -p 8081:8081 mongo-express
