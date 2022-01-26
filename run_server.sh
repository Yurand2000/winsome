#!/bin/bash

echo Starting Server
cd server_working_directory
java -jar ../bin/WinsomeServer.jar
cd ..
echo Press ENTER to close...
read