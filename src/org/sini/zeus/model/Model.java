package org.sini.zeus.model;

import java.nio.ByteBuffer;
import org.sini.zeus.graphics.TriangleRasterizer;
import org.sini.zeus.io.BufferUtils;

/**
 * Model.java
 * @version 1.0.0
 * @author Evelus Development (SiniSoul)
 */
public final class Model {
    
    /**
     * The model triangles.
     */
    private Triangle[] triangles = null;
    
    /**
     * The model vertices.
     */
    private Vertex[] vertices = null;
    
    /**
     * Loads this model from a soft model.
     * @param model The soft model to load this model from.
     */
    public void load(SoftModel model) {
        ByteBuffer buffer0 = ByteBuffer.wrap(model.bytes);
        ByteBuffer buffer1 = buffer0.duplicate();
        ByteBuffer buffer2 = buffer0.duplicate();
        ByteBuffer buffer3 = buffer0.duplicate();
        ByteBuffer buffer4 = buffer0.duplicate();
        triangles = new Triangle[model.triangles];   
        buffer0.position(model.tdrawoffset);
        buffer1.position(model.tposoffset);
        int v0 = 0;
        int v1 = 0;
        int v2 = 0;
        int o = 0;
        for(int i = 0; i < triangles.length; i++) {
            Triangle triangle = triangles[i] = new Triangle(this);
            switch(buffer1.get() & 0xFF) {
                
                case 1:
                    v0 = BufferUtils.getSmartA(buffer0) + o;
                    o = v0;
                    v1 = BufferUtils.getSmartA(buffer0) + o;
                    o = v1;
                    v2 = BufferUtils.getSmartA(buffer0) + o;
                    o = v2;
                    triangle.v0 = v0;
                    triangle.v1 = v1;
                    triangle.v2 = v2;
                    break;
                    
                case 2:
                    v1 = v2;
                    v2 = BufferUtils.getSmartA(buffer0) + o;
                    o = v2;
                    triangle.v0 = v0;
                    triangle.v1 = v1;
                    triangle.v2 = v2;
                    break;
                    
                case 3:
                    v0 = v2;
                    v2 = BufferUtils.getSmartA(buffer0) + o;
                    o = v2;
                    triangle.v0 = v0;
                    triangle.v1 = v1;
                    triangle.v2 = v2;
                    break;
                    
                case 4:
                    int temp = v0;
                    v0 = v1;
                    v1 = temp;
                    v2 = BufferUtils.getSmartA(buffer0) + o;
                    o = v2;
                    triangle.v0 = v0;
                    triangle.v1 = v1;
                    triangle.v2 = v2;
                    break;
            }
        }
        buffer0.position(model.tcoloroffset);
        if(model.topcodeoffset >= 0)
            buffer1.position(model.topcodeoffset);
        if(model.tprioffset >= 0)
            buffer2.position(model.tprioffset);
        if(model.talphaoffset >= 0)
            buffer3.position(model.talphaoffset);
        if(model.tgroupoffset >= 0)
            buffer4.position(model.tgroupoffset);
        for(int i = 0; i < triangles.length; i++) {
            triangles[i].colorindex = buffer0.getShort() & 0xFFFF;
            if(model.topcodeoffset >= 0)
                triangles[i].opcode = buffer1.get() & 0xFF;
            if(model.tprioffset >= 0)
                triangles[i].priority = buffer2.get() & 0xFF;
            if(model.talphaoffset >= 0)
                triangles[i].alpha = buffer3.get() & 0xFF;
            if(model.tgroupoffset >= 0)
                triangles[i].group = buffer4.get() & 0xFF;
        }
        vertices = new Vertex[model.vertices];
        buffer0.position(model.vopcodeoffset);
        buffer1.position(model.vxoffset);
        buffer2.position(model.vyoffset);
        buffer3.position(model.vzoffset);
        if(model.vgroupoffset >= 0)
            buffer4.position(model.vgroupoffset);
        int curx = 0;
        int cury = 0;
        int curz = 0;
        for(int i = 0; i < vertices.length; i++) {
            Vertex vertex = vertices[i] = new Vertex();
            int opcode = buffer0.get() & 0xFF;
            int translatex = 0;
            if((opcode & 1) != 0)
                translatex = BufferUtils.getSmartA(buffer1);
            int translatey = 0;
            if((opcode & 2) != 0)
                translatey = BufferUtils.getSmartA(buffer2);
            int translatez = 0;
            if((opcode & 4) != 0)
                translatez = BufferUtils.getSmartA(buffer3);
            vertex.x = curx + translatex;
            vertex.y = cury + translatey;
            vertex.z = curz + translatez;
            curx = vertices[i].x;
            cury = vertices[i].y;
            curz = vertices[i].z;
            if(model.vgroupoffset >= 0)
                 vertex.group = buffer4.get() & 0xFF;
        }
    }
    
    /**
     * Gets a triangle at a specified triangle array position.
     * @param i The triangle array position.
     * @return The triangle at the specified position.
     */
    public Triangle getTriangle(int i) {
        return triangles[i];
    }
    
    /**
     * Gets a vertex at a specified vertex array position.
     * @param i The vertex array position.
     * @return The vertex at the specified position.
     */
    public Vertex getVertex(int i) {
        return vertices[i];
    }
    
    /**
     * Gets the amount of triangles that this model contains.
     * @return The amount of triangles that this model contains.
     */
    public int getAmountTriangles() {
        return triangles.length;
    }
    
    /**
     * Gets the amount of vertices that this model contains.
     * @return The amount of vertices that this model contains.
     */
    public int getAmountVertices() {
        return vertices.length;
    }
    
    /**
     * @param model The soft model to load this model from.
     */
    public Model(SoftModel model) {
        load(model);
    }
}