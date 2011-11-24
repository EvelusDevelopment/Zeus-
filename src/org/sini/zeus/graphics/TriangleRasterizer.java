package org.sini.zeus.graphics;

/**
 * TriangleBasicBasicRasterizerizerizer.java
 * @version 1.0.0
 * @author Evelus Development (SiniSoul)
 */
public class TriangleRasterizer {

    /**
     * The BasicBasicRasterizerizerizers sine table.
     */
    public final static int[] SINETABLE;

    /**
     * The BasicBasicRasterizerizerizers cosine table.
     */
    public final static int[] COSINETABLE;
    
    /**
     * RESEARCH NESSECARY.
     */
    public final static int[] GRADIENTTABLE;
    
    /**
     * RESEARCH NESSECARY.
     */
    public final static int[] COLORTABLE;
    
    /**
     * The center width of this BasicBasicRasterizerizerizer.
     */
    static int cwidth = -1;
    
    /**
     * The center height of this BasicBasicRasterizerizerizer.
     */
    static int cheight = -1;
    
    /**
     * The height offsets for the output BasicBasicRasterizerizerizer.
     */
    private static int[] heightoffsets = null;
    
    /**
     * Clips the triangles being drawn to fit within bounds.
     */
    private static boolean clip = false;
    
    /**
     * The current alpha value of this BasicBasicRasterizerizerizer.
     */
    private static int alpha = 256;
    
    /**
     * Sets the dimensions of this BasicBasicRasterizerizerizer to support the basic BasicBasicRasterizerizerizer.
     */
    public static void setDimensions() {
        setDimensions(BasicRasterizer.outputheight, BasicRasterizer.outputwidth);
    }
    
    /**
     * Sets the dimensions of this BasicBasicRasterizerizerizer.
     * @param width The width of this BasicBasicRasterizerizerizer.
     * @param height The height of this BasicBasicRasterizerizerizer.
     */
    public static void setDimensions(int width, int height) {
	heightoffsets = new int[height];
        for(int h = 0; h < height; h++)
            heightoffsets[h] = width * h;
        cwidth = width / 2;
        cheight = height / 2;
    }
    
    /**
     * Sets the color table with a specified brightness, using the HSB model.
     * @param brightness The brightness of the color table to set.
     */
    public static void setColorTable(double brightness) {
        brightness += Math.random() * 0.029999999999999999D - 0.014999999999999999D;
        int j = 0;
        for(int k = 0; k < 512; k++) {
            double d1 = (double)(k / 8) / 64D + 0.0078125D;
            double d2 = (double)(k & 7) / 8D + 0.0625D;
            for(int k1 = 0; k1 < 128; k1++) {
                double d3 = (double)k1 / 128D;
                double d4 = d3;
                double d5 = d3;
                double d6 = d3;
                if(d2 != 0.0D) {
                    double d7;
                    if(d3 < 0.5D)
                        d7 = d3 * (1.0D + d2);
                    else
                        d7 = (d3 + d2) - d3 * d2;
                    double d8 = 2D * d3 - d7;
                    double d9 = d1 + 0.33333333333333331D;
                    if(d9 > 1.0D)
                        d9--;
                    double d10 = d1;
                    double d11 = d1 - 0.33333333333333331D;
                    if(d11 < 0.0D)
                        d11++;
                    if(6D * d9 < 1.0D)
                        d4 = d8 + (d7 - d8) * 6D * d9;
                    else
                    if(2D * d9 < 1.0D)
                        d4 = d7;
                    else
                    if(3D * d9 < 2D)
                        d4 = d8 + (d7 - d8) * (0.66666666666666663D - d9) * 6D;
                    else
                        d4 = d8;
                    if(6D * d10 < 1.0D)
                        d5 = d8 + (d7 - d8) * 6D * d10;
                    else
                    if(2D * d10 < 1.0D)
                        d5 = d7;
                    else
                    if(3D * d10 < 2D)
                        d5 = d8 + (d7 - d8) * (0.66666666666666663D - d10) * 6D;
                    else
                        d5 = d8;
                    if(6D * d11 < 1.0D)
                        d6 = d8 + (d7 - d8) * 6D * d11;
                    else
                    if(2D * d11 < 1.0D)
                        d6 = d7;
                    else
                    if(3D * d11 < 2D)
                        d6 = d8 + (d7 - d8) * (0.66666666666666663D - d11) * 6D;
                    else
                        d6 = d8;
                }
                int l1 = (int)(d4 * 256D);
                int i2 = (int)(d5 * 256D);
                int j2 = (int)(d6 * 256D);
                int k2 = (l1 << 16) + (i2 << 8) + j2;
                k2 = intensify(k2, brightness);
                if(k2 == 0)
                    k2 = 1;
                COLORTABLE[j++] = k2;
            }
        }
    }

