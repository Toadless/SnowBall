#!/usr/bin/env bash

git pull
./gradlew clean build
docker build .
tagName=$(docker images | awk '{print $3}' | awk 'NR==2')
docker tag $tagName toadless/snowball:latest
docker login
docker push toadless/snowball:latest
echo "Done!"