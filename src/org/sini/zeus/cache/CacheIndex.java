package org.sini.zeus.cache;

import org.sini.zeus.io.BufferUtils;

import java.nio.ByteBuffer;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * CacheIndex.java
 * @version 1.0.0
 * @author Evelus Development (SiniSoul)
 */
public final class CacheIndex {
    
    /**
     * The index number that this cache index represents.
     */
    private int num = -1;
    
    /**
     * The main index random access file.
     */
    private RandomAccessFile mainfile = null;
    
    /**
     * The index file for this cache index.
     */
    private RandomAccessFile indexfile = null;
    
    /**
     * Gets the archive bytes from this cache index.
     * @param n The archive number.
     * @return The archive bytes.
     */
    public byte[] getArchive(int n) throws IOException {
        byte[] bytes = null;
        ByteBuffer buffer = ByteBuffer.allocate(520);
        indexfile.seek(6 * n);
        indexfile.readFully(buffer.array(), 0, 6);
        int archivesize = BufferUtils.getTri(buffer);
        int chunk = BufferUtils.getTri(buffer);
        buffer.position(0);
        if((long) chunk > mainfile.length()/520L || chunk <= 0)
            throw new RuntimeException("COB - Idx: "+num+", Archive: "+n+", Chunk: "+chunk);   
        bytes = new byte[archivesize];
        int offset = 0;
        for(int curchunk = 0; offset < archivesize; curchunk++) {
            mainfile.seek(chunk * 520);
            int length = archivesize - offset;
            if(length > 512)
                length = 512;
            mainfile.readFully(buffer.array(), 0, length + 8);
            int archive = buffer.getShort();
            int chkchunk = buffer.getShort();
            int nxtchunk = BufferUtils.getTri(buffer);
            int chkidx = buffer.get();
            if(archive != n || chkchunk != curchunk || chkidx != num)
                throw new RuntimeException("Invalid Check - "
                                                +(archive == n)+", "
                                                +(chkchunk == curchunk)+", "
                                                +(chkidx == num));   
            if(nxtchunk < 0 || (long) chunk > mainfile.length()/520L)
                throw new RuntimeException("COB - Idx: "+num+", Archive: "+n+", Chunk: "+chunk); 
            for(int i = 0; i < length; i++)
                bytes[offset++] = buffer.array()[8 + i];
            buffer.position(0);
            chunk = nxtchunk;
        }       
        return bytes;
    }
    
    /**
     * @param n The index number that this cache index represents.
     * @param main The main index random access file.
     * @param index The index reference random access file.
     */
    public CacheIndex(int n, RandomAccessFile main, RandomAccessFile index) {
        num = n + 1;
        mainfile = main;
        indexfile = index;     
    }
}