    /**
     * PURPOSE KNOWN, DESCRIPTION UNDECIDED.
     * @param color The color to modify.
     * @param intensity The intensity to modify with.
     * @return The intensified color.
     */
    private static int intensify(int color, double intensity) {
        double d1 = (double)(color >> 16) / 256D;
        double d2 = (double)(color >> 8 & 0xff) / 256D;
        double d3 = (double)(color & 0xff) / 256D;
        d1 = Math.pow(d1, intensity);
        d2 = Math.pow(d2, intensity);
        d3 = Math.pow(d3, intensity);
        int j = (int)(d1 * 256D);
        int k = (int)(d2 * 256D);
        int l = (int)(d3 * 256D);
        return (j << 16) + (k << 8) + l;
    }
    
    /**
     * Draws a simple colored triangle onto the BasicRasterizer.
     * @param x0 The x coordinate of point 0.
     * @param x1 The x coordinate of point 1.
     * @param x2 The x coordinate of point 2.
     * @param y0 The y coordinate of point 0.
     * @param y1 The y coordinate of point 1.
     * @param y2 The y coordinate of point 2.
     * @param color The color of the triangle.
     */
    public static void drawSimpleTriangle(int x0, int x1, int x2, int y0, int y1, int y2, int color) {
        int rate0 = 0;
        if(y0 != y1)
            rate0 = (x0 - x1 << 16) / (y0 - y1);
        int rate1 = 0;
        if(y2 != y0)
            rate1 = (x2 - x0 << 16) / (y2 - y0);
        int rate3 = 0;
        if(y2 != y1)
            rate3 = (x1 - x2 << 16) / (y1 - y2);
        if(y1 <= y0 && y1 <= y2) {
            if(y1 >= BasicRasterizer.height)
                return;
            if(y0 > BasicRasterizer.height)
                y0 = BasicRasterizer.height;
            if(y2 > BasicRasterizer.height)
                y2 = BasicRasterizer.height;
            if(y0 < y2) {
                x2 = x1 <<= 16;
                if(y1 < 0) {
                    x2 -= rate3 * y1;
                    x1 -= rate0 * y1;
                    y1 = 0;
                }
                x0 <<= 16;
                if(y0 < 0) {
                    x0 -= rate1 * y0;
                    y0 = 0;
                }
                if(y1 != y0 && rate3 < rate0 || y1 == y0 && rate3 > rate1) {
                    y2 -= y0;
                    y0 -= y1;
                    for(y1 = heightoffsets[y1]; --y0 >= 0; y1 += BasicRasterizer.outputwidth) {
                        drawBasicLine(BasicRasterizer.output, y1, x2 >> 16, x1 >> 16, 0, color);
                        x2 += rate3;
                        x1 += rate0;
                    }
                    while(--y2 >= 0) {
                        drawBasicLine(BasicRasterizer.output, y1, x2 >> 16, x0 >> 16, 0, color);
                        x2 += rate3;
                        x0 += rate1;
                        y1 += BasicRasterizer.outputwidth;
                    }
                    return;
                }
                y2 -= y0;
                y0 -= y1;
                for(y1 = heightoffsets[y1]; --y0 >= 0; y1 += BasicRasterizer.outputwidth)
                {
                    drawBasicLine(BasicRasterizer.output, y1, x1 >> 16, x2 >> 16, 0, color);
                    x2 += rate3;
                    x1 += rate0;
                }

                while(--y2 >= 0) {
                    drawBasicLine(BasicRasterizer.output, y1, x0 >> 16, x2 >> 16, 0, color);
                    x2 += rate3;
                    x0 += rate1;
                    y1 += BasicRasterizer.outputwidth;
                }
                return;
            }
            x0 = x1 <<= 16;
            if(y1 < 0) {
                x0 -= rate3 * y1;
                x1 -= rate0 * y1;
                y1 = 0;
            }
            x2 <<= 16;
            if(y2 < 0)
            {
                x2 -= rate1 * y2;
                y2 = 0;
            }
            if(y1 != y2 && rate3 < rate0 || y1 == y2 && rate1 > rate0)
            {
                y0 -= y2;
                y2 -= y1;
                for(y1 = heightoffsets[y1]; --y2 >= 0; y1 += BasicRasterizer.outputwidth)
                {
                    drawBasicLine(BasicRasterizer.output, y1, x0 >> 16, x1 >> 16, 0, color);
                    x0 += rate3;
                    x1 += rate0;
                }

                while(--y0 >= 0)
                {
                    drawBasicLine(BasicRasterizer.output, y1, x2 >> 16, x1 >> 16, 0, color);
                    x2 += rate1;
                    x1 += rate0;
                    y1 += BasicRasterizer.outputwidth;
                }
                return;
            }
            y0 -= y2;
            y2 -= y1;
            for(y1 = heightoffsets[y1]; --y2 >= 0; y1 += BasicRasterizer.outputwidth)
            {
                drawBasicLine(BasicRasterizer.output, y1, x1 >> 16, x0 >> 16, 0, color);
                x0 += rate3;
                x1 += rate0;
            }

            while(--y0 >= 0)
            {
                drawBasicLine(BasicRasterizer.output, y1, x1 >> 16, x2 >> 16, 0, color);
                x2 += rate1;
                x1 += rate0;
                y1 += BasicRasterizer.outputwidth;
            }
            return;
        }
        if(y0 <= y2) {
            if(y0 >= BasicRasterizer.height)
                return;
            if(y2 > BasicRasterizer.height)
                y2 = BasicRasterizer.height;
            if(y1 > BasicRasterizer.height)
                y1 = BasicRasterizer.height;
            if(y2 < y1) {
                x1 = x0 <<= 16;
                if(y0 < 0) {
                    x1 -= rate0 * y0;
                    x0 -= rate1 * y0;
                    y0 = 0;
                }
                x2 <<= 16;
                if(y2 < 0)
                {
                    x2 -= rate3 * y2;
                    y2 = 0;
                }
                if(y0 != y2 && rate0 < rate1 || y0 == y2 && rate0 > rate3) {
                    y1 -= y2;
                    y2 -= y0;
                    for(y0 = heightoffsets[y0]; --y2 >= 0; y0 += BasicRasterizer.outputwidth) {
                        drawBasicLine(BasicRasterizer.output, y0, x1 >> 16, x0 >> 16, 0, color);
                        x1 += rate0;
                        x0 += rate1;
                    }
                    while(--y1 >= 0) {
                        drawBasicLine(BasicRasterizer.output, y0, x1 >> 16, x2 >> 16, 0, color);
                        x1 += rate0;
                        x2 += rate3;
                        y0 += BasicRasterizer.outputwidth;
                    }
                    return;
                }
                y1 -= y2;
                y2 -= y0;
                for(y0 = heightoffsets[y0]; --y2 >= 0; y0 += BasicRasterizer.outputwidth) {
                    drawBasicLine(BasicRasterizer.output, y0, x0 >> 16, x1 >> 16, 0, color);
                    x1 += rate0;
                    x0 += rate1;
                }
                while(--y1 >= 0) {
                    drawBasicLine(BasicRasterizer.output, y0, x2 >> 16, x1 >> 16, 0, color);
                    x1 += rate0;
                    x2 += rate3;
                    y0 += BasicRasterizer.outputwidth;
                }
                return;
            }
            x2 = x0 <<= 16;
            if(y0 < 0)
            {
                x2 -= rate0 * y0;
                x0 -= rate1 * y0;
                y0 = 0;
            }
            x1 <<= 16;
            if(y1 < 0)
            {
                x1 -= rate3 * y1;
                y1 = 0;
            }
            if(rate0 < rate1) {
                y2 -= y1;
                y1 -= y0;
                for(y0 = heightoffsets[y0]; --y1 >= 0; y0 += BasicRasterizer.outputwidth) {
                    drawBasicLine(BasicRasterizer.output, y0, x2 >> 16, x0 >> 16, 0, color);
                    x2 += rate0;
                    x0 += rate1;
                }
                while(--y2 >= 0) {
                    drawBasicLine(BasicRasterizer.output, y0, x1 >> 16, x0 >> 16, 0, color);
                    x1 += rate3;
                    x0 += rate1;
                    y0 += BasicRasterizer.outputwidth;
                }
                return;
            }
            y2 -= y1;
            y1 -= y0;
            for(y0 = heightoffsets[y0]; --y1 >= 0; y0 += BasicRasterizer.outputwidth)
           {
                drawBasicLine(BasicRasterizer.output, y0, x0 >> 16, x2 >> 16, 0, color);
                x2 += rate0;
                x0 += rate1;
            }

            while(--y2 >= 0)
            {
                drawBasicLine(BasicRasterizer.output, y0, x0 >> 16, x1 >> 16, 0, color);
                x1 += rate3;
                x0 += rate1;
                y0 += BasicRasterizer.outputwidth;
            }
            return;
        }
        if(y2 >= BasicRasterizer.height)
            return;
        if(y1 > BasicRasterizer.height)
            y1 = BasicRasterizer.height;
        if(y0 > BasicRasterizer.height)
            y0 = BasicRasterizer.height;
        if(y1 < y0)
        {
            x0 = x2 <<= 16;
            if(y2 < 0)
            {
                x0 -= rate1 * y2;
                x2 -= rate3 * y2;
                y2 = 0;
            }
            x1 <<= 16;
            if(y1 < 0)
            {
                x1 -= rate0 * y1;
                y1 = 0;
            }
            if(rate1 < rate3)
            {
                y0 -= y1;
                y1 -= y2;
                for(y2 = heightoffsets[y2]; --y1 >= 0; y2 += BasicRasterizer.outputwidth)
                {
                    drawBasicLine(BasicRasterizer.output, y2, x0 >> 16, x2 >> 16, 0, color);
                    x0 += rate1;
                    x2 += rate3;
                }

                while(--y0 >= 0)
                {
                    drawBasicLine(BasicRasterizer.output, y2, x0 >> 16, x1 >> 16, 0, color);
                    x0 += rate1;
                    x1 += rate0;
                    y2 += BasicRasterizer.outputwidth;
                }
                return;
            }
            y0 -= y1;
            y1 -= y2;
            for(y2 = heightoffsets[y2]; --y1 >= 0; y2 += BasicRasterizer.outputwidth)
            {
                drawBasicLine(BasicRasterizer.output, y2, x2 >> 16, x0 >> 16, 0, color);
                x0 += rate1;
                x2 += rate3;
            }

            while(--y0 >= 0)
            {
                drawBasicLine(BasicRasterizer.output, y2, x1 >> 16, x0 >> 16, 0, color);
                x0 += rate1;
                x1 += rate0;
                y2 += BasicRasterizer.outputwidth;
            }
            return;
        }
        x1 = x2 <<= 16;
        if(y2 < 0) {
            x1 -= rate1 * y2;
            x2 -= rate3 * y2;
            y2 = 0;
        }
        x0 <<= 16;
        if(y0 < 0) {
            x0 -= rate0 * y0;
            y0 = 0;
        }
        if(rate1 < rate3) {
            y1 -= y0;
            y0 -= y2;
            for(y2 = heightoffsets[y2]; --y0 >= 0; y2 += BasicRasterizer.outputwidth) {
                drawBasicLine(BasicRasterizer.output, y2, x1 >> 16, x2 >> 16, 0, color);
                x1 += rate1;
                x2 += rate3;
            }
            while(--y1 >= 0) {
                drawBasicLine(BasicRasterizer.output, y2, x0 >> 16, x2 >> 16, 0, color);
                x0 += rate0;
                x2 += rate3;
                y2 += BasicRasterizer.outputwidth;
            }
            return;
        }
        y1 -= y0;
        y0 -= y2;
        for(y2 = heightoffsets[y2]; --y0 >= 0; y2 += BasicRasterizer.outputwidth) {
            drawBasicLine(BasicRasterizer.output, y2, x2 >> 16, x1 >> 16, 0, color);
            x1 += rate1;
            x2 += rate3;
        }
        while(--y1 >= 0) {
            drawBasicLine(BasicRasterizer.output, y2, x2 >> 16, x0 >> 16, 0, color);
            x0 += rate0;
            x2 += rate3;
            y2 += BasicRasterizer.outputwidth;
        }
    }
    
