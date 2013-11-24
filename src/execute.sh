#!/bin/bash

if [ $# -ne 5 ] ; then
    echo "Usage: bash $0 [MAZE_X_SIZE] [MAZE_Y_SIZE] [MAZE_DIFFICULTY (0/1/2/3)] [PACE (ms/update)] [INVINCIBILITY (0/1)]"
    exit
fi

echo $4 > data/game_info.txt
echo $5 >> data/game_info.txt
javac MazeGenerator.java
java MazeGenerator $1 $2 $3
python input_listener.py &
javac Maze.java
appletviewer Maze.html
pkill -KILL python