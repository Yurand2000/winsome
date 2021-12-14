@echo off
echo JUnit Tests
java -jar libs\junit_standalone_executor\junit-platform-console-standalone-1.8.2.jar --disable-ansi-colors --disable-banner --fail-if-no-tests --class-path bin --select-package winsome
pause