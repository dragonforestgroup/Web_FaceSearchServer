package com.example.arcfaceserver.util;

import net.coobird.thumbnailator.Thumbnails;

import java.io.*;

public class ImageUtil {
    /**
     * 最大图片大小 单位是KB
     */
    public static final int MAX_IMAGE_SIZE=512;

    public static boolean compress(byte[] bytes, String dstPath, int width, int height) {
        try {
            InputStream ins=new ByteArrayInputStream(bytes);
            Thumbnails.of(ins)
                    .size(width, height)
                    .outputFormat("jpg")
                    .toFile(new File(dstPath));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


}
