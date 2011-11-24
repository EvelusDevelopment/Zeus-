package org.sini.zeus.model;

import org.sini.zeus.graphics.TriangleRasterizer;

/**
 * Triangle.java
 * @version 1.0.0
 * @author Evelus Development (SiniSoul)
 */
public class Triangle {
    
    /**
     * The drawing opcodes.
     */
    public final static int SHADEDDRAW   = 0, 
                            SOLIDDRAW    = 1,
                            TEXTUREDDRAW = 2;
    
    /**
     * The model that is the parent of this triangle.
     */
    private final Model parent;
    
    /**
     * The vertex points of this triangle.
     */
    int v0 = -1,
        v1 = -1,
        v2 = -1;

    /**
     * The color of this triangle.
     */
    int colorindex = -1;

    /**
     * The drawing opcode of this triangle.
     */
    int opcode = -1;

    /**
     * The priority of this triangle.
     */
    int priority = -1;

    /**
     * The alpha value of this triangle.
     */
    int alpha = -1;

    /**
     * The triangle group that this triangle belongs to.
     */
    int group = -1;
    
    /**
     * Gets the RGB color for this triangle.
     * @return The RGB color at the triangle rasters color table specified index.
     */
    public int getColor() {
        return TriangleRasterizer.COLORTABLE[colorindex];
    }
    
    /**
     * Gets the color index for this triangle.
     * @return The color index for this triangle.
     */
    public int getColorIndex() {
        return colorindex;
    }
    
    /**
     * Gets a specified vertex for this triangle given the vertex number of this triangle.
     * @param n The vertex number of the triangle.
     * @return The vertex of the specified vertex number.
     */
    public Vertex getVertex(int n) {
        return n == 0 ? parent.getVertex(v0) : 
               n == 1 ? parent.getVertex(v1) : 
               n == 2 ? parent.getVertex(v2) : null;
    }
    
    /**
     * @param parent The parent model to this triangle.
     */
    Triangle(Model parent) {
        this.parent = parent;
    }
}
