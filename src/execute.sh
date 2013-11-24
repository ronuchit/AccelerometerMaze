#!/bin/bash

if [ $# -ne 3 ] ; then
    echo "Usage: bash $0 [MAZE_X_SIZE] [MAZE_Y_SIZE] [MAZE_DIFFICULTY (0/1/2/3)]"
    exit
fi

javac MazeGenerator.java
javac Maze.java
java MazeGenerator $1 $2 $3
python input_listener.py &
appletviewer Maze.html
pkill -KILL python
rm *.class
rm *.pyc