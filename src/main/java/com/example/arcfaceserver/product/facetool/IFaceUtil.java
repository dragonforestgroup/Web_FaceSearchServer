package com.example.arcfaceserver.product.facetool;

public interface IFaceUtil {
    /**
     * 初始化
     */
    void init();

    /**
     * 卸载
     */
    void unInit();

    /**
     * 获取特征码
     * @param imgPath1
     * @return
     */
    byte[] getFeature(String imgPath1);

    /**
     * 比较两个图片的相似度
     *
     * @param imgPath1
     * @param imgPath2
     * @return
     */
    float compareFace(String imgPath1, String imgPath2);

    /**
     * 比较两个特征值相似度
     *
     * @param b1
     * @param b2
     * @return
     */
    float compareFace(byte[] b1, byte[] b2);

    /**
     * 比较特征值和图片的相似度
     *
     * @param imgPath1
     * @param b2
     * @return
     */
    float compareFace(String imgPath1, byte[] b2);
}
