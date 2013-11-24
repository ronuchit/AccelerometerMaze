package maze;


import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JPanel;

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
