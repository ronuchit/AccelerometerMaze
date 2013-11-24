#!/bin/bash

if [ $# -ne 5 ] ; then
    echo "Usage: bash $0 [MAZE_X_SIZE] [MAZE_Y_SIZE] [MAZE_DIFFICULTY (0/1/2/3)] [PACE (0/1/2)] [INVINCIBILITY (0/1)]"
    exit
fi

javac MazeGenerator.java
java MazeGenerator $1 $2 $3
python input_listener.py &
javac Maze.java
appletviewer Maze.html