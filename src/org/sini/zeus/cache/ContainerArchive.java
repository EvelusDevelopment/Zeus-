package org.sini.zeus.cache;

import java.io.IOException;
import java.nio.ByteBuffer;
import org.sini.zeus.io.BufferUtils;
import org.sini.zeus.io.CompressionUtils;

/**
 * ContainerArchive.java
 * @version 1.0.0
 * @author Evelus Development (SiniSoul)
 */
public final class ContainerArchive {
    
    /**
     * The bytes for this archive.
     */
    byte[] archivebytes = null;
    
    /**
     * The name hashes for each entry.
     */
    private int[] hashes = null;
    
    /**
     * The uncompressed size of each entry.
     */
    private int[] usizes = null;
    
    /**
     * The compressed size of each entry.
     */
    private int[] csizes = null;
    
    /**
     * The offset position of each entry.
     */
    private int[] ps = null;
    
    /**
     * The amount of entries in this archive.
     */
    private int entries = -1;
    
    /**
     * Option for if this archive is completely compressed or individually compressed.
     */
    private boolean compressed;
    
    /**
     * Initializes this container archive with new data.
     * @param bytes The bytes of data to initialize this archive with.
     */
    public void initialize(byte[] bytes) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        int decompressedsize = BufferUtils.getTri(buffer);
        int compressedsize = BufferUtils.getTri(buffer);
        if(compressedsize != decompressedsize) {
            bytes = CompressionUtils.ubzip2(bytes, 6, bytes.length);
            archivebytes = bytes;
            buffer = ByteBuffer.wrap(bytes);
            compressed = true;
        } else {
            archivebytes = bytes;
            compressed = false;
        }
        entries = buffer.getShort();
        hashes = new int[entries];
        usizes = new int[entries];
        csizes = new int[entries];
        ps = new int[entries];
        int offset = buffer.position() + (entries * 10);
        for(int i = 0; i < entries; i++) {
            hashes[i] = buffer.getInt();
            usizes[i] = BufferUtils.getTri(buffer);
            csizes[i] = BufferUtils.getTri(buffer);
            ps[i] = offset;
            offset += csizes[i];
        }
    }
    /**
     * Gets the bytes for an entry.
     * @param str The name string for the entry.
     * @return The bytes of the entry.
     * @throws IOException An IOException was thrown while getting the entry.
     */
    public byte[] getEntry(String str) throws IOException {
        int hash = 0;
        str = str.toUpperCase();
        for(int j = 0; j < str.length(); j++)
            hash = (hash * 61 + str.charAt(j)) - 32;
        for(int i = 0; i < entries; i++)
            if(hashes[i] == hash) {
                byte[] bytes = new byte[usizes[i]];
                if(!compressed) {
                    bytes = CompressionUtils.ubzip2(archivebytes, ps[i], csizes[i]);
                } else {
                    for(int j = 0; j < usizes[i]; j++)
                        bytes[j] = archivebytes[ps[i] + j];
                }
                return bytes;
            }
        return null;
    }
    
    /**
     * @param bytes The bytes to initialize this archive with.
     */
    public ContainerArchive(byte[] bytes) throws IOException {
        initialize(bytes);
    }   
}
