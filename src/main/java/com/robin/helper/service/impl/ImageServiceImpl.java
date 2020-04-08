package com.robin.helper.service.impl;

import com.robin.helper.common.utils.Base64;
import com.robin.helper.service.ImageService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.robin.helper.common.utils.ByteHelper.toByteArray;
import static com.robin.helper.common.utils.Consts.DATE_FORMAT_1;
import static com.robin.helper.common.utils.Msgs.FILE_NOT_EXIST;

/**
 * picture processing
 * @author Robin
 * @date 2020/4/8
 */
@Log4j2
@Service
public class ImageServiceImpl implements ImageService {

    @Value("${images.path}")
    private String imagesPath;

    @Override
    public Map<String, String> loadImage() {
        // save base64 of image in Map
        Map<String, String> imagesMap = new HashMap<>();
        File file = new File(imagesPath);
        Map<String, String> base64 = loadImageFromServer(file, imagesMap);
        return base64;
    }

    @Override
    public Map<String, String> loadImageFromServer(File file, Map<String, String> imagesMap) {
        if (file.exists()) {
            if (file.isFile()) {
                imagesMap.put(file.getAbsolutePath(), Base64.getImgSt(file.getAbsolutePath()));
            } else {
                // When 'file' a folder, the following method
                File[] fileList = file.listFiles();
                if (fileList.length == 0) {
                    System.out.println(file.getAbsolutePath() + "is null!");
                } else {
                    for (int i = 0; i < fileList.length; i++) {
                        // recursive function, when 'file' is not file
                        loadImageFromServer(fileList[i], imagesMap);
                    }
                }
            }
        } else {
            System.out.println(FILE_NOT_EXIST);
        }
        return imagesMap;
    }

    @Override
    public void saveImage(MultipartFile img, String contentType) {
        // Determines if the file is empty
        if (!img.isEmpty()) {
            // this object contains the current date value
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_1);
            String dateString = formatter.format(date);
            // Categorizes pictures by date
            File targetImg = new File(imagesPath + "\\" + dateString);
            // Determines if the folder is empty
            if (!targetImg.exists()) {
                // Create folders in cascade
                targetImg.mkdirs();
            }
            try {
                FileOutputStream outputStream = new FileOutputStream(imagesPath + "\\" + dateString + "\\" + System.currentTimeMillis() + "." + contentType);
                outputStream.write(img.getBytes());
                outputStream.flush();
                outputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String modifyImageMdFive(String filePath) {
        byte[] data = new byte[0];
        try {
            FileInputStream in = new FileInputStream(filePath);
            data = toByteArray(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // add two more element into byte[] of image,
        // people look at these two images and they are same,
        // but actually they are not the same.
        byte[] data2 = new byte[data.length + 2];
        for (int i = 0; i < data.length; i++) {
            data2[i] = data[i];
        }
        Random r = new Random();
        data2[data.length] = (byte)r.nextInt(101);
        data2[data.length + 1] = (byte)r.nextInt(101);
        System.out.println("pic bytes:" + Arrays.toString(data2));
        System.out.println("pic1 md5:" + DigestUtils.md5Hex(data));
        System.out.println("pic2 md5:" + DigestUtils.md5Hex(data2));

        BASE64Encoder base64Encoder = new BASE64Encoder();
        return base64Encoder.encode(data2);
    }


}
