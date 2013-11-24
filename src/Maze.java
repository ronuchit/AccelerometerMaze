import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JLabel;
/*<applet code=applet.java width=1000 height=1000>
</applet>*/ 

import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.Timer;

import java.io.*;

public class Maze extends JApplet implements ActionListener {
    static final int APPLET_X_SIZE = 1000;
    static final int APPLET_Y_SIZE = 1000;
    
    int size_delta = 0;
    Box b;
    int maze_height, maze_width,move_units;
    String[] heightAndWidth;
    MazeGUIGenerator mg;
    Timer timer;
    boolean invincible = false;
    
    public Maze() {
        int pace = 0, invincible = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("data/game_info.txt")));
            pace = Integer.parseInt(br.readLine());
            invincible = Integer.parseInt(br.readLine());
        } catch (IOException e) {
            System.out.println("this can't happen");
            System.exit(1);
        }
        if (invincible == 1) {
            this.invincible = true;
        }
        timer = new Timer(pace, this);
        timer.setInitialDelay(1000);
        b = new Box();
        try {
            move_units = MoveUnits();
        } catch (IOException e) {
            System.out.println("parse failure");
            System.exit(1);
        }
        System.out.println("File is " + move_units);
        b.setX((int)(0.4*move_units));
        b.setY((int)(1.35*move_units));
    }
    
    public int MoveUnits() throws FileNotFoundException, IOException {
        File file = new File("data/vWalls.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();
        heightAndWidth = line.split(" ");
        maze_width = new Integer(heightAndWidth[0]);
        maze_height = new Integer(heightAndWidth[1]);
        return 250 / Math.max(maze_height, maze_width);
    }
    @Override
    public void init() {
        JLabel fail;
        fail = new JLabel(getParameter("HURUR"));
        setSize(APPLET_X_SIZE, APPLET_Y_SIZE);
        mg = new MazeGUIGenerator();
        add(mg);
        timer.start();
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        // hack for refresh
        setSize(APPLET_X_SIZE + size_delta, APPLET_Y_SIZE + size_delta);
        if(size_delta == 1) {
            size_delta--;
        }
        else {
            size_delta++;
        }
        File file = new File("data/curr_direction.txt");
        int direction = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            direction = Integer.parseInt(br.readLine());
        } catch (FileNotFoundException e) {
            System.out.println("File curr_direction.txt not found");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Failed curr_direction.txt read");
            System.exit(1);
        }
        if (direction == 0) { // up
            b.setY(b.getY() - move_units);
            if(b.getScaledY() == 0 || mg.horizontalWalls[b.getScaledX()][b.getScaledY()-1] == 1){
                timer.stop();
            }
        } else if (direction == 1) { // down
            b.setY(b.getY() + move_units);
            if(b.getScaledY() == 0 || mg.horizontalWalls[b.getScaledX()][b.getScaledY()] == 1){
                timer.stop();
            }
        } else if (direction == 2) { // left
            b.setX(b.getX() - move_units);
            if(b.getScaledX() == 0 || mg.verticalWalls[b.getScaledX()-1][b.getScaledY()] == 1){
                timer.stop();
            }
        } else if (direction == 3) { // right
            b.setX(b.getX() + move_units);
        }
        add(b);
    }

}

class Box extends JPanel {
    int x = -20, y = 0;
    int scaled_x = 0;
    int scaled_y = 0;
    private static final int PREF_W = 500;
    private static final int PREF_H = PREF_W;
    public void setScaledX(int x) {
        this.scaled_x = x;
    }
    
    public void setScaledY(int y) {
        this.scaled_y = y;
    }

    public int getScaledX() {
        return scaled_x;
    }

    public int getScaledY() {
        return scaled_y;
    }
    public void setX(int x) {
        this.x = x;
    }
    
    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(PREF_W, PREF_H);
    }

    /**
     * *
     * This algorithm prints the actual picture to the GUI. In the case where a
     * box isn't filled by the end of the function, the box will be filled
     * black.
     *
     * @param g is the graphics interface the program uses.
     * @author Robert
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.RED);
        g2.fillRect(x, y, 10, 10);
    }
}
