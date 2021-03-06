package com.robin.life.helper.common.utils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.apache.commons.codec.binary.Base64;
/**
 * base64 util
 * @author Robin
 * @date 2020/4/8
 */
public class Base64Util {

    /**
     * transform image to byte[],
     * and transform is to base64
     * @param imgPath
     * @return
     */
    public static String getImgSt(String imgPath) {
        File f = new File(imgPath);
        if (!f.exists()) {
            try {
                throw new FileNotFoundException(imgPath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        FileChannel channel = null;
        FileInputStream fs = null;
        byte[] data = null;

        try {
            fs = new FileInputStream(f);
            channel = fs.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
            while ((channel.read(byteBuffer)) > 0) {
                // do nothing
                // log.debug("reading");
            }
            data = byteBuffer.array();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return Base64.encodeBase64String(data);
    }
}
