#!/bin/bash

dockerfile="templates/Dockerfile"
composeFile="templates/docker-compose.yml"
imageVersionPattern="%version%"
grafanaUserIdPattern="%grafana-user-id%"
currentUserId=$(id -u)

read -p 'Version to deploy (e.g. 0.0.3): ' version

deleteFolder() {
    read -p "$1" choice
    if [ "$choice" = "y" ] || [ "$choice" = "Y" ]; then
        sudo rm -rf $2
        echo "Folder $2 deleted"
    elif [ "$choice" = "n" ] || [ "$choice" = "N" ]; then
        echo "No delete"
    else
        echo "Unknow choice..."
        exit 0
    fi
}

createFolder() {
    if [ -d "$1" ]; then
        echo "Directory $1 exist"
    else
        $(mkdir -p $1)
        echo "Directory $1 created"
    fi
}

# Delete existing folders (TODO => Extract in a function)
echo "--> Delete existing directories"
# read -p 'Delete pictures directory? [yY/nN]: ' deletePictures
deleteFolder 'Delete pictures directory? [yY/nN]: ' pictures
deleteFolder 'Delete mongo directory? [yY/nN]: ' mongo
deleteFolder 'Delete monitoring directory? [yY/nN]: ' monitoring
# Create folders
echo "--> Create directories"
createFolder pictures
createFolder mongo
createFolder monitoring/grafana
# Update docker-compose.yml template
echo "--> Generate Docker Compose via template"
cp templates/docker-compose-template.yml templates/docker-compose_generate.yml
sed -i "s/$imageVersionPattern/$version/gi" "templates/docker-compose_generate.yml"
sed -i "s/$grafanaUserIdPattern/$currentUserId/gi" "templates/docker-compose_generate.yml"
mv templates/docker-compose_generate.yml ./docker-compose.yml
# Move Prometheus configuration
echo "--> Move Prometheus configuration"
cp templates/prometheus.yml ./monitoring/prometheus.yml
# Clean environment containers
echo "--> Clean previous existing environment"
docker-compose down
docker container stop $(docker ps -a -q --filter "name=spotfinder-*")
docker container rm $(docker ps -a -q --filter "name=spotfinder-*")
# Create clean docker compose env
echo "--> Build && deploy"
docker network create nginx-proxy
docker-compose build --no-cache && docker-compose up -d && echo "--> Done :)"
