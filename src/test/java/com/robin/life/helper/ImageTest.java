package com.robin.life.helper;

import com.robin.life.helper.dao.ImageDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

}
