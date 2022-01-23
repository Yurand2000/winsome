@echo off

setlocal enabledelayedexpansion
set libs=
set sources=
for %%f in (libs\jackson\*.jar) do (
    set libs=!libs!%%f;
)
for %%f in (libs\junit\*.jar) do (
    set libs=!libs!%%f;
)

cd src
for /r %%f in (*.java) do (
	set "sources=!sources!%%f "
)
cd ..

REM echo !sources!
REM echo !libs!

echo Compiling
echo !sources! > sources.txt
echo !libs! > libs.txt
mkdir bin
call javac -d bin -classpath @libs.txt -sourcepath src @sources.txt
del sources.txt
del libs.txt
pause