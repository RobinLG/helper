package com.robin.left.helper.service.impl;

import com.robin.left.helper.common.utils.Base64;
import com.robin.left.helper.service.ImageService;
import com.robin.left.helper.common.utils.Consts;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.robin.left.helper.common.utils.ByteHelper.toByteArray;
import static com.robin.left.helper.common.utils.Consts.*;
import static com.robin.left.helper.common.utils.Msgs.*;

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
    @Value("${images.pathSplit}")
    private String pathSplit;

    @Override
    public Map<String, String> loadImage() {
        // save base64 of image in Map
        Map<String, String> imagesMap = new HashMap<>();
        System.out.println(Thread.currentThread().getContextClassLoader().
                getResource("").getPath() + imagesPath);
        File file = new File(Thread.currentThread().getContextClassLoader().
                getResource("").getPath() + imagesPath);
        Map<String, String> base64 = loadImageFromServer(file, imagesMap);
        return base64;
    }

    @Override
    public Map<String, String> loadImageFromServer(File file, Map<String, String> imagesMap) {
        if (file.exists()) {
            if (file.isFile()) {
                System.out.println(file.getAbsolutePath());
                String[] type = file.getAbsolutePath().split("\\.");
                String[] path = file.getAbsolutePath().split(pathSplit);
                // Prefix base64 according to the image type
                if (type[1].equals(JPG)) {
                    imagesMap.put(path[1], IMAGE_JPG + Base64.getImgSt(file.getAbsolutePath()));
                } else if (type[1].equals(PNG)) {
                    imagesMap.put(path[1], IMAGE_PNG + Base64.getImgSt(file.getAbsolutePath()));
                }

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
    public Map<String, String> saveImage(MultipartFile img, String contentType) {
        String imgUrl = "";
        Map<String, String> uploadResult = new HashMap<>();
        // Determines if the file is empty
        if (!img.isEmpty()) {
            // this object contains the current date value
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat(Consts.DATE_FORMAT_1);
            String dateString = formatter.format(date);
            // Categorizes pictures by date
            File targetImg = new File(Thread.currentThread().getContextClassLoader().getResource("")
                    .getPath() + imagesPath + dateString);
            // Determines if the folder is empty
            if (!targetImg.exists()) {
                // Create folders in cascade
                targetImg.mkdirs();
            }

            imgUrl = Thread.currentThread().getContextClassLoader().getResource("")
                    .getPath() + imagesPath + dateString + "/" + System.currentTimeMillis() + "." + contentType;
            try {
                FileOutputStream outputStream = new FileOutputStream(imgUrl);
                outputStream.write(img.getBytes());
                outputStream.flush();
                outputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                uploadResult.put("state", "0");
                uploadResult.put("errmsg", FILE_IO_ERR);
            } catch (IOException e) {
                e.printStackTrace();
                uploadResult.put("state", "0");
                uploadResult.put("errmsg", FILE_NOT_FOUND);
            }
        }
        uploadResult.put("state", "1");
        uploadResult.put("path", imgUrl);
        return uploadResult;
    }

    @Override
    public byte[] modifyImageMdFive(String filePath) {
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

        return data2;
    }


}
