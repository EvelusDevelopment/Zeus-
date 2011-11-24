package org.sini.zeus.graphics;

import java.awt.image.DirectColorModel;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.ColorModel;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;

/**
 * GraphicsFetcher.java
 * @version 1.0.0
 * @author Evelus Development (SiniSoul)
 */
public final class GraphicsFetcher implements ImageProducer, ImageObserver {

    /**
     * Get the color model for the image fetcher.
     */
    private static ColorModel COLORMODEL = new DirectColorModel(32, 0xff0000, 0x00ff00, 0x0000ff);

    /**
     * The image to fetch.
     */
    private Image image = null;

    /**
     * The pixels for this image fetcher.
     */
    private int[] pixels = null;

    /**
     * The width of this image.
     */
    private int width = -1;

    /**
     * The height of this image.
     */
    private int height = -1;

    /**
     * The consumer of this image.
     */
    private ImageConsumer consumer = null;

    /**
     * Sets this image fetcher to be drawn on the rasterizer.
     */
    public void setBasicRasterizer() {
        BasicRasterizer.setOutput(height, width, pixels);
    }

    /**
     * Adds a image consumer to this fetcher.
     * @param imageconsumer The image consumer to add.
     */
    public void addConsumer(ImageConsumer imageconsumer) {
        consumer = imageconsumer;
        imageconsumer.setDimensions(width, height);
        imageconsumer.setColorModel(COLORMODEL);
        imageconsumer.setHints(14);
    }

    /**
     * Checks to see if it is a valid image consumer.
     * @param imageconsumer The image consumer to check.
     * @return If it is the image consumer for this fetcher.
     */
    public boolean isConsumer(ImageConsumer imageconsumer) {
        return consumer == imageconsumer;
    }

    /**
     * Removes a consumer from this image fetcher.
     * @param ic The image consumer to remove.
     */
    public void removeConsumer(ImageConsumer ic) {
        if(isConsumer(ic))
            consumer = null;
    }

    /**
     * Starts production of this image.
     * @param ic The image consumer to start production with.
     */
    public void startProduction(ImageConsumer ic) {
        addConsumer(ic);
    }

    /**
     * @param ic The image consumer.
     */
    public void requestTopDownLeftRightResend(ImageConsumer ic) {
        throw new UnsupportedOperationException("TDLR");
    }

    /**
     * Called when this image is updated.
     * @param img The image that was updated.
     * @param infoflags The info flags for what was updated.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param width The width of the image.
     * @param height The height of the image.
     * @return
     */
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        return true;
    }

    /**
     * Updates the graphics with this image.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param graphics The graphics to update.
     */
    public void updateGraphics(int x,int y, Graphics graphics) {
        finalizeImage();
        graphics.drawImage(image, x, y, this);
    }

    /**
     * Finalizes the image.
     */
    public synchronized void finalizeImage() {
        if(consumer == null)
            return;
        consumer.setPixels(0, 0, width, height, COLORMODEL, pixels, 0, width);
        consumer.imageComplete(2);
    }

    /**
     * @param component The component to bind this image fetcher to.
     */
    public GraphicsFetcher(int width, int height, Component component) {
        image = component.createImage(this);
        pixels = new int[width * height];
        this.width = width;
        this.height = height;
        component.prepareImage(image, this);
        finalizeImage();
        setBasicRasterizer();
    }
}

