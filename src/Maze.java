import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Label;
import java.awt.*;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.*;
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
    int pace = 200;
    int startPoint = 0;
    Box b;
    int maze_height, maze_width,move_units;
    String[] heightAndWidth;

    JButton restart;
    JButton start;
    JButton paceButton;
    JButton invulnButton;
    JLabel fail;
    JLabel success;
    MazeGUIGenerator mg;
    Timer timer;
    boolean invincible = false;
    
    public Maze() {

        int pace = 200;
        timer = new Timer(pace,this);
        timer.setInitialDelay(3000);
        /*try {
            BufferedReader br = new BufferedReader(new FileReader(new File("data/game_info.txt")));
            pace = Integer.parseInt(br.readLine());
            invincible = Integer.parseInt(br.readLine());
        } catch (IOException e) {
            System.out.println("this can't happen");
            System.exit(1);
        }*/
        /*if (invincible == 1) {
            System.out.println("Scrub");
            this.invincible = true;
        }*/
        b = new Box();
        try {
            move_units = MoveUnits();
        } catch (IOException e) {
            System.out.println("parse failure");
            System.exit(1);
        }
        b.setX((int)(0.40*move_units));
        b.setY((int)(1.35*move_units));
        add(b);
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

    /*
        Initializes the starting conditions of the maze. Creates the matrix.

    */
    @Override
    public void init() {
        Font f = new Font("serif", Font.BOLD, 100);
        fail = new JLabel("Fail");
        restart = new JButton("Restart?");
        start = new JButton("Start!");
        paceButton = new JButton("200");
        invulnButton = new JButton("Invuln No");
        restart.setFont(f);
        start.setFont(f);
        start.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e)
            {
                if(start.getText() == "Success!"){
                    System.exit(0);
                }
                //Execute when button is pressed
                b.setX((int)(0.40*move_units));
                b.setY((int)(1.35*move_units));
                startPoint = 0;
                b.setScaledX(0);
                b.setScaledY(0);
                start.setVisible(false);
                add(mg);
                timer.start();
            }
        });    
        restart.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e)
            {
                if(restart.getText() == "Success!"){
                    System.exit(0);
                }
                //Execute when button is pressed
                b.setX((int)(0.40*move_units));
                b.setY((int)(1.35*move_units));
                startPoint = 0;
                b.setScaledX(0);
                b.setScaledY(0);
                restart.setVisible(false);
                add(mg);
                timer.restart();
            }
        });    
        paceButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e)
            {
                pace = (pace+200)%2000;
                if(pace == 0){
                    pace = 2000;
                }
                timer.setDelay(pace);
                paceButton.setText(String.valueOf(pace));
            }
        }); 
        invulnButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e)
            {
                invincible = !invincible;
                if(invincible){
                        invulnButton.setText("Invuln Yes");
                }
                else{

                        invulnButton.setText("Invuln No");
                }
            }
        });    
        setSize(APPLET_X_SIZE, APPLET_Y_SIZE);
        mg = new MazeGUIGenerator();
        add(mg);
        JFrame frame = new JFrame ("Start");
        frame.getContentPane().setLayout(new GridLayout(2, 2));
        frame.add (paceButton);
        frame.add (invulnButton);
        frame.pack();
        frame.setVisible(true);
        add(start);
    }
    
    /*
        The rate of the action performed is based on the game info. Using 
        the accelerometer, the player moves around the android phone. If he
        collides into a wall, the game will end, but he will be given a chance
        to reset. Otherwise, if he manages to get to the other side, he wins.
    */
    @Override
    public void actionPerformed(ActionEvent ae) {
        fail.setLocation(0,0);
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
        if(startPoint == 0){
            startPoint++;
            b.setX(b.getX() + move_units);
            return;
        }
        if (direction == 0) { // up
            if(b.getScaledY() == 0 || mg.horizontalWalls[b.getScaledY()-1][b.getScaledX()] == 1){   
                if(invincible){
                    return;
                }
                add(restart);
                restart.setVisible(true);
                timer.stop();
                try {
                  BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/curr_direction.txt"), "utf-8"));
                  writer.write("3");
                  writer.close();
                } catch (IOException e) { }                  
                return;
            }
            b.setY(b.getY() - move_units);
            b.setScaledY(b.getScaledY() - 1);
        } else if (direction == 1) { // down
            if(b.getScaledY() >= maze_height-1 || mg.horizontalWalls[b.getScaledY()][b.getScaledX()] == 1){
                if(invincible){
                    return;
                }
                add(restart);
                restart.setVisible(true);
                timer.stop();
                try {
                  BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/curr_direction.txt"), "utf-8"));
                  writer.write("3");
                  writer.close();
                } catch (IOException e) { }                  
                return;
            }
            b.setY(b.getY() + move_units);
            b.setScaledY(b.getScaledY() + 1);
        } else if (direction == 2) { // left
            if(b.getScaledX() == 0 || mg.verticalWalls[b.getScaledY()][b.getScaledX()-1] == 1){
                if(invincible){
                    return;
                }
                try {
                  BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/curr_direction.txt"), "utf-8"));
                  writer.write("3");
                  writer.close();
                } catch (IOException e) { }                  
                return;
            }
            b.setX(b.getX() - move_units);
            b.setScaledX(b.getScaledX() - 1);
        } else if (direction == 3) { // right
            if(b.getScaledY() == maze_height-1 && b.getScaledX() == maze_width-1){
                b.setX(b.getX() + move_units);
                restart.setText("Success!");
                restart.setVisible(true);
                add(restart);
                timer.stop();
                try {
                  BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/curr_direction.txt"), "utf-8"));
                  writer.write("3");
                  writer.close();
                } catch (IOException e) { }                  
                return;
            }
            else if(b.getScaledX() == maze_width-1 || mg.verticalWalls[b.getScaledY()][b.getScaledX()] == 1){
                if(invincible){
                    return;
                }
                add(restart);
                restart.setVisible(true);
                timer.stop();
                try {
                  BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/curr_direction.txt"), "utf-8"));
                  writer.write("3");
                  writer.close();
                } catch (IOException e) { }                  
                return;
            }
            b.setX(b.getX() + move_units);
            b.setScaledX(b.getScaledX() + 1);
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
