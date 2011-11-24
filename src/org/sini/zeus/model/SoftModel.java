package org.sini.zeus.model;

import java.nio.ByteBuffer;

/**
 * SoftModel.java
 * @version 1.0.0
 * @author Evelus Development (SiniSoul)
 */
public class SoftModel {
    
    /**
     * The bytes for this soft model.
     */
    byte[] bytes = null;
    
    /**
     * The amount of vertices for this soft model.
     */
    int vertices = -1;
    
    /**
     * The amount of triangles for this soft model.
     */
    int triangles = -1;
    
    /**
     * The amount of textured triangles for this soft model.
     */
    int texturedtris = -1;
    
    /**
     * The vertex opcode byte array offset.
     */
    int vopcodeoffset = -1;
    
    /**
     * The vertex x coordinate byte array offset.
     */
    int vxoffset = -1;
    
    /**
     * The vertex y coordinate byte array offset.
     */
    int vyoffset = -1;
    
    /**
     * The vertex z coordinate byte array offset.
     */
    int vzoffset = -1;
    
    /**
     * The vertex group byte array offset.
     */
    int vgroupoffset = -1;
    
    /**
     * The triangle data byte array offset.
     */
    int tdataoffset = -1;
    
    /**
     * The triangle opcode byte array offset.
     */
    int topcodeoffset = -1;
    
    /**
     * The triangle color byte array offset.
     */
    int tcoloroffset = -1;
    
    /**
     * The triangle position opcode byte array offset.
     */
    int tposoffset = -1;
    
    /**
     * The triangle draw position byte array offset.
     */
    int tdrawoffset = -1;
    
    /**
     * The triangle priority byte array offset.
     */
    int tprioffset = -1;
    
    /**
     * The triangle alpha byte array offset.
     */
    int talphaoffset = -1;
    
    /**
     * The triangle group byte array offset.
     */
    int tgroupoffset = -1;
    
    /**
     * The texture vertex index offset.
     */
    int ttoffset = -1;
    
    /**
     * Creates a new soft model from header data provided.
     * @param bytes The byte array of initialize with.
     */
    public static SoftModel create(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.position(bytes.length - 18);
        SoftModel model = new SoftModel();
        model.bytes = bytes;
        model.vertices = buffer.getShort();
        model.triangles = buffer.getShort();
        model.texturedtris = buffer.get() & 0xFF;
        boolean topcodes = (buffer.get() & 0xFF) == 1;
        int priorities = buffer.get() & 0xFF;
        boolean alpha = (buffer.get() & 0xFF) == 1;
        boolean tgroups = (buffer.get() & 0xFF) == 1;
        boolean vgroups = (buffer.get() & 0xFF) == 1;
        int vxoffset = buffer.getShort();
        int vyoffset = buffer.getShort();
        int vzoffset = buffer.getShort();
        int tdrawoffset = buffer.getShort();
        int offset = 0;
        model.vopcodeoffset = offset;
        offset += model.vertices;
        model.tposoffset = offset;
        offset += model.triangles;
        model.tprioffset = offset;
        if(priorities == 255)
            offset += model.triangles;
        else
            model.tprioffset = -priorities - 1;
        model.tgroupoffset = offset;
        if(tgroups)
            offset += model.triangles;
        else
            model.tgroupoffset = -1;
        model.topcodeoffset = offset;
        if(topcodes)
            offset += model.triangles;
        else
            model.topcodeoffset = -1;
        model.vgroupoffset = offset;
        if(vgroups)
            offset += model.vertices;
        else
            model.vgroupoffset = -1;
        model.talphaoffset = offset;
        if(alpha)
            offset += model.triangles;
        else
            model.talphaoffset = -1;
        model.tdrawoffset = offset;
        offset += tdrawoffset;
        model.tcoloroffset = offset;
        offset += model.triangles * 2;
        model.ttoffset = offset;
        offset += model.texturedtris * 6;
        model.vxoffset = offset;
        offset += vxoffset;
        model.vyoffset = offset;
        offset += vyoffset;
        model.vzoffset = offset;
        offset += vzoffset;
        return model;
    }
    
    /**
     * Prevent public construction.
     */
    private SoftModel() {}
    
}
