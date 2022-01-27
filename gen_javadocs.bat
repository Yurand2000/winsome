@echo off

echo Generate Javadocs
call javadoc -d javadocs -sourcepath .\src -subpackages winsome -classpath bin -nonavbar -nohelp -windowtitle Winsome Project
pause