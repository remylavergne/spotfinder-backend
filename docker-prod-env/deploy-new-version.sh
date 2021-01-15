#!/bin/bash

imageVersionPattern="%version%"
grafanaUserIdPattern="%grafana-user-id%"
currentUserId=$(id -u)

read -p 'Version to deploy (e.g. 0.0.3): ' version

# Update docker-compose.yml template
echo "--> Generate Docker Compose via template"
cp templates/docker-compose-template.yml templates/docker-compose_generate.yml
sed -i "s/$imageVersionPattern/$version/gi" "templates/docker-compose_generate.yml"
sed -i "s/$grafanaUserIdPattern/$currentUserId/gi" "templates/docker-compose_generate.yml"
mv templates/docker-compose_generate.yml ./docker-compose.yml
# Clean environment containers
echo "--> Stop && remove previous backend container"
docker container stop $(docker ps -a -q --filter "name=spotfinder-backend")
docker container rm $(docker ps -a -q --filter "name=spotfinder-backend")
# Create clean docker compose env
echo "--> Rebuild && deploy backend"
docker-compose up -d --no-deps --force-recreate --build spotfinder-backend && echo "--> Done :)"
