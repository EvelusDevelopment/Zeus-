package org.sini.zeus.graphics;

/**
 * BasicRasterizer.java
 * @version 1.0.0
 * @author Evelus Development (SiniSoul)
 */
public class BasicRasterizer {

    /**
     * The output for this rasterizer.
     */
    public static int output[];

    /**
     * The output width for this rasterizer.
     */
    public static int outputwidth;

    /**
     * The output height for this rasterizer.
     */
    public static int outputheight;

    /**
     * The current height offset for the output of this rasterizer.
     */
    public static int heightoffset;

    /**
     * The current height for the output of this rasterizer.
     */
    public static int height;

    /**
     * The current width offset for the output of this rasterizer.
     */
    public static int widthoffset;

    /**
     * The current width for the output of this rasterizer.
     */
    public static int width;

    /**
     * FINISH.
     */
    public static int widthoffset$;

    /**
     *
     */
    public static int centerwidth;

    /**
     *
     */
    public static int centerheight;

    public static void setOutput(int outheight, int outwidth, int out[]) {
        output = out;
        outputwidth = outwidth;
        outputheight = outheight;
        setDimensions(outwidth, 0, outheight, 0);
    }

    public static void reset() {
        widthoffset = 0;
        heightoffset = 0;
        width = outputwidth;
        height = outputheight;
        widthoffset$ = width - 1;
        centerwidth = width / 2;
    }

    public static void setDimensions(int w, int woffset, int h, int hoffset) {
        if(woffset < 0)
            woffset = 0;
        if(hoffset < 0)
            hoffset = 0;
        if(w > outputwidth)
            w = outputwidth;
        if(h > outputheight)
            h = outputheight;
        widthoffset = woffset;
        heightoffset = hoffset;
        width = w;
        height = h;
        widthoffset$ = width - 1;
        centerwidth = width / 2;
        centerheight = height / 2;
    }

    public static void resetOutput() {
        int i = outputwidth * outputheight;
        for(int j = 0; j < i; j++)
            output[j] = 0;
    }

    public static void drawQuadrilateralBlend(int x, int y, int w, int h, int alpha, int color)
    {
        if(x < widthoffset)
        {
            w -= widthoffset - x;
            x = widthoffset;
        }
        if(y < heightoffset)
        {
            h -= heightoffset - y;
            y = heightoffset;
        }
        if(x + w > width)
            w = w - x;
        if(y + h > height)
            h = h - y;
        int outalpha = 256 - alpha;
        int red = (color >> 16 & 0xff) * alpha;
        int green = (color >> 8 & 0xff) * alpha;
        int blue = (color & 0xff) * alpha;
        int widthincrement = outputwidth - w;
        int offset = x + y * outputwidth;
        for(int i = 0; i < h; i++)
        {
            for(int j = -w; j < 0; j++)
            {
                int outred = (output[offset] >> 16 & 0xff) * outalpha;
                int outgreen = (output[offset] >> 8 & 0xff) * outalpha;
                int outblue = (output[offset] & 0xff) * outalpha;
                int result = ((red + outred >> 8) << 16) + ((green + outgreen >> 8) << 8) + (blue + outblue >> 8);
                output[offset++] = result;
            }
            offset += widthincrement;
        }
    }

    public static void drawQuadrilateral(int x, int y, int w, int h, int color)
    {
        if(x < widthoffset)
        {
            w -= widthoffset - x;
            x = widthoffset;
        }
        if(y < heightoffset)
        {
            h -= heightoffset - y;
            y = heightoffset;
        }
        if(x + w > width)
            w = width - x;
        if(y + h > height)
            h = height - y;
        int widthincrement = outputwidth - w;
        int offset = x + y * outputwidth;
        for(int i = -h; i < 0; i++)
        {
            for(int j = -w; j < 0; j++)
                output[offset++] = color;
            offset += widthincrement;
        }
    }

    public static void drawQuadrilateralOutline(int x, int y, int width, int height, int color)
    {
        drawHorizontalLine(x, y, width, color);
        drawHorizontalLine(x, (y + height) - 1, width, color);
        drawVerticalLine(x, y, height, color);
        drawVerticalLine((x + width) - 1,y, height, color);
    }

    public static void drawQuadrilateralOutlineBlend(int x,int y, int width, int height, int alpha, int color) {
        drawVerticalLineBlend(x, y, width, alpha, color);
        drawVerticalLineBlend(x, (y + height) - 1, width, alpha, color);
        if(height >= 3) {
            drawHorizontalLineBlend(x, y + 1, height - 2, alpha, color);
            drawHorizontalLineBlend((x + width) - 1, y + 1, height - 2, alpha, color);
        }
    }

    public static void drawHorizontalLine(int x,int y, int length, int color)
    {
        if(y < heightoffset || y >= height)
            return;
        if(x < widthoffset)
        {
            length -= widthoffset - x;
            x = widthoffset;
        }
        if(x + length > width)
            length = width - x;
        int offset = x + y * outputwidth;
        for(int o = 0; o < length; o++)
            output[offset + o] = color;
    }

    public static void drawVerticalLineBlend(int x, int y, int length, int alpha, int color)
    {
        if(y < heightoffset || y >= height)
            return;
        if(x < widthoffset)
        {
            length -= widthoffset - x;
            x = widthoffset;
        }
        if(x + length > width)
            length = width - x;
        int outalpha = 256 - alpha;
        int red = (color >> 16 & 0xff) * alpha;
        int green = (color >> 8 & 0xff) * alpha;
        int blue = (color & 0xff) * alpha;
        int offset = x + y * outputwidth;
        for(int i = 0; i < length; i++) {
            int outred = (output[offset] >> 16 & 0xff) * outalpha;
            int outgreen = (output[offset] >> 8 & 0xff) * outalpha;
            int outblue = (output[offset] & 0xff) * outalpha;
            int result = ((red + outred >> 8) << 16) + ((green + outgreen >> 8) << 8) + (blue + outblue >> 8);
            output[offset++] = result;
        }
    }

    public static void drawVerticalLine(int x,int y, int length, int color)
    {
        if(x < widthoffset || x >= width)
            return;
        if(y < heightoffset)
        {
            length -= heightoffset - y;
            y = heightoffset;
        }
        if(y + length > height)
            length = height - y;
        int offset = x + y * outputwidth;
        for(int o = 0; o < length; o++)
            output[offset + (o * outputwidth)] = color;
    }

    public static void drawHorizontalLineBlend(int x, int y, int length, int alpha, int color)
    {
        if(x < widthoffset || x >= width)
            return;
        if(y < heightoffset)
        {
            length -= heightoffset - y;
            y = heightoffset;
        }
        if(y + length > height)
            length = height - y;
        int outalpha = 256 - alpha;
        int red = (color >> 16 & 0xff) * alpha;
        int green = (color >> 8 & 0xff) * alpha;
        int blue = (color & 0xff) * alpha;
        int offset = x + y * outputwidth;
        for(int j3 = 0; j3 < length; j3++)
        {
            int outred = (output[offset] >> 16 & 0xff) * outalpha;
            int outgreen = (output[offset] >> 8 & 0xff) * outalpha;
            int outblue = (output[offset] & 0xff) * outalpha;
            int result = ((red + outred >> 8) << 16) + ((green + outgreen >> 8) << 8) + (blue + outblue >> 8);
            output[offset] = result;
            offset += outputwidth;
        }
    }
}
