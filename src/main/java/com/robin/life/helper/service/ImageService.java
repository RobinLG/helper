package com.robin.life.helper.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * picture processing
 * @author Robin
 * @date 2020/4/8
 */
public interface ImageService {

    /**
     * load images from server
     * @return base64 of images
     */
    Map<String, String> loadImage();

    /**
     * return base64 of image
     * @param file
     * @return base64 of image
     */
    Map<String, String> loadImageFromServer(File file, Map<String, String> imagesMap);

    /**
     * Saving image to server
     * @param img
     * @param contentType
     * @return result
     */
    Map<String, String> saveImage(MultipartFile img, String contentType);

    /**
     * Modifying md5 of the pic which user selected.
     *
     * @param filePath
     * @return returning the result
     */
    byte[] modifyImageMdFive(String filePath);
}
