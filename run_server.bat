@echo off

echo Starting Server
cd server_working_directory
call java -jar ../bin/WinsomeServer.jar
cd ..
pause