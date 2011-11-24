package org.sini.zeus.awt;

import org.sini.zeus.awt.registries.KeyboardRegistry;
import org.sini.zeus.awt.registries.MouseRegistry;

import java.applet.Applet;
import java.awt.Component;
import java.awt.Graphics;

/**
 * ApplicationFrame.java
 * @version 1.0.0
 * @author Evelus Development (SiniSoul)
 */
public class ApplicationApplet extends Applet implements Runnable {

    /**
     * The graphics for this application frame.
     */
    protected Graphics graphics = null;
    
    /**
     * The frame of the application if running on a local machine.
     */
    private ApplicationFrame frame = null;
    
    /**
     * The applet's width.
     */
    protected int appletwidth = -1;
    
    /**
     * The applet's height.
     */
    protected int appletheight = -1;

    /**
     * The keyboard registry.
     */
    protected KeyboardRegistry keyboardregistry = null;

    /**
     * The mouse registry.
     */
    protected MouseRegistry mouseregistry = null;

    /**
     * The profile times for this application.
     */
    private long[] profiles = null;

    /**
     * The minimum delta.
     */
    private int minimumdelta = -1;

    /**
     * The maximum delta.
     */
    private int maximumdelta = -1;

    /**
     * The frames per second.
     */
    private int fps = -1;

    /**
     * The runnable method of this class.
     */
    public void run() {
        keyboardregistry.bind(getDrawableComponent());
        mouseregistry.bind(getDrawableComponent());
        load();
        int ratio = 256;
        int intex = 0;
        int count = 0;
        int ppos = 0;
        int del = 1;
        for(int i = 0; i < profiles.length; i++)
            profiles[i] = System.currentTimeMillis();
        while(true) {
            int r = ratio;
            int d = del;
            ratio = 300;
            del = 1;
            long stamp = System.currentTimeMillis();
            if(profiles[ppos] == 0L) {
                ratio = r;
                del = d;
            } else
            if(stamp > profiles[ppos])
                ratio = (int)((long) (2560 * maximumdelta) / (stamp - profiles[ppos]));
            if(ratio < 25)
                ratio = 25;
            if(ratio > 256) {
                ratio = 256;
                del = (int)((long) maximumdelta - (stamp - profiles[ppos]) / 10L);
            }
            if(del > maximumdelta)
                del = maximumdelta;
            profiles[ppos] = stamp;
            ppos = (ppos + 1) % profiles.length;
            if(del > 1) {
                for(int k2 = 0; k2 < 10; k2++)
                    if(profiles[k2] != 0L)
                        profiles[k2] += del;
            }
            if(del < minimumdelta)
                del = minimumdelta;
            try {
                Thread.sleep(del);
            } catch(Exception ex) {
                intex++;
            }
            for(; count < 256; count += ratio) {
                logicCycle();
            }
            count &= 0xff;
            if(maximumdelta > 0)
                fps = (1000 * ratio) / (maximumdelta * 256);
            drawCycle();
        }
    }
    
    /**
     * Initializes this applet for local machine use.
     * @param width The width of the applet.
     * @param height The height of the applet.
     */
    public void localInitialization(int width, int height) {
        frame = new ApplicationFrame(this, width, height);
        graphics = getDrawableComponent().getGraphics();
        appletheight = height;
        appletwidth = width;   
        createThread(this, 1);
    }
    
    /**
     * Initializes this applet for being hosted on the web.
     * @param width The width of the applet.
     * @param height The height of the applet.
     */
    public void hostedInitialization(int width, int height) {
        graphics = getDrawableComponent().getGraphics();
        appletheight = height;
        appletwidth = width;    
        createThread(this, 1);
    }

    /**
     * Updates this application frame.
     * @param g The graphics to paint on.
     */
    @Override
    public void update(Graphics g) {
        if(graphics == null)
            graphics = g;
        paintRequested();
    }

    /**
     * Paints this application frame.
     * @param g The graphics to paint on.
     */
    @Override
    public void paint(Graphics g) {
        if(graphics == null)
            graphics = g;
        paintRequested();
    }

    /**
     * A call to paint was requested.
     */
    protected void paintRequested() { }

    /**
     * Does a logic cycle.
     */
    protected void logicCycle() { }

    /**
     * Does a draw cycle.
     */
    protected void drawCycle() { }

    /**
     * Initializes this application.
     */
    protected void load() { }

    /**
     * Gets the drawable component for this applet.
     * @return The drawable component for this applet.
     */
    protected Component getDrawableComponent() {
        if(frame != null)
            return frame;
        return this;
    }

    /**
     * Creates and starts a new thread.
     * @param runnable The runnable method to use for the thread.
     * @param priority The threads priority.
     */
    public void createThread(Runnable runnable, int priority) {
        Thread thread = new Thread(runnable);
        thread.setPriority(priority);
        thread.start();       
    }

    /**
     */
    public ApplicationApplet() {
        keyboardregistry = new KeyboardRegistry();
        mouseregistry = new MouseRegistry();
        profiles = new long[10];
        minimumdelta = 1;
        maximumdelta = 1;   
    }
}
