@echo off
javac -Xdiags:verbose *.java
echo OK
PAUSE
java -ea GameBoard 19 1 1 2 > run.txt
PAUSE
jar -cf Gomuku.jar *.java
java -jar Gomuku.jar