    /**
     * Draws a basic line onto an output array.
     * @param output The output array to draw the line into.
     * @param offset The offset in the output array to draw the line into.
     * @param x The x coordinate on the line.
     * @param xmax The maximum x coordinate on the line.
     * @param loops The amount of loops to draw the line for.
     * @param color The color to draw the line as.
     */
    private static void drawBasicLine(int output[], int offset, int x, int xmax, int loops, int color) {
        if(clip) {
            if(xmax > BasicRasterizer.widthoffset$)
                xmax = BasicRasterizer.widthoffset$;
            if(x < 0)
                x = 0;
        }
        if(x >= xmax)
            return;
        offset += x;
        loops = xmax - x >> 2;
        if(alpha == 0) {
            while(--loops >= 0) {
                output[offset++] = color;
                output[offset++] = color;
                output[offset++] = color;
                output[offset++] = color;
            }
            for(loops = xmax - x & 3; --loops >= 0;)
                output[offset++] = color;
            return;
        }
        int outalpha = alpha;
        int a = 256 - alpha;
        color = ((color & 0xff00ff) * a >> 8 & 0xff00ff) + ((color & 0xff00) * a >> 8 & 0xff00);
        while(--loops >= 0) {
            output[offset++] = color + ((output[offset] & 0xff00ff) * outalpha >> 8 & 0xff00ff) + ((output[offset] & 0xff00) * outalpha >> 8 & 0xff00);
            output[offset++] = color + ((output[offset] & 0xff00ff) * outalpha >> 8 & 0xff00ff) + ((output[offset] & 0xff00) * outalpha >> 8 & 0xff00);
            output[offset++] = color + ((output[offset] & 0xff00ff) * outalpha >> 8 & 0xff00ff) + ((output[offset] & 0xff00) * outalpha >> 8 & 0xff00);
            output[offset++] = color + ((output[offset] & 0xff00ff) * outalpha >> 8 & 0xff00ff) + ((output[offset] & 0xff00) * outalpha >> 8 & 0xff00);
        }
        for(loops = xmax - x & 3; --loops >= 0;)
            output[offset++] = color + ((output[offset] & 0xff00ff) * outalpha >> 8 & 0xff00ff) + ((output[offset] & 0xff00) * outalpha >> 8 & 0xff00);
    }
    
