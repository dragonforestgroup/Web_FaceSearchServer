package com.example.arcfaceserver.product.facetool;

/**
 * Baidu人脸识别工具类 待实现
 */
public class BaiduFaceUtil implements IFaceUtil{
    @Override
    public void init() {

    }

    @Override
    public void unInit() {

    }

    @Override
    public byte[] getFeature(String imgPath1) {
        return new byte[0];
    }

    @Override
    public float compareFace(String imgPath1, String imgPath2) {
        return 0;
    }

    @Override
    public float compareFace(byte[] b1, byte[] b2) {
        return 0;
    }

    @Override
    public float compareFace(String imgPath1, byte[] b2) {
        return 0;
    }
}
