@echo off

setlocal enabledelayedexpansion

set sources=
cd src
for /r %%f in (*.java) do (
	set "sources=!sources!%%f "
)
cd ..
REM echo !sources!

echo Generation Javadocs
call javadoc -d javadoc !sources!