    public static void drawShadedTriangle(int x0, int x1, int x2,int y0, int y1, int y2, int s0, int s1, int s2) {
        int prate0 = 0;
        int srate0 = 0;
        if(y1 != y0)
        {
            prate0 = (x0 - x1 << 16) / (y1 - y0);
            srate0 = (s0 - s1 << 15) / (y1 - y0);
        }
        int prate1 = 0;
        int srate1 = 0;
        if(y2 != y1)
        {
            prate1 = (x2 - x0 << 16) / (y2 - y1);
            srate1 = (s2 - s0 << 15) / (y2 - y1);
        }
        int prate2 = 0;
        int srate2 = 0;
        if(y2 != y0)
        {
            prate2 = (x1 - x2 << 16) / (y0 - y2);
            srate2 = (s1 - s2 << 15) / (y0 - y2);
        }
        if(y0 <= y1 && y0 <= y2)
        {
            if(y0 >= BasicRasterizer.height)
                return;
            if(y1 > BasicRasterizer.height)
                y1 = BasicRasterizer.height;
            if(y2 > BasicRasterizer.height)
                y2 = BasicRasterizer.height;
            if(y1 < y2)
            {
                x2 = x1 <<= 16;
                s2 = s1 <<= 15;
                if(y0 < 0)
                {
                    x2 -= prate2 * y0;
                    x1 -= prate0 * y0;
                    s2 -= srate2 * y0;
                    s1 -= srate0 * y0;
                    y0 = 0;
                }
                x0 <<= 16;
                s0 <<= 15;
                if(y1 < 0)
                {
                    x0 -= prate1 * y1;
                    s0 -= srate1 * y1;
                    y1 = 0;
                }
                if(y0 != y1 && prate2 < prate0 || y0 == y1 && prate2 > prate1)
                {
                    y2 -= y1;
                    y1 -= y0;
                    for(y0 = heightoffsets[y0]; --y1 >= 0; y0 += BasicRasterizer.outputwidth)
                    {
                        drawShadedLine(BasicRasterizer.output, x2 >> 16, x1 >> 16, y0, 0, 0, s2 >> 7, s1 >> 7);
                        x2 += prate2;
                        x1 += prate0;
                        s2 += srate2;
                        s1 += srate0;
                    }

                    while(--y2 >= 0)
                    {
                        drawShadedLine(BasicRasterizer.output, x2 >> 16, x0 >> 16, y0, 0, 0, s2 >> 7, s0 >> 7);
                        x2 += prate2;
                        x0 += prate1;
                        s2 += srate2;
                        s0 += srate1;
                        y0 += BasicRasterizer.outputwidth;
                    }
                    return;
                }
                y2 -= y1;
                y1 -= y0;
                for(y0 = heightoffsets[y0]; --y1 >= 0; y0 += BasicRasterizer.outputwidth)
                {
                    drawShadedLine(BasicRasterizer.output, x1 >> 16, x2 >> 16, y0, 0, 0, s1 >> 7, s2 >> 7);
                    x2 += prate2;
                    x1 += prate0;
                    s2 += srate2;
                    s1 += srate0;
                }

                while(--y2 >= 0)
                {
                    drawShadedLine(BasicRasterizer.output, x0 >> 16, x2 >> 16, y0, 0, 0, s0 >> 7, s2 >> 7);
                    x2 += prate2;
                    x0 += prate1;
                    s2 += srate2;
                    s0 += srate1;
                    y0 += BasicRasterizer.outputwidth;
                }
                return;
            }
            x0 = x1 <<= 16;
            s0 = s1 <<= 15;
            if(y0 < 0)
            {
                x0 -= prate2 * y0;
                x1 -= prate0 * y0;
                s0 -= srate2 * y0;
                s1 -= srate0 * y0;
                y0 = 0;
            }
            x2 <<= 16;
            s2 <<= 15;
            if(y2 < 0)
            {
                x2 -= prate1 * y2;
                s2 -= srate1 * y2;
                y2 = 0;
            }
            if(y0 != y2 && prate2 < prate0 || y0 == y2 && prate1 > prate0)
            {
                y1 -= y2;
                y2 -= y0;
                for(y0 = heightoffsets[y0]; --y2 >= 0; y0 += BasicRasterizer.outputwidth)
                {
                    drawShadedLine(BasicRasterizer.output, x0 >> 16, x1 >> 16, y0, 0, 0, s0 >> 7, s1 >> 7);
                    x0 += prate2;
                    x1 += prate0;
                    s0 += srate2;
                    s1 += srate0;
                }

                while(--y1 >= 0)
                {
                    drawShadedLine(BasicRasterizer.output, x2 >> 16, x1 >> 16, y0, 0, 0, s2 >> 7, s1 >> 7);
                    x2 += prate1;
                    x1 += prate0;
                    s2 += srate1;
                    s1 += srate0;
                    y0 += BasicRasterizer.outputwidth;
                }
                return;
            }
            y1 -= y2;
            y2 -= y0;
            for(y0 = heightoffsets[y0]; --y2 >= 0; y0 += BasicRasterizer.outputwidth)
            {
                drawShadedLine(BasicRasterizer.output, x1 >> 16, x0 >> 16, y0, 0, 0, s1 >> 7, s0 >> 7);
                x0 += prate2;
                x1 += prate0;
                s0 += srate2;
                s1 += srate0;
            }

            while(--y1 >= 0)
            {
                drawShadedLine(BasicRasterizer.output, x1 >> 16, x2 >> 16, y0, 0, 0, s1 >> 7, s2 >> 7);
                x2 += prate1;
                x1 += prate0;
                s2 += srate1;
                s1 += srate0;
                y0 += BasicRasterizer.outputwidth;
            }
            return;
        }
        if(y1 <= y2)
        {
            if(y1 >= BasicRasterizer.height)
                return;
            if(y2 > BasicRasterizer.height)
                y2 = BasicRasterizer.height;
            if(y0 > BasicRasterizer.height)
                y0 = BasicRasterizer.height;
            if(y2 < y0)
            {
                x1 = x0 <<= 16;
                s1 = s0 <<= 15;
                if(y1 < 0)
                {
                    x1 -= prate0 * y1;
                    x0 -= prate1 * y1;
                    s1 -= srate0 * y1;
                    s0 -= srate1 * y1;
                    y1 = 0;
                }
                x2 <<= 16;
                s2 <<= 15;
                if(y2 < 0)
                {
                    x2 -= prate2 * y2;
                    s2 -= srate2 * y2;
                    y2 = 0;
                }
                if(y1 != y2 && prate0 < prate1 || y1 == y2 && prate0 > prate2)
                {
                    y0 -= y2;
                    y2 -= y1;
                    for(y1 = heightoffsets[y1]; --y2 >= 0; y1 += BasicRasterizer.outputwidth)
                    {
                        drawShadedLine(BasicRasterizer.output, x1 >> 16, x0 >> 16, y1, 0, 0, s1 >> 7, s0 >> 7);
                        x1 += prate0;
                        x0 += prate1;
                        s1 += srate0;
                        s0 += srate1;
                    }

                    while(--y0 >= 0)
                    {
                        drawShadedLine(BasicRasterizer.output, x1 >> 16, x2 >> 16, y1, 0, 0, s1 >> 7, s2 >> 7);
                        x1 += prate0;
                        x2 += prate2;
                        s1 += srate0;
                        s2 += srate2;
                        y1 += BasicRasterizer.outputwidth;
                    }
                    return;
                }
                y0 -= y2;
                y2 -= y1;
                for(y1 = heightoffsets[y1]; --y2 >= 0; y1 += BasicRasterizer.outputwidth)
                {
                    drawShadedLine(BasicRasterizer.output, x0 >> 16, x1 >> 16, y1, 0, 0, s0 >> 7, s1 >> 7);
                    x1 += prate0;
                    x0 += prate1;
                    s1 += srate0;
                    s0 += srate1;
                }

                while(--y0 >= 0)
                {
                    drawShadedLine(BasicRasterizer.output, x2 >> 16, x1 >> 16, y1, 0, 0, s2 >> 7, s1 >> 7);
                    x1 += prate0;
                    x2 += prate2;
                    s1 += srate0;
                    s2 += srate2;
                    y1 += BasicRasterizer.outputwidth;
                }
                return;
            }
            x2 = x0 <<= 16;
            s2 = s0 <<= 15;
            if(y1 < 0)
            {
                x2 -= prate0 * y1;
                x0 -= prate1 * y1;
                s2 -= srate0 * y1;
                s0 -= srate1 * y1;
                y1 = 0;
            }
            x1 <<= 16;
            s1 <<= 15;
            if(y0 < 0)
            {
                x1 -= prate2 * y0;
                s1 -= srate2 * y0;
                y0 = 0;
            }
            if(prate0 < prate1)
            {
                y2 -= y0;
                y0 -= y1;
                for(y1 = heightoffsets[y1]; --y0 >= 0; y1 += BasicRasterizer.outputwidth)
                {
                    drawShadedLine(BasicRasterizer.output, x2 >> 16, x0 >> 16, y1, 0, 0, s2 >> 7, s0 >> 7);
                    x2 += prate0;
                    x0 += prate1;
                    s2 += srate0;
                    s0 += srate1;
                }

                while(--y2 >= 0)
                {
                    drawShadedLine(BasicRasterizer.output, x1 >> 16, x0 >> 16, y1, 0, 0, s1 >> 7, s0 >> 7);
                    x1 += prate2;
                    x0 += prate1;
                    s1 += srate2;
                    s0 += srate1;
                    y1 += BasicRasterizer.outputwidth;
                }
                return;
            }
            y2 -= y0;
            y0 -= y1;
            for(y1 = heightoffsets[y1]; --y0 >= 0; y1 += BasicRasterizer.outputwidth)
            {
                drawShadedLine(BasicRasterizer.output, x0 >> 16, x2 >> 16, y1, 0, 0, s0 >> 7, s2 >> 7);
                x2 += prate0;
                x0 += prate1;
                s2 += srate0;
                s0 += srate1;
            }

            while(--y2 >= 0)
            {
                drawShadedLine(BasicRasterizer.output, x0 >> 16, x1 >> 16, y1, 0, 0, s0 >> 7, s1 >> 7);
                x1 += prate2;
                x0 += prate1;
                s1 += srate2;
                s0 += srate1;
                y1 += BasicRasterizer.outputwidth;
            }
            return;
        }
        if(y2 >= BasicRasterizer.height)
            return;
        if(y0 > BasicRasterizer.height)
            y0 = BasicRasterizer.height;
        if(y1 > BasicRasterizer.height)
            y1 = BasicRasterizer.height;
        if(y0 < y1)
        {
            x0 = x2 <<= 16;
            s0 = s2 <<= 15;
            if(y2 < 0)
            {
                x0 -= prate1 * y2;
                x2 -= prate2 * y2;
                s0 -= srate1 * y2;
                s2 -= srate2 * y2;
                y2 = 0;
            }
            x1 <<= 16;
            s1 <<= 15;
            if(y0 < 0)
            {
                x1 -= prate0 * y0;
                s1 -= srate0 * y0;
                y0 = 0;
            }
            if(prate1 < prate2)
            {
                y1 -= y0;
                y0 -= y2;
                for(y2 = heightoffsets[y2]; --y0 >= 0; y2 += BasicRasterizer.outputwidth)
                {
                    drawShadedLine(BasicRasterizer.output, x0 >> 16, x2 >> 16, y2, 0, 0, s0 >> 7, s2 >> 7);
                    x0 += prate1;
                    x2 += prate2;
                    s0 += srate1;
                    s2 += srate2;
                }

                while(--y1 >= 0)
                {
                    drawShadedLine(BasicRasterizer.output, x0 >> 16, x1 >> 16, y2, 0, 0, s0 >> 7, s1 >> 7);
                    x0 += prate1;
                    x1 += prate0;
                    s0 += srate1;
                    s1 += srate0;
                    y2 += BasicRasterizer.outputwidth;
                }
                return;
            }
            y1 -= y0;
            y0 -= y2;
            for(y2 = heightoffsets[y2]; --y0 >= 0; y2 += BasicRasterizer.outputwidth)
            {
                drawShadedLine(BasicRasterizer.output, x2 >> 16, x0 >> 16, y2, 0, 0, s2 >> 7, s0 >> 7);
                x0 += prate1;
                x2 += prate2;
                s0 += srate1;
                s2 += srate2;
            }

            while(--y1 >= 0)
            {
                drawShadedLine(BasicRasterizer.output, x1 >> 16, x0 >> 16, y2, 0, 0, s1 >> 7, s0 >> 7);
                x0 += prate1;
                x1 += prate0;
                s0 += srate1;
                s1 += srate0;
                y2 += BasicRasterizer.outputwidth;
            }
            return;
        }
        x1 = x2 <<= 16;
        s1 = s2 <<= 15;
        if(y2 < 0)
        {
            x1 -= prate1 * y2;
            x2 -= prate2 * y2;
            s1 -= srate1 * y2;
            s2 -= srate2 * y2;
            y2 = 0;
        }
        x0 <<= 16;
        s0 <<= 15;
        if(y1 < 0)
        {
            x0 -= prate0 * y1;
            s0 -= srate0 * y1;
            y1 = 0;
        }
        if(prate1 < prate2)
        {
            y0 -= y1;
            y1 -= y2;
            for(y2 = heightoffsets[y2]; --y1 >= 0; y2 += BasicRasterizer.outputwidth)
            {
                drawShadedLine(BasicRasterizer.output, x1 >> 16, x2 >> 16, y2, 0, 0, s1 >> 7, s2 >> 7);
                x1 += prate1;
                x2 += prate2;
                s1 += srate1;
                s2 += srate2;
            }

            while(--y0 >= 0)
            {
                drawShadedLine(BasicRasterizer.output, x0 >> 16, x2 >> 16, y2, 0, 0, s0 >> 7, s2 >> 7);
                x0 += prate0;
                x2 += prate2;
                s0 += srate0;
                s2 += srate2;
                y2 += BasicRasterizer.outputwidth;
            }
            return;
        }
        y0 -= y1;
        y1 -= y2;
        for(y2 = heightoffsets[y2]; --y1 >= 0; y2 += BasicRasterizer.outputwidth)
        {
            drawShadedLine(BasicRasterizer.output, x2 >> 16, x1 >> 16, y2, 0, 0, s2 >> 7, s1 >> 7);
            x1 += prate1;
            x2 += prate2;
            s1 += srate1;
            s2 += srate2;
        }

        while(--y0 >= 0)
        {
            drawShadedLine(BasicRasterizer.output, x2 >> 16, x0 >> 16, y2, 0, 0, s2 >> 7, s0 >> 7);
            x0 += prate0;
            x2 += prate2;
            s0 += srate0;
            s2 += srate2;
            y2 += BasicRasterizer.outputwidth;
        }
    }

