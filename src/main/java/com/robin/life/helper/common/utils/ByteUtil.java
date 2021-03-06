package com.robin.life.helper.common.utils;

import java.io.*;

/**
 * byte util
 * @author Robin
 * @date 2020/4/8
 */
public class ByteUtil {

    /**
     * transform inputStream to byte[]
     * @param in
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(InputStream in) throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }
}
