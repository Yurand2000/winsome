@echo off

echo JUnit Tests
java -jar libs\junit_standalone_executor\junit-platform-console-standalone-1.8.2.jar --disable-ansi-colors --disable-banner --fail-if-no-tests -cp bin --include-classname=.* --select-package winsome
pause

