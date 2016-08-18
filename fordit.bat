@echo off
javac -Xdiags:verbose Gui.java ComputerMove.java GameBoard.java
echo OK
PAUSE
java GameBoard
PAUSE
jar cf Gomuku.jar Gui.java ComputerMove.java GameBoard.java