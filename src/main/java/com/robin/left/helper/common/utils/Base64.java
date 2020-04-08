package com.robin.left.helper.common.utils;

import sun.misc.BASE64Encoder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * base64 util
 * @author Robin
 * @date 2020/4/8
 */
public class Base64 {

    /**
     * transform image to byte[],
     * and transform is to base64
     * @param imgPath
     * @return
     */
    public static String getImgSt(String imgPath) {
        byte[] date = null;

        try {
            InputStream in = new FileInputStream(imgPath);
            date = new byte[in.available()];
            in.read();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BASE64Encoder base64Encoder = new BASE64Encoder();
        return base64Encoder.encode(date);
    }
}
