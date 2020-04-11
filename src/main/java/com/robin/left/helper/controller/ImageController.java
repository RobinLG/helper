package com.robin.left.helper.controller;

import com.robin.left.helper.common.utils.Base64;
import com.robin.left.helper.service.ImageService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import static com.robin.left.helper.common.utils.Consts.JPG;
import static com.robin.left.helper.common.utils.Consts.PNG;
import static com.robin.left.helper.common.utils.Msgs.FILE_TYPE;

@Log4j2
@RestController
public class ImageController {

    @Autowired
    private ImageService imageService;
    @Value("${images.path}")
    private String imagesPath;
    private BASE64Decoder base64Decoder = new BASE64Decoder();

    @PostMapping("/uploadImg")
    public Map<String, String> uploadImg(@RequestParam("file")MultipartFile img) {
        // Gets type of file
        String contentType = img.getContentType();
        log.debug(FILE_TYPE + contentType);
        // Gets original name of file
        String originalFilename = img.getOriginalFilename();
        log.debug(originalFilename);
        // Gets suffix of the image
        String[] filename = originalFilename.split("\\.");
        Map<String, String> result = imageService.saveImage(img, filename[filename.length - 1]);
        return result;
    }

    @GetMapping("/loadImg")
    public Map<String, String> loadImg() {
        log.debug("*******************");
        Map<String, String> imagesMap = imageService.loadImage();
        return imagesMap;
    }

    @GetMapping("/getImgMdFive")
    public String getImgMdFive(@RequestParam("fileName")String filePath, HttpServletRequest request, HttpServletResponse response) {
        log.debug("filePath:" + filePath);
        try {
            // if using linux, the following line of code can be deleted
            // decode base64, get the path of image
            File file = new File(imagesPath + new String(base64Decoder.decodeBuffer(filePath)));
            log.debug(new String(base64Decoder.decodeBuffer(filePath)));
            if (file.exists()) {
                // setting download but not open
                response.setContentType("application/force-download");
                // Prefix base64 according to the image type
                String[] type = file.getAbsolutePath().split("\\.");
                String fileName = "";
                if (type[1].equals(JPG)) {
                    fileName = System.currentTimeMillis() + "." + JPG;
                } else if (type[1].equals(PNG)) {
                    fileName = System.currentTimeMillis() + "." + PNG;
                }
                response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);

                byte[] modified = imageService.modifyImageMdFive(file.getPath());
                try {
                    OutputStream os = response.getOutputStream();
                    os.write(modified);
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
