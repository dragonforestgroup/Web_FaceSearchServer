package com.example.arcfaceserver.product.facetool;

import com.example.arcfaceserver.product.beans.Vender;

import java.util.HashMap;

/**
 * 人脸识别工具类产生工厂
 */
public class FaceUtilFactory {

    /**
     * 保存每个厂家的示例  享元模式
     */
    private static HashMap<String, IFaceUtil> faceUtilHashMap = new HashMap<>();

    /**
     * 产生对应厂商的工具实例
     *
     * @param vender
     * @return
     */
    public static IFaceUtil getFaceUtil(String vender) {
        // 从内存中获取
        if (faceUtilHashMap.get(vender) != null) {
            return faceUtilHashMap.get(vender);
        }
        // 创建
        IFaceUtil faceUtil = null;
        if (vender.equals(Vender.ARCSOFT)) {
            faceUtil = new ArcFaceUtil();
            faceUtilHashMap.put(vender, faceUtil);
        } else if (vender.equals(Vender.BAIDU)) {
            faceUtil = getDefault();
            faceUtilHashMap.put(vender, faceUtil);
        } else if (vender.equals(Vender.ALIBABA)) {
            faceUtil = getDefault();
            faceUtilHashMap.put(vender, faceUtil);
        } else if (vender.equals(Vender.TENCENT)) {
            faceUtil = getDefault();
            faceUtilHashMap.put(vender, faceUtil);
        } else if (vender.equals(Vender.MEGVII)) {
            faceUtil = getDefault();
            faceUtilHashMap.put(vender, faceUtil);
        }else if (vender.equals(Vender.SENSETIME)) {
            faceUtil = getDefault();
            faceUtilHashMap.put(vender, faceUtil);
        } else {
            // 未知厂商  默认先使用虹软 且不保存到内存
            faceUtil = new ArcFaceUtil();
        }
        return faceUtil;
    }

    /**
     * 在没有厂商的情况下默认返回
     *
     * @return
     */
    private static IFaceUtil getDefault() {
        return new ArcFaceUtil();
    }
}
