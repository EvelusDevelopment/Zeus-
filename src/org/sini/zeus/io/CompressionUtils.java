package org.sini.zeus.io;

import org.sini.zeus.cache.bzip2.CBZip2InputStream;
import java.util.zip.GZIPInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Fusion 
 * CompressionUtils.java
 * @version 1.0.0
 * @author SiniSoul (SiniSoul@live.com)
 */
public class CompressionUtils {
    
    /**
     * 
     * @param src
     * @param offset
     * @param length
     * @return 
     */
    public static byte[] ubzip2(byte[] src, int offset, int length) throws IOException {
        /* Construct the bzip2 bytes */
        byte[] bzip2 = new byte[(length + 2) - offset];
        /* Add the bzip2 header */
        bzip2[0] = 'h';
        bzip2[1] = '1';
        /* Copy the source bytes into the bzip2 bytes */
        System.arraycopy(src, offset, bzip2, 2, (length - offset));
        InputStream is = new CBZip2InputStream(new ByteArrayInputStream(bzip2));
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                byte[] buf = new byte[1028];
                int len = 0;
                while ((len = is.read(buf, 0, buf.length)) != -1) {
                    os.write(buf, 0, len);
                }
            } finally {
                os.close();
            }
            return os.toByteArray();
        } finally {
            is.close();
        }
    }

    /**
     * 
     * @param bytes
     * @return
     * @throws IOException 
     */
    public static byte[] ugzip(byte[] bytes) throws IOException {
        InputStream is = new GZIPInputStream(new ByteArrayInputStream(bytes));
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                byte[] buf = new byte[1024];
                int len = 0;
                while ((len = is.read(buf, 0, buf.length)) != -1) {
                    os.write(buf, 0, len);
                }
            } finally {
                os.close();
            }
            return os.toByteArray();
        } finally {
            is.close();
        }
    }
}
