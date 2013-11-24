/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 *
 * @author Robert
 */
public class MazeGUIGenerator extends JPanel {

    public static final int VERTICAL = 1;
    private static final int PREF_W = 500;
    private static final int PREF_H = PREF_W;
    public int[][] verticalWalls;
    public int[][] horizontalWalls;
    public int[][] walls;
    public int height;
    public int width;    
    private int scaledValue;


    /*Parses the boolean matrix off the HorizontalMaze text file. First loop
      creates the vertical walls, second loop creates horizontal walls. Takes
      info from the text.
    */
    public void Parse() throws FileNotFoundException, IOException {
        File file;
        for (int i = 0; i < 2; i++) {
            if (i == VERTICAL) {
                file = new File("../data/vWalls.txt");
            }
            else {
               file = new File("../data/hWalls.txt");
            }
            int count = -1;
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = br.readLine()) != null) {
                if (count == -1) {
                    String[] heightAndWidth = line.split(" ");
                    width = new Integer(heightAndWidth[0]);
                    height = new Integer(heightAndWidth[1]);
                    this.walls = new int[height][width];
                } else {
                    String[] numbers = line.split(" ");
                    for (int j = 0; j < width-i ; j++) {
                        walls[count][j] = new Integer(numbers[j]);
                    }
                }
                count++;
            }
            scaledValue = 500/Math.max(height, width);
            if (i == VERTICAL) {
                verticalWalls = walls;
            } else {
                horizontalWalls = walls;
            }
        }
    }

    

    /**
     * *
     * This algorithm prints actual graph. Based on the boolean matrix, it prints a vertical
     * or horizontal matrix if valid.
     *
     * @param g is the graphics interface the program uses.
     * @author Robert
     * @throws java.io.IOException
     */
    @Override
    protected void paintComponent(Graphics g) {
        try {
            Parse();
        } catch (IOException ex) {
            System.out.println("parse failure");
            System.exit(1);
        }
        
        Graphics2D g2 = (Graphics2D) g;
        for (int i = 0; i < height; i++) {
            for (int j = -1; j < width; j++) {
                //if black.
                g2.setColor(Color.BLACK);
                if (j == -1 || j == width-1 || verticalWalls[i][j] == 1) {
                    if(!(i == 0 && j == -1) && !(i== height-1 && j == width-1 )) {
                        Rectangle r = new Rectangle((j+2) * scaledValue, (i+1) * scaledValue, 1, scaledValue);
                        g2.draw(r);
                    }
                }
            }
        }
        
        for (int i = -1; i < height; i++) {
            for (int j = 0; j < width; j++) {
                //if black.
                g2.setColor(Color.BLACK);
                if ( i<0 || i == height-1 || horizontalWalls[i][j] == 1) {
                    Rectangle r = new Rectangle((j+1) * scaledValue, (i+2) * scaledValue, scaledValue, 1);
                    g2.draw(r);
                }
            }
        }
    }
}
