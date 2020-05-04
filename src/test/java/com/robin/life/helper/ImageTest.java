package com.robin.life.helper;

import com.robin.life.helper.dao.ImageDao;
import com.robin.life.helper.service.ImageService;
import com.robin.life.helper.service.impl.ImageServiceImpl;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.util.ThumbnailatorUtils;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@SpringBootTest
public class ImageTest {

    @Autowired
    private ImageDao imageDao;

    @Test
    public void testInsertImage() {
        Map<String, Object> imageMap = new HashMap<>();
        imageMap.put("thumbnail", "1");
        imageMap.put("picture", "2");
        imageMap.put("time", new Date());
        imageDao.insertImage(imageMap);
    }

    @Test
    public void testThumbnail() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //BufferedImage aa =
        Thumbnails.of(new File("/Users/robin/Pictures/WechatIMG8.jpeg"))
        .scale(1f)
        .outputQuality(0.5f)
        .toFile("/Users/robin/Pictures/1.png");
        //.asBufferedImage();
        //ImageIO.write(aa, "png", byteArrayOutputStream);
        //log.debug("image byte[]:" + Arrays.toString(byteArrayOutputStream.toByteArray()));
    }

    @Test
    public void testUploadImage() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        File file = new File("/Users/robin/Pictures/2.png");
        // File file = new File("/Users/robin/Pictures/WechatIMG8.jpeg");
        MultipartFile multipartFile = new MockMultipartFile(
                "test.jpg", //file name
                "test.jpg", //originalName
                String.valueOf(ContentType.IMAGE_JPEG), // type of file
                new FileInputStream(file)
        );
        ImageService imageService = new ImageServiceImpl();
        Map<String, Object> result = imageService.saveImage2Db(multipartFile, "data:image/png;base64,");
        imageDao.insertImage(result);

//        byteArrayOutputStream = new ByteArrayOutputStream();
//        objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
//        objectOutputStream.writeObject(result.get("picture"));
//        objectOutputStream.flush();
//
//        File file1 = new File("/Users/robin/Pictures/3.jpg");
//        fileOutputStream = new FileOutputStream(file1);
//        bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
//        bufferedOutputStream.write(byteArrayOutputStream.toByteArray());
//
//        objectOutputStream.close();
//        byteArrayOutputStream.close();
//        fileOutputStream.close();
//        bufferedOutputStream.close();
    }

}
