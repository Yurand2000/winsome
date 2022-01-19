@echo off

setlocal enabledelayedexpansion
set libs=
for %%f in (libs\jackson\*.jar) do (
    set libs=!libs!%%f;
)
for %%f in (libs\junit\*.jar) do (
    set libs=!libs!%%f;
)

echo JUnit Tests
java -jar libs\junit_standalone_executor\junit-platform-console-standalone-1.8.2.jar --disable-ansi-colors --disable-banner --fail-if-no-tests --class-path bin -classpath !libs! --select-package winsome
pause