/* MazeGenerator.java */

import java.util.*;
import java.io.*;
import utils.*;
import java.lang.*;

/**
 *  The MazeGenerator class represents a maze in a rectangular grid.  There is exactly
 *  one path between any two points.
 **/

public class MazeGenerator {

  // Horizontal and vertical dimensions of the maze.
  protected int horiz;
  protected int vert;
  // Horizontal and vertical interior walls; each is true if the wall exists.
  protected boolean[][] hWalls;
  protected boolean[][] vWalls;

  // Object for generating random numbers.
  private static Random random;

  // Constants used in depth-first search (which checks for cycles in the
  // maze).
  private static final int STARTHERE = 0;
  private static final int FROMLEFT = 1;
  private static final int FROMRIGHT = 2;
  private static final int FROMABOVE = 3;
  private static final int FROMBELOW = 4;

  /**
   *  MazeGenerator() creates a rectangular maze having "horizontalSize" cells in the
   *  horizontal direction, and "verticalSize" cells in the vertical direction.
   *  There is a path between any two cells of the maze.  A disjoint set data
   *  structure is used to ensure that there is only one path between any two
   *  cells.
   **/
  public MazeGenerator(int horizontalSize, int verticalSize) {
    int i, j;

    horiz = horizontalSize;
    vert = verticalSize;
    if ((horiz < 1) || (vert < 1) || ((horiz == 1) && (vert == 1))) {
      return;                                    // There are no interior walls
    }

    // Create all of the horizontal interior walls.  Initially, every
    // horizontal wall exists; they will be removed later by the maze
    // generation algorithm.
    if (vert > 1) {
      hWalls = new boolean[horiz][vert - 1];
      for (j = 0; j < vert - 1; j++) {
        for (i = 0; i < horiz; i++) {
          hWalls[i][j] = true;
        }
      }
    }
    // Create all of the vertical interior walls.
    if (horiz > 1) {
      vWalls = new boolean[horiz - 1][vert];
      for (i = 0; i < horiz - 1; i++) {
        for (j = 0; j < vert; j++) {
          vWalls[i][j] = true;
        }
      }
    }

    DisjointSets cells = new DisjointSets(horiz * vert);

    int numWalls = horiz * (vert - 1) + (horiz - 1) * vert;
    Wall[] walls = new Wall[numWalls];
    for (j = 0; j < vert - 1; j++) {
        for (i = 0; i < horiz; i++) {
            walls[j * horiz + i] = new Wall(true, i, j);
        }
    }
    for (i = 0; i < horiz - 1; i++) {
        for (j = 0; j < vert; j++) {
            walls[(horiz * (vert - 1)) + i * vert + j] = new Wall(false, i, j);
        }
    }
    
    for (int w = numWalls; w > 1; w--) {
        int randNum = randInt(w);
        Wall temp = walls[w - 1];
        walls[w - 1] = walls[randNum];
        walls[randNum] = temp;
    }

    for (Wall wall : walls) {
        int neighbor1 = wall.j * horiz + wall.i;
        int neighbor2;
        if (wall.horiz) {
            neighbor2 = (wall.j + 1) * horiz + wall.i;
        } else {
            neighbor2 = wall.j * horiz + wall.i + 1;
        }

        int root1 = cells.find(neighbor1);
        int root2 = cells.find(neighbor2);
        if (root1 != root2) {
            cells.union(root1, root2);
            if (wall.horiz) {
                hWalls[wall.i][wall.j] = false;
            } else {
                vWalls[wall.i][wall.j] = false;
            }
        }
    }
    
  }

  /**
   *  toString() returns a string representation of the maze.
   **/
  public String toString() {
    int i, j;
    String s = "";

    // Print the top exterior wall.
    for (i = 0; i < horiz; i++) {
      s = s + "--";
    }
    s = s + "-\n|";

    // Print the maze interior.
    for (j = 0; j < vert; j++) {
      // Print a row of cells and vertical walls.
      for (i = 0; i < horiz - 1; i++) {
        if (vWalls[i][j]) {
          s = s + " |";
        } else {
          s = s + "  ";
        }
      }
      s = s + " |\n+";
      if (j < vert - 1) {
        // Print a row of horizontal walls and wall corners.
        for (i = 0; i < horiz; i++) {
          if (hWalls[i][j]) {
            s = s + "-+";
          } else {
            s = s + " +";
          }
        }
        s = s + "\n|";
      }
    }

    // Print the bottom exterior wall.  (Note that the first corner has
    // already been printed.)
    for (i = 0; i < horiz; i++) {
      s = s + "--";
    }
    return s + "\n";
  }

  /**
   * horizontalWall() determines whether the horizontal wall on the bottom
   * edge of cell (x, y) exists.  If the coordinates (x, y) do not correspond
   * to an interior wall, true is returned.
   **/
  public boolean horizontalWall(int x, int y) {
    if ((x < 0) || (y < 0) || (x > horiz - 1) || (y > vert - 2)) {
      return true;
    }
    return hWalls[x][y];
  }

  /**
   * verticalWall() determines whether the vertical wall on the right edge of
   * cell (x, y) exists. If the coordinates (x, y) do not correspond to an
   * interior wall, true is returned.
   **/
  public boolean verticalWall(int x, int y) {
    if ((x < 0) || (y < 0) || (x > horiz - 2) || (y > vert - 1)) {
      return true;
    }
    return vWalls[x][y];
  }

  /**
   * randInt() returns a random integer from 0 to choices - 1.
   **/
  private static int randInt(int choices) {
    if (random == null) {       // Only executed first time randInt() is called
      random = new Random();       // Create a "Random" object with random seed
    }
    int r = random.nextInt() % choices;      // From 1 - choices to choices - 1
    if (r < 0) {
      r = -r;                                          // From 0 to choices - 1
    }
    return r;
  }

