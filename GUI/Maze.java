package maze;
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
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.Timer;



public class Maze extends JApplet implements ActionListener{
    int length = 0;
    int x = 0;
    public Maze() {
        Timer timer = new Timer(500, this);
        timer.setInitialDelay(5000);
        timer.start();
    }
    public void init(){
        setSize( 1000, 1000 );
		Box a = new Box();
                add(a);
		//getContentPane().add(a);          
		//topPanel.add( labelHello, BorderLayout.NORTH ); 
    }
    @Override
    public void actionPerformed(ActionEvent ae) {
        setSize(1000+x, 1000+x );
        if(x == 1){
            x--;
        }
        else{
            x++;
        }
        this.length+=20;
        Box b = new Box();
        b.setLength(this.length);
        add(b);
    }
    
}

class VerticalMaze extends JPanel{
    
}

class Box extends JPanel {
    int a = 1;
    int length;
    private static final int PREF_W = 500;
    private static final int PREF_H = PREF_W;
    /**
     * *
     * This adds a letter.b
     *
     * @params: x coordinate, y coordinate, letter.
     * @author Robert
     */
    public void setLength(int length) {
        this.length = length;
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
        Rectangle r = new Rectangle(length, length, 10, 10);
        g2.draw(r);
    }
}