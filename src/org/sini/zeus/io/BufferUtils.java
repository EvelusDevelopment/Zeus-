package org.sini.zeus.io;

import java.nio.ByteBuffer;

/**
 * BufferUtils.java
 * @version 1.0.0
 * @author Evelus Development (SiniSoul)
 */
public class BufferUtils {
    
    /**
     * @param buffer
     * @return 
     */
    public static int getTri(ByteBuffer buffer) {
        return ((buffer.get() & 0xFF) << 16) | 
               ((buffer.get() & 0xFF) << 8)  | 
               (buffer.get() & 0xFF);
    }  
    
    /**
     * @param buffer
     * @return 
     */
    public static int getSmartA(ByteBuffer buffer) {
        int i = buffer.get(buffer.position()) & 0xFF;
        if(i < 128)
            return (buffer.get() & 0xFF) - 64;
        else 
            return (buffer.getShort() & 0xFFFF) - 49152;
    }
    
    /**
     * @param buffer
     * @return 
     */
    public static int getSmartB(ByteBuffer buffer) {
        int i = buffer.get(buffer.position()) & 0xFF;
        if(i < 128)
            return (buffer.get() & 0xFF);
        else 
            return (buffer.getShort() & 0xFFFF) - 32768;
    }  
}
