@echo off

setlocal enabledelayedexpansion
set libs=
for %%f in (libs\jackson\*.jar) do (
    set "libs=!libs!;../%%f"
)
for %%f in (libs\junit\*.jar) do (
    set "libs=!libs!;../%%f"
)

set "libs=!libs!;../bin/WinsomeServer.jar"
REM echo !libs!

echo Starting Server
cd server_working_directory
call java -cp !libs! winsome.server_app.ServerAppMain
cd ..
pause