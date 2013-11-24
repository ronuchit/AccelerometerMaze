/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maze;

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
public class VerticalMaze extends JPanel{

    private static final int PREF_W = 500;
    private static final int PREF_H = PREF_W;
    public int[][] verticalWalls;
    private int length;
    private int height;
    private int width;

    //Parses the boolean matrix off the VerticalMaze text file.
    public void Parse() throws FileNotFoundException, IOException {
        File file = new File("C:\\Users\\Robert\\Documents\\vertical.txt");
        int count = -1;
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = null;
        while ((line = br.readLine()) != null) {
            if (count == -1) {
                String[] heightAndWidth = line.split(" ");
                height = new Integer(heightAndWidth[0]);
                width = new Integer(heightAndWidth[1]);
                this.verticalWalls = new int[height][width];
            } else {
                String[] numbers = line.split(" ");
                for (int i = 0; i < width; i++) {
                    verticalWalls[count][i] = new Integer(numbers[i]);
                }
            }
            count++;
        }
    }


    /**
     * *
     * This algorithm prints the actual picture to the GUI. In the case where a
     * box isn't filled by the end of the function, the box will be filled
     * black.
     *
     * @param g is the graphics interface the program uses.
     * @author Robert
     * @throws java.io.IOException
     */
    @Override
    protected void paintComponent(Graphics g){
        try {
            Parse();
        } catch (IOException ex) {
            Logger.getLogger(VerticalMaze.class.getName()).log(Level.SEVERE, null, ex);
        }
        Graphics2D g2 = (Graphics2D) g;
        System.out.println(height);
        System.out.println(width);
        for (int i = 0; i < verticalWalls.length; i++) {
            for (int j = 0; j < verticalWalls[i].length; j++) {
                //if black.
                g2.setColor(Color.BLACK);
                if (verticalWalls[i][j] == 1) {
                    Rectangle r = new Rectangle(j * 20, i * 20, 1, 20);
                    g2.draw(r);
                }
            }
        }
    }
}
