#!/bin/bash
mvnw -DskipTests install
cp ladderbackend/target/demo-1.0.0-SNAPSHOT.war app.war
mvnw pre-integration-test -Prun-full-stack
#To start the frontend locally, uncomment the following lines and run:
#cd ladderfrontend
#ng serve