package com.robin.helper.controller;

import com.robin.helper.service.ImageService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

import static com.robin.helper.common.utils.Msgs.FILE_TYPE;

@Log4j2
@RestController
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/uploadImg")
    public boolean uploadImg(@RequestParam("img")MultipartFile img) {

        // Gets type of file
        String contentType = img.getContentType();
        System.out.println(FILE_TYPE + contentType);
        // Gets original name of file
        String originalFilename = img.getOriginalFilename();
        System.out.println(originalFilename);
        // Gets suffix of the image
        String[] filename = originalFilename.split("\\.");
        imageService.saveImage(img, filename[filename.length - 1]);
        return true;
    }

    @GetMapping("/loadImg")
    public Map<String, String> loadImg() {
        Map<String, String> imagesMap = imageService.loadImage();
        return imagesMap;
    }

    @GetMapping("/getImgMdFive")
    public String getImgMdFive(@RequestParam("filePath")String filePath) {
       return imageService.modifyImageMdFive(filePath);
    }
}