    public static void drawShadedLine(int out[], int x, int xmax, int offset, int loops, int color, int shade, int shademax)
    {       
        if(true) {
            int increment;
            if(clip)
            {
                if(xmax - x > 3)
                    increment = (shademax - shade) / (xmax - x);
                else
                    increment = 0;
                if(xmax > BasicRasterizer.widthoffset$)
                    xmax = BasicRasterizer.widthoffset$;
                if(x < 0)
                {
                    shade -= x * increment;
                    x = 0;
                }
                if(x >= xmax)
                    return;
                offset += x;
                loops = xmax - x >> 2;
                increment <<= 2;
            } else
            {
                if(x >= xmax)
                    return;
                offset += x;
                loops = xmax - x >> 2;
                if(loops > 0)
                    increment = (shademax - shade) * GRADIENTTABLE[loops] >> 15;
                else
                    increment = 0;
            }
            if(alpha == 0)
            {
                while(--loops >= 0)
                {
                    color = COLORTABLE[shade >> 8];
                    shade += increment;
                    out[offset++] = color;
                    out[offset++] = color;
                    out[offset++] = color;
                    out[offset++] = color;
                }
                loops = xmax - x & 3;
                if(loops > 0)
                {
                    color = COLORTABLE[shade >> 8];
                    do
                        out[offset++] = color;
                    while(--loops > 0);
                    return;
                }
            } else
            {
                int outalpha = alpha;
                int a = 256 - alpha;
                while(--loops >= 0)
                {
                    color = COLORTABLE[shade >> 8];
                    shade += increment;
                    color = ((color & 0xff00ff) * a >> 8 & 0xff00ff) + ((color & 0xff00) * a >> 8 & 0xff00);
                    out[offset++] = color + ((out[offset] & 0xff00ff) * outalpha >> 8 & 0xff00ff) + ((out[offset] & 0xff00) * outalpha >> 8 & 0xff00);
                    out[offset++] = color + ((out[offset] & 0xff00ff) * outalpha >> 8 & 0xff00ff) + ((out[offset] & 0xff00) * outalpha >> 8 & 0xff00);
                    out[offset++] = color + ((out[offset] & 0xff00ff) * outalpha >> 8 & 0xff00ff) + ((out[offset] & 0xff00) * outalpha >> 8 & 0xff00);
                    out[offset++] = color + ((out[offset] & 0xff00ff) * outalpha >> 8 & 0xff00ff) + ((out[offset] & 0xff00) * outalpha >> 8 & 0xff00);
                }
                loops = xmax - x & 3;
                if(loops > 0)
                {
                    color = COLORTABLE[shade >> 8];
                    color = ((color & 0xff00ff) * a >> 8 & 0xff00ff) + ((color & 0xff00) * a >> 8 & 0xff00);
                    do
                        out[offset++] = color + ((out[offset] & 0xff00ff) * outalpha >> 8 & 0xff00ff) + ((out[offset] & 0xff00) * outalpha >> 8 & 0xff00);
                    while(--loops > 0);
                }
            }
            return;
        }
        if(x >= xmax)
            return;
        int i2 = (shademax - shade) / (xmax - x);
        if(clip)
        {
            if(xmax > BasicRasterizer.widthoffset$)
                xmax = BasicRasterizer.widthoffset$;
            if(x < 0)
            {
                shade -= x * i2;
                x = 0;
            }
            if(x >= xmax)
                return;
        }
        offset += x;
        loops = xmax - x;
        if(alpha == 0)
        {
            do
            {
                out[offset++] = COLORTABLE[shade >> 8];
                shade += i2;
            } while(--loops > 0);
            return;
        }
        int outalpha = alpha;
        int a = 256 - alpha;
        do
        {
            color = COLORTABLE[shade >> 8];
            shade += i2;
            color = ((color & 0xff00ff) * a >> 8 & 0xff00ff) + ((color & 0xff00) * a >> 8 & 0xff00);
            out[offset++] = color + ((out[offset] & 0xff00ff) * outalpha >> 8 & 0xff00ff) + ((out[offset] & 0xff00) * outalpha >> 8 & 0xff00);
        } while(--loops > 0);
    }
    
