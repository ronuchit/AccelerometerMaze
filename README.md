AccelerometerMaze
=================

Hack project for H@B BearHack Fall 2013. Android phone accelerometer-based maze navigation game on the computer.

//===================================//
   Authors
//===================================//
  Robert Chang
  Rohan Chitnis
  Sydney Wong
  Junwei Yu

//==================================//
   Description
//=================================//
  Libraries used: Android App, Content, Sensors, OS and View; BlueCove 

  The user navigates through a maze by moving his/her android phone.

//==================================//
   Instructions
//=================================//
To use AccelerometerMaze, run the program on your computer and tilt your android phone to control the direction of movement of the red square.  The square will move on its own and you must tilt the phone at the right time to turn the square to move in a new direction.  When you reach the end of the maze a new maze will be generated for you to play again!  If you hit a wall of the maze, you lose and have the option of restarting that maze.

//===============================//
   Further Description
//==============================//
The android phone collects gyroscope and accelerometer data and then calculates relative position of the phone.  Then it translates this position data into direction (up, down, left, or right) and then communicates with the computer via bluetooth and writes the direction to a file, which is read by the maze file.  Then the maze file reads that direction and moves the square on the screen accordingly.
