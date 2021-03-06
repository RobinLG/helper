package com.robin.life.helper.service.impl;

import com.robin.life.helper.common.utils.Base64Util;
import com.robin.life.helper.dao.ImageDao;
import com.robin.life.helper.service.ImageService;
import com.robin.life.helper.common.utils.Consts;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.robin.life.helper.common.utils.ByteUtil.toByteArray;
import static com.robin.life.helper.common.utils.Consts.*;
import static com.robin.life.helper.common.utils.Msgs.*;

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
    @Autowired
    private ImageDao imageDao;

    @Override
    public Map<String, String> loadImage() {
        // save base64 of image in Map
        Map<String, String> imagesMap = new HashMap<>();
        log.debug("images:" + imagesPath);
        File file = new File(imagesPath);
        Map<String, String> base64 = loadImageFromServer(file, imagesMap);
        return base64;
    }

    @Override
    public Map<String, String> loadImageFromServer(File file, Map<String, String> imagesMap) {
        if (file.exists()) {
            if (file.isFile()) {
                log.debug(file.getAbsolutePath());
                String[] type = file.getAbsolutePath().split("\\.");
                String[] path = file.getAbsolutePath().split(pathSplit);
                // Prefix base64 according to the image type,
                // and key, because http can not transform '/' in URL
                if (type[1].equals(JPG)) {
                    imagesMap.put(Base64.encodeBase64String(path[1].getBytes()), IMAGE_JPG + Base64Util.getImgSt(file.getAbsolutePath()));
                } else if (type[1].equals(PNG)) {
                    imagesMap.put(Base64.encodeBase64String(path[1].getBytes()), IMAGE_PNG + Base64Util.getImgSt(file.getAbsolutePath()));
                }

            } else {
                // When 'file' a folder, the following method
                File[] fileList = file.listFiles();
                if (fileList.length == 0) {
                    log.debug(file.getAbsolutePath() + "is null!");
                } else {
                    for (int i = 0; i < fileList.length; i++) {
                        // recursive function, when 'file' is not file
                        loadImageFromServer(fileList[i], imagesMap);
                    }
                }
            }
        } else {
            log.debug(FILE_NOT_EXIST);
        }
        return imagesMap;
    }

    @Override
    public Map<String, String> saveImage2Server(MultipartFile img, String contentType) {
        String imgUrl = "";
        Map<String, String> uploadResult = new HashMap<>();
        // Determines if the file is empty
        if (!img.isEmpty()) {
            // this object contains the current date value
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat(Consts.DATE_FORMAT_1);
            String dateString = formatter.format(date);
            // Categorizes pictures by date
            File targetImg = new File(imagesPath + dateString);
            // Determines if the folder is empty
            if (!targetImg.exists()) {
                // Create folders in cascade
                targetImg.mkdirs();
            }

            imgUrl = imagesPath + dateString + "/" + System.currentTimeMillis() + "." + contentType;
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
    public Map<String, Object> saveImage2Db(MultipartFile img, String contentType) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        InputStream inputStream = null;
        Map<String, Object> imageMap = new HashMap<>();
        // bytes of img
        try {
            inputStream = img.getInputStream();
            // changes all of images's type to .jpg,
            // because using plugin(thumbnail) to change .png, the size of images will up.
            if (!img.isEmpty() && contentType.equals(IMAGE_PNG)) {
                // read image file
                BufferedImage bufferedImage = ImageIO.read(img.getInputStream());
                // create a blank, RGB, same width and height, and a white
                // background
                BufferedImage newBufferedImage = new BufferedImage(
                        bufferedImage.getWidth(), bufferedImage.getHeight(),
                        BufferedImage.TYPE_INT_RGB);
                // TYPE_INT_RGB:creating a RBG image，24bit depth，changing 32bitmap to 24 bit successfully
                newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0,
                        Color.WHITE, null);
                ImageIO.write(newBufferedImage, "jpg", byteArrayOutputStream);
                inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                inputStream.close();
            }

            // cut the size of image
            BufferedImage bufferedThumbnailImage = Thumbnails.of(inputStream)
            .scale(1f)
            .outputQuality(0.1f).asBufferedImage();
            ImageIO.write(bufferedThumbnailImage, "jpg", byteArrayOutputStream);

            imageMap.put("thumbnail", byteArrayOutputStream.toByteArray());
            imageMap.put("picture", img.getBytes());
            imageMap.put("time", new Date());
            imageDao.insertImage(imageMap);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageMap;
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
        log.debug("pic bytes:" + Arrays.toString(data2));
        log.debug("pic1 md5:" + DigestUtils.md5Hex(data));
        log.debug("pic2 md5:" + DigestUtils.md5Hex(data2));

        return data2;
    }


}