  protected boolean good_difficulty(int difficulty) {
    boolean[][] cellVisited = new boolean[horiz][vert];
    int num_turns = depthFirstSearch(0, 0, STARTHERE, cellVisited, 0);
    int thres1 = 3, thres2 = 6, thres3 = 9, thres4 = 12;
    if (difficulty == 0) {
      return num_turns >= thres1 && num_turns < thres2;
    } else if (difficulty == 1) {
      return num_turns >= thres2 && num_turns < thres3;
    } else if (difficulty == 2) {
      return num_turns >= thres3 && num_turns < thres4;
    } else if (difficulty == 3) {
      return num_turns >= thres4;
    } else {
      System.out.println("Invalid difficulty (0/1/2/3)");
      System.exit(1);
    }

    return false;
  }

  protected int depthFirstSearch(int x, int y, int fromWhere,
                                     boolean[][] cellVisited, int turns) {
    cellVisited[x][y] = true;
    if (x == horiz - 1 && y == vert - 1) {
      return turns;
    }
    int t1 = -1, t2 = -1, t3 = -1, t4 = -1;

    // Visit the cell to the right?
    if ((fromWhere != FROMRIGHT) && !verticalWall(x, y)) {
      if (!cellVisited[x + 1][y]) {
        if (fromWhere == FROMLEFT) {
          t1 = depthFirstSearch(x + 1, y, FROMLEFT, cellVisited, turns);
        } else {
          t1 = depthFirstSearch(x + 1, y, FROMLEFT, cellVisited, turns + 1);
        }
      }
    }

    // Visit the cell below?
    if ((fromWhere != FROMBELOW) && !horizontalWall(x, y)) {
      if (!cellVisited[x][y + 1]) {
        if (fromWhere == FROMABOVE) {
          t2 = depthFirstSearch(x, y + 1, FROMABOVE, cellVisited, turns);
        } else {
          t2 = depthFirstSearch(x, y + 1, FROMABOVE, cellVisited, turns + 1);
        }
      }
    }

    // Visit the cell to the left?
    if ((fromWhere != FROMLEFT) && !verticalWall(x - 1, y)) {
      if (!cellVisited[x - 1][y]) {
        if (fromWhere == FROMRIGHT) {
          t3 = depthFirstSearch(x - 1, y, FROMRIGHT, cellVisited, turns);
        } else {
          t3 = depthFirstSearch(x - 1, y, FROMRIGHT, cellVisited, turns + 1);
        }
      }
    }

    // Visit the cell above?
    if ((fromWhere != FROMABOVE) && !horizontalWall(x, y - 1)) {
      if (!cellVisited[x][y - 1]) {
        if (fromWhere == FROMBELOW) {
          t4 = depthFirstSearch(x, y - 1, FROMBELOW, cellVisited, turns);
        } else {
          t4 = depthFirstSearch(x, y - 1, FROMBELOW, cellVisited, turns + 1);
        }
      }
    }

    return Math.max(t1, Math.max(t2, Math.max(t3, t4)));
  }

  protected void write_walls() {
    Writer writer = null;

    try {
      writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("../data/hWalls.txt"), "utf-8"));
      writer.write(horiz + " " + vert + "\n");
      for(int i = 0; i < hWalls[0].length; i++) {
        for(int j = 0; j < hWalls.length; j++) {
          if (hWalls[j][i]) {
            writer.write("1 ");
          } else {
            writer.write("0 ");
          }
        }
        if (i != hWalls[0].length - 1) {
            writer.write("\n");
        }
      }
      writer.close();
    } catch (IOException e) {
      System.out.println("Failed hWalls write to file");
      System.exit(1);
    }

    try {
      writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("../data/vWalls.txt"), "utf-8"));
      writer.write(horiz + " " + vert + "\n");
      for(int i = 0; i < vWalls[0].length; i++) {
        for(int j = 0; j < vWalls.length; j++) {
          if (vWalls[j][i]) {
            writer.write("1 ");
          } else {
            writer.write("0 ");
          }
        }
        if (i != vWalls[0].length - 1) {
            writer.write("\n");
        }
      }
      writer.close();
    } catch (IOException e) {
      System.out.println("Failed vWalls write to file");
      System.exit(1);
    }
  }

  /**
   * main() creates a maze of dimensions specified on the command line, prints
   * the maze, and runs the diagnostic method to see if the maze is good.
   */
  public static void main(String[] args) {
    if (args.length != 3) {
      System.out.println("Usage: java MazeGenerator [dim1] [dim2] [difficulty 0/1/2/3]");
      System.exit(1);
    }
    int horiz = 0, vert = 0, difficulty = 0;
    try {
      horiz = Integer.parseInt(args[0]);
      vert = Integer.parseInt(args[1]);
      difficulty = Integer.parseInt(args[2]);
    } catch (NumberFormatException e) {
      System.out.println("Usage: java MazeGenerator [dim1] [dim2]");
      System.exit(1);
    }

    MazeGenerator maze = new MazeGenerator(horiz, vert);
    while (!maze.good_difficulty(difficulty)) {
      System.out.println("Generating harder maze");
      maze = new MazeGenerator(horiz, vert);
    }
    maze.write_walls();
    System.out.print(maze);
    // maze.diagnose();
  }

}

class Wall {
    boolean horiz;
    int i;
    int j;

    Wall(boolean horiz, int x, int y) {
        this.horiz = horiz;
        i = x;
        j = y;
    }   
}
