@echo off

setlocal enabledelayedexpansion
set libs=
for %%f in (libs\jackson\*.jar) do (
    set libs=!libs!%%f;
)
for %%f in (libs\junit\*.jar) do (
    set libs=!libs!%%f;
)

echo Generate Javadocs
call javadoc -d javadocs -sourcepath .\src -subpackages winsome -public -classpath !libs!
pause