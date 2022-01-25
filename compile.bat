@echo off


echo Compiling
call javac -d bin -cp @compile_data\libs_jar.txt @compile_data\sources.txt

echo Making Jars
cd bin
jar cfe WinsomeServer.jar winsome.server_app.ServerAppMain @..\compile_data\classes_server.txt @com_libs.txt
jar cfe WinsomeClient.jar winsome.console_app.ConsoleAppMain @..\compile_data\classes_client.txt @com_libs.txt
cd ..
pause