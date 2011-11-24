package org.sini.zeus.graphics;

import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.PixelGrabber;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * DirectColorSprite.java
 * @version 1.0.0
 * @author Evelus Development (SiniSoul)
 */
public class DirectColorSprite {

    /**
     * The pixel array for this sprite.
     */
    private int[] pixels = null;

    /**
     * The width of this sprite.
     */
    private int width = -1;

    /**
     * The height of this sprite.
     */
    private int height = -1;

    /**
     * The offset x for this sprite.
     */
    private int offsetx = -1;

    /**
     * The offset y for this sprite.
     */
    private int offsety = -1;

    /**
     * Draws this sprite.
     * @param x The x coordinate to draw this sprite.
     * @param y The y coordinate to draw this sprite.
     */
    public void draw(int x, int y) {
        x += offsetx;
        y += offsety;
        int outoffset = x + y * BasicRasterizer.outputwidth;
        int srcoffset = 0;
        int h = height;         /* TEMPORARY */
        int w = width;          /* TEMPORARY */
        int srcincrement = BasicRasterizer.outputwidth - w;
        int outincrement = 0;
        if(y < BasicRasterizer.heightoffset) {
            int j2 = BasicRasterizer.heightoffset - y;
            h -= j2;
            y = BasicRasterizer.heightoffset;
            srcoffset += j2 * w;
            outoffset += j2 * BasicRasterizer.outputwidth;
        }
        if(y + h > BasicRasterizer.height)
            h -= (y + h) - BasicRasterizer.height;
        if(x < BasicRasterizer.widthoffset) {
            int k2 = BasicRasterizer.widthoffset - x;
            w -= k2;
            x = BasicRasterizer.widthoffset;
            srcoffset += k2;
            outoffset += k2;
            outincrement += k2;
            srcincrement += k2;
        }
        if(x + w > BasicRasterizer.width) {
            int l2 = (x + w) - BasicRasterizer.width;
            w -= l2;
            outincrement += l2;
            srcincrement += l2;
        }
        if(w <= 0 || h <= 0)
            return;
        draw(BasicRasterizer.output, pixels, outoffset, srcoffset, srcincrement, outincrement, w, h);
    }

    /**
     * Draws pixels into an output with no special implications.
     * @param out The output for this draw method.
     * @param src The source of the pixels.
     * @param outoffset The offset in the out array.
     * @param srcoffset The offset in the source array.
     * @param outheight The height increment for the out array.
     * @param srcheight The height increment for the source array.
     * @param width The width of the source to draw.
     * @param height The height of the source to draw.
     */
    private static void draw(int[] out, int[] src, int outoffset, int srcoffset, int outheight, int srcheight, int width, int height) {
        int widthoffset0 = -(width >> 2);
        width = -(width & 3);
        for(int i = -height; i < 0; i++) {
            for(int j = widthoffset0; j < 0; j++) {
                out[outoffset++] = src[srcoffset++];
                out[outoffset++] = src[srcoffset++];
                out[outoffset++] = src[srcoffset++];
                out[outoffset++] = src[srcoffset++];
            }
            for(int k = width; k < 0; k++)
                out[outoffset++] = src[srcoffset++];
            outoffset += outheight;
            srcoffset += srcheight;
        }
    }

    /**
     * Gets the width of this sprite.
     * @return The width of this sprite.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of this sprite.
     * @return The height of this sprite.
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param file The image file string to construct the sprite from.
     * @param component The component to bind this sprite to.
     */
    public DirectColorSprite(String file, Component component) {
        try {
            Image image = ImageIO.read(new File(file));
            MediaTracker mediatracker = new MediaTracker(component);
            mediatracker.addImage(image, 0);
            mediatracker.waitForAll();
            width = image.getWidth(component);
            height = image.getHeight(component);
            offsetx = 0;
            offsety = 0;
            pixels = new int[width * height];
            PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);
            pixelgrabber.grabPixels();
        } catch(Exception ex) {
            throw new RuntimeException("Failed to initialize sprite.");
        }
    }
}

