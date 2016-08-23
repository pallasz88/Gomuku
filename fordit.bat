@echo off
javac -Xdiags:verbose *.java
echo OK
PAUSE
java -ea GameBoard 19 1 1 2
PAUSE
jar cf Gomuku.jar Gui.java ComputerMove.java GameBoard.java