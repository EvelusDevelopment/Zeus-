package org.sini.zeus;

import org.sini.zeus.awt.ApplicationApplet;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import org.sini.zeus.io.CompressionUtils;
import org.sini.zeus.model.Model;
import org.sini.zeus.model.SoftModel;

/**
 * Main.java
 * @version 1.0.0
 * @author Evelus Development (SiniSoul)
 */
class Main extends ApplicationApplet implements Runnable {
    
    /**
     * The local assets directory in the jar file.
     */
    private final static String ASSETSDIR = "/assets/";

    /**
     * The main object that this application is based off of.
     */
    private static Main main = null;
    
    /**
     * The logic cycle count.
     */
    private int logiccycle = 0;
    
    /**
     * The draw cycle count.
     */
    private int drawcycle = 0;

    /**
     * Option for when a repaint was called by the client.
     */
    private boolean repaint = false;
    
    /**
     * Used in web initialization.
     */
    @Override
    public void init() {
        main = new Main();
        main.hostedInitialization(768, 512);
    }

    /**
     * Loads everything.
     */
    @Override
    public void load() {
        
    }

    /**
     * The logic cycle.
     */
    @Override
    public void logicCycle() { 
        logiccycle++;
    }
    
    /**
     * The draw cycle.
     */
    @Override
    public void drawCycle() {
        drawcycle++;
    }

    /**
     * A repaint was requested by the drawable component.
     */
    @Override
    public void paintRequested() {
        repaint = true;
    }
    
    /**
     * Fetches an asset from the local jar file.
     * @param s The name string of the asset to fetch.
     * @return The asset in bytes.
     * @throws Exception An exception was thrown while fetching the asset.
     */
    private static byte[] fetchLocalAsset(String s) throws Exception {
        InputStream in = Main.class.getResourceAsStream(ASSETSDIR + s);
        DataInputStream stream = new DataInputStream(in);
        byte[] bytes = new byte[stream.available()];
        stream.readFully(bytes);
        return bytes;
    }

    /**
     * The entry point for the program.
     * @param args The command line arguments.
     */
    public static void main(String[] args) throws Exception {  
        main = new Main();
        main.localInitialization(768, 512);
        DataInputStream stream = new DataInputStream(new FileInputStream("./model.dat"));
        byte[] bytes = new byte[stream.available()];
        stream.readFully(bytes);
        Model model = new Model(SoftModel.create(bytes));
    }
}