    /**
     * Sets if the BasicRasterizer clips the triangles within bounds before it draws.
     * @param bool The clipped boolean value.
     */
    public static void setClipped(boolean bool) {
        clip = bool;
    }
    
    /**
     * Gets the center width of this BasicRasterizer.
     * @return The center width of this BasicRasterizer.
     */
    public static int getCenterWidth() {
        return cwidth;
    }
    
    /**
     * Gets the center height of this BasicRasterizer.
     * @return The center height of this BasicRasterizer.
     */
    public static int getCenterHeight() {
        return cheight;
    } 
    
    /**
     * Sets the draw alpha for when triangles are drawn.
     * @param value The alpha value.
     */
    public static void setAlpha(int value) {
        alpha = value;
    }

    static {
        COLORTABLE = new int[0x10000];
        setColorTable(0.80000000000000004D);
        GRADIENTTABLE = new int[512];
        for(int i = 1; i < 512; i++)
            GRADIENTTABLE[i] = 32768 / i;
        SINETABLE = new int[2048];
        COSINETABLE = new int[2048];
        for(int k = 0; k < 2048; k++) {
            SINETABLE[k]   = (int) (65536D * Math.sin((double)k * 0.0030679614999999999D));
            COSINETABLE[k] = (int) (65536D * Math.cos((double)k * 0.0030679614999999999D));
        }
    }
}
