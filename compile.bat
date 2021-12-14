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
call javac -d bin -classpath !libs! -sourcepath src !sources!
if !ERRORLEVEL! NEQ 0 (
	pause
)