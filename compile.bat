@echo off

setlocal enabledelayedexpansion

cd src
dir /s /B *.java > ../bin/sources.txt
cd ..

set libs=
for %%f in (libs\jackson\*.jar) do (
    set "libs=!libs!;%%f"
)
for %%f in (libs\junit\*.jar) do (
    set "libs=!libs!;%%f"
)

REM echo !libs!

echo Compiling
call javac -d bin -cp !libs! @bin\sources.txt
del bin\sources.txt

cd bin
echo Making Jars
jar cf WinsomeServer.jar *
jar cf WinsomeClient.jar *
cd ..

pause