package org.sini.zeus.awt;

import java.awt.Frame;
import java.awt.Graphics;

/**
 * ApplicationFrame.java
 * @version 1.0.0
 * @author Evelus Development (SiniSoul)
 */
public class ApplicationFrame extends Frame {
    
    /**
     * The base applet for this frame.
     */
    private ApplicationApplet applet = null;
    
    /**
     * Updates this frame.
     * @param graphics  The graphics to update.
     */
    @Override
    public void update(Graphics graphics) {
        applet.update(graphics);
    }
    
    /**
     * Paints this frame.
     * @param graphics The graphics to paint on.
     */
    @Override
    public void paint(Graphics graphics) {
        applet.paint(graphics);
    }
    
    /**
     * Gets the graphics for this frame.
     * @return The graphics for this frame.
     */
    @Override
    public Graphics getGraphics() {
        Graphics g = super.getGraphics();
        g.translate(0, 25);
        return g;
    }
    
    /**
     * @param app The application applet base.
     * @param width The width of the frame.
     * @param height The height of the frame.
     */
    ApplicationFrame(ApplicationApplet app, int width, int height) {
        applet = app;
        setTitle("Evelus | Zeus");
        setSize(width, height);
        setResizable(false);
        setVisible(true);
        toFront();
    }   
}
