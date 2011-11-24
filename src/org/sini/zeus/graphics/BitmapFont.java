package org.sini.zeus.graphics;

import java.nio.ByteBuffer;

/**
 * BitmapFont.java
 * @version 1.0.0
 * @author Evelus Development (SiniSoul)
 */
public class BitmapFont {
    
    /**
     * The maximum amount of characters per font set.
     */
    private static final int MAXCHARS = 256;
     
    /**
     * The character indices where a map of where pixels are to be drawn.
     */
    private byte[][] cindices = null;
    
    /**
     * The translation on the x axis for the font index.
     */
    private int[] transx = null;
    
    /**
     * The translation on the y axis for the font index.
     */
    private int[] transy = null;
    
    /**
     * The character index widths in pixels.
     */
    private int[] indexw = null;
    
    /**
     * The character index widths in pixels.
     */
    private int[] indexh = null;
    
    /**
     * The character widths in pixels for calculating metrics.
     */
    private int[] cwidth = null;
    
    /**
     * The max height difference sampled from all the characters.
     */
    private int maxh = -1;
    
    /**
     * Draws a string's text onto the basic BasicRasterizer.
     * @param s The string to draw.
     * @param x The x coordinate of the string.
     * @param y The y coordinate of the string.
     * @param color The color of the string.
     */
    public void drawString(String s, int x, int y, int color) {
        if(s == null)
            return;
        y -= maxh;
        for(int i1 = 0; i1 < s.length(); i1++) {
            char c = s.charAt(i1);
            if(c != ' ')
                draw(cindices[c], x + transx[c], y + transy[c], indexw[c], indexh[c], color);
            x += cwidth[c];
        }
    }
    
    /**
     * Draws a bitmap index into the basic raster output array.
     * @param index The bitmap index.
     * @param x The x coordinate to draw the index at.
     * @param y The y coordinate to draw the index at.
     * @param w The width of the index.
     * @param h The height of the index.
     * @param color The color to set the pixels as.
     */
    private static void draw(byte index[], int x, int y, int w, int h, int color) {
        int outoffset = x + y * BasicRasterizer.outputwidth;
        int outincr = BasicRasterizer.outputwidth - w;
        int indexincr = 0;
        int indexoffset = 0;
        if(y < BasicRasterizer.heightoffset) {
            int j2 = BasicRasterizer.heightoffset - y;
            h -= j2;
            y = BasicRasterizer.heightoffset;
            indexoffset += j2 * w;
            outoffset += j2 * BasicRasterizer.outputwidth;
        }
        if(y + h >= BasicRasterizer.height)
            h -= ((y + h) - BasicRasterizer.height) + 1;
        if(x < BasicRasterizer.widthoffset) {
            int k2 = BasicRasterizer.widthoffset - x;
            w -= k2;
            x = BasicRasterizer.widthoffset;
            indexoffset += k2;
            outoffset += k2;
            indexincr += k2;
            outincr += k2;
        }
        if(x + w >= BasicRasterizer.width) {
            int l2 = ((x + w) - BasicRasterizer.width) + 1;
            w -= l2;
            indexincr += l2;
            outincr += l2;
        }
        if(w <= 0 || h <= 0)
            return;
        draw(BasicRasterizer.output, index, outoffset, indexoffset, outincr, indexincr, h, w, color);
    }
    
    /**
     * Draws a bitmap onto an output array.
     * @param out The output array to draw into.
     * @param index The bitmap index to use to draw with.
     * @param outoffset The output array offset.
     * @param indexoffset The index array offset.
     * @param outincr The output array increment.
     * @param indexincr The index array increment.
     * @param w The width of the bitmap index.
     * @param h The height of the bitmap index.
     * @param color The color to set the pixels as.
     */
    private static void draw(int out[], byte index[], int outoffset, int indexoffset, int outincr, int indexincr, int w, int h, int color) {
        int l1 = -(h >> 2);
        h = -(h & 3);
        for(int i2 = -w; i2 < 0; i2++) {
            for(int j2 = l1; j2 < 0; j2++) {
                if(index[indexoffset++] != 0)
                    out[outoffset++] = color;
                else
                    outoffset++;
                if(index[indexoffset++] != 0)
                    out[outoffset++] = color;
                else
                    outoffset++;
                if(index[indexoffset++] != 0)
                    out[outoffset++] = color;
                else
                    outoffset++;
                if(index[indexoffset++] != 0)
                    out[outoffset++] = color;
                else
                    outoffset++;
            }
            for(int k2 = h; k2 < 0; k2++)
                if(index[indexoffset++] != 0)
                    out[outoffset++] = color;
                else
                    outoffset++;
            outoffset += outincr;
            indexoffset += indexincr;
        }
    }

    /**
     * @param fbytes The font bytes.
     * @param ibytes The index bytes.
     */
    public BitmapFont(byte[] fbytes, byte[] ibytes) {
        ByteBuffer buffer0 = ByteBuffer.wrap(fbytes);
        ByteBuffer buffer1 = ByteBuffer.wrap(ibytes);
        cindices = new byte[MAXCHARS][];
        indexw = new int[MAXCHARS];
        indexh = new int[MAXCHARS];
        transx = new int[MAXCHARS];
        transy = new int[MAXCHARS];
        cwidth = new int[MAXCHARS];
        buffer1.position(buffer0.getShort() + 4);
        int colors = buffer1.get() & 0xFF;
        if(colors > 0)
            buffer1.position(buffer1.position() + (3 * (colors - 1)));
        for(int l = 0; l < MAXCHARS; l++) {
            transx[l] = buffer1.get() & 0xFF;
            transy[l] = buffer1.get() & 0xFF;
            int w = indexw[l] = buffer1.getShort();
            int h = indexh[l] = buffer1.getShort();
            int o = buffer1.get() & 0xFF;
            int s = w * h;
            cindices[l] = new byte[s];
            if(o == 0) {
                for(int j = 0; j < s; j++)
                    cindices[l][j] = (byte) (buffer0.get() & 0xFF);
            } else if(o == 1) {
                for(int x = 0; x < w; x++) {
                    for(int y = 0; y < h; y++)
                        cindices[l][x + y * w] = (byte) (buffer0.get() & 0xFF);
                }
            }
            if(h > maxh && l < 128)
                maxh = h;
            transx[l] = 1;
            cwidth[l] = w + 2;
            int k2 = 0;
            for(int y = h / 7; y < h; y++)
                k2 += cindices[l][y * w];
            if(k2 <= h / 7) {
                cwidth[l]--;
                transx[l] = 0;
            }
            k2 = 0;
            for(int y = h / 7; y < h; y++)
                k2 += cindices[l][(w - 1) + y * w];
            if(k2 <= h / 7)
                cwidth[l]--;
        }
        cwidth[32] = cwidth[105];
    }
}
