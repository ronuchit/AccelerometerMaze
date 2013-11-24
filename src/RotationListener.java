package de.vogella.android.sensor;

import java.util.ArrayList;
import java.lang.Math;

public void RotationListener(Array positions) {
  int UP, DOWN, LEFT, RIGHT = 0, 1, 2, 3;
  ArrayList<Double> currData;
  float motionThreshold = 2.0;
  float zeroThreshold = 0.5;
  boolean canMove = true;
  double[] currAvg;
  currData = new ArrayList<Double>();

  //write 3 to curr_direction
  BufferedWriter out = null;
  try {
    FileWriter fstream = new FileWriter("data/curr_direction.txt", false);
    out = new BufferedWriter(fstream);
    out.write("3");
   }
   catch (IOException e) {
     System.err.println("Error: " + e.getMessage());
   }
   finally {
     if(out != null) {
       out.close();
     }
   }

  while true:
    refreshData();
    currAvg = {0.0, 0.0};
    Double total = 0.0;
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 10; j++) {
        total += currAvg[j][i]; 
      }
    currAvg[i] = total/10;
    }
    for (Double d : currAvg) {
      System.out.println(d);
    }
    if ((Math.abs(currAvg[0]) < zeroThreshold) & (Math.abs(currAvg[1]) < zeroThreshold) {
      canMove = true;
    } else {
      for (i = 0; i < currAvg.length(); i++) {
        if (Math.abs(currAvg[i]) > motionThreshold) {
          if (currAvg[i] > motionThreshold) {
            if (i == 0) {
              System.out.println("right");
              if (canMove) {
                registerDirection(RIGHT);
              }
              canMove = false;
            } else {
              System.out.println("up");
              if (canMove) {
                registerDirection(UP);
              }
              canMove = false;
            }
          } else {
            if (i == 0) {
              System.out.println("left");
              if (canMove) {
                registerDirection(LEFT);
              }
              canMove = false;
            } else {
              System.out.println("down");
              if (canMove) {
                registerDirection(DOWN);
              }
              canMove = false;
            }
          }
        } 
      }
    }

  private void registerDirection(int direction) {
  	out = null;
  try {
    FileWriter fstream = new FileWriter("data/curr_direction.txt", false);
    out = new BufferedWriter(fstream);
    out.write(direction);
   }
   catch (IOException e) {
     System.err.println("Error: " + e.getMessage());
   }
   finally {
     if(out != null) {
       out.close();
     }
   }
  }

  private void refreshData() {
    File file = new File("data/pos_data.txt");
    BufferedReader reader = null;

    try {
      reader = new BufferedReader(new FileReader(file));
      String text = null;

      while ((text = reader.readLine()) != null) {
        currData.add(text);
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (reader != null) {
          reader.close();
        }
      } catch (IOException e) {
      }
    }
  }
}






