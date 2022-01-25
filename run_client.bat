@echo off

setlocal enabledelayedexpansion
set libs=
for %%f in (libs\jackson\*.jar) do (
    set "libs=!libs!;../%%f"
)
for %%f in (libs\junit\*.jar) do (
    set "libs=!libs!;../%%f"
)

set "libs=!libs!;../bin/WinsomeClient.jar"
REM echo !libs!

cd client_working_directory
call java -cp !libs! winsome.console_app.ConsoleAppMain
cd ..
pause