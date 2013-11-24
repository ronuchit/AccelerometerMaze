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
    @Override
    public void init(){
        setSize( 1000, 1000 );
		Box a = new Box();
                VerticalMaze vertical = new VerticalMaze();
                add(vertical);
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


