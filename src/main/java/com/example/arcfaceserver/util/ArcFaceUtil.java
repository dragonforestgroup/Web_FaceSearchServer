package com.example.arcfaceserver.util;

import com.arcsoft.face.*;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.DetectOrient;
import com.arcsoft.face.enums.ErrorInfo;
import com.arcsoft.face.enums.ImageFormat;
import com.arcsoft.face.toolkit.ImageInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.arcsoft.face.toolkit.ImageFactory.getRGBData;

public class ArcFaceUtil {

    String path = "D:\\projects\\idreaProjects\\arcfaceserver\\src\\lib";

    String appId = "CgmQozskVZzcQ6CjHYH3gydyeQZpTd5v73z5dpQhirwE";
    String sdkKey = "Bk5xPWwoegot2NtZzxfGMKJNeHDmkVF4ScKxDCyiARYY";

    FaceEngine faceEngine;

    private static ArcFaceUtil instance;

    private void ArcSoftSDK2() {

    }

    public static ArcFaceUtil getInstance() {
        if (instance == null) {
            instance = new ArcFaceUtil();
        }
        return instance;
    }


    // 初始化加载引擎
    public void init(String path) {
        faceEngine = new FaceEngine(path);
        //激活引擎
        int activeCode = faceEngine.activeOnline(appId, sdkKey);
        if (activeCode != ErrorInfo.MOK.getValue() && activeCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
            System.out.println("引擎激活失败");
        }

        //引擎配置
        EngineConfiguration engineConfiguration = new EngineConfiguration();
        engineConfiguration.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);
        engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_0_ONLY);

        //功能配置
        FunctionConfiguration functionConfiguration = new FunctionConfiguration();
        functionConfiguration.setSupportAge(true);
        functionConfiguration.setSupportFace3dAngle(true);
        functionConfiguration.setSupportFaceDetect(true);
        functionConfiguration.setSupportFaceRecognition(true);
        functionConfiguration.setSupportGender(true);
        functionConfiguration.setSupportLiveness(true);
        functionConfiguration.setSupportIRLiveness(true);
        engineConfiguration.setFunctionConfiguration(functionConfiguration);


        //初始化引擎
        int initCode = faceEngine.init(engineConfiguration);

        if (initCode != ErrorInfo.MOK.getValue()) {
            System.out.println("初始化引擎失败");
        }
    }

    // 卸载引擎
    public void unInit() {
        if (faceEngine != null) {
            faceEngine.unInit();
        }
    }

    /**
     * 获取图片特征
     *
     * @param imgPath1
     * @return
     */
    public byte[] getFeature(String imgPath1) {
        //人脸检测
        ImageInfo imageInfo = getRGBData(new File(imgPath1));
        List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
        int detectCode = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList);
        System.out.println(faceInfoList);

        FaceFeature faceFeature = null;
        if (faceInfoList.size() > 0) {
            //特征提取
            faceFeature = new FaceFeature();
            int extractCode = faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList.get(0), faceFeature);
            System.out.println("特征值大小：" + faceFeature.getFeatureData().length);
            return faceFeature.getFeatureData();
        } else {
            System.out.println("图片1未识别出人脸");
            return null;
        }
    }

    /**
     * 两个图片比较
     *
     * @param imgPath1
     * @param imgPath2
     * @return
     */
    public float compareFace(String imgPath1, String imgPath2) {
        //人脸检测
        ImageInfo imageInfo = getRGBData(new File(imgPath1));
        List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
        int detectCode = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList);
        System.out.println(faceInfoList);

        FaceFeature faceFeature;
        if (faceInfoList.size() > 0) {
            //特征提取
            faceFeature = new FaceFeature();
            int extractCode = faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList.get(0), faceFeature);
            System.out.println("特征值大小：" + faceFeature.getFeatureData().length);
        } else {
            System.out.println("图片1未识别出人脸");
            return -1.0f;
        }

        //人脸检测2
        ImageInfo imageInfo2 = getRGBData(new File(imgPath2));
        List<FaceInfo> faceInfoList2 = new ArrayList<FaceInfo>();
        int detectCode2 = faceEngine.detectFaces(imageInfo2.getImageData(), imageInfo2.getWidth(), imageInfo2.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList2);
        System.out.println(faceInfoList);

        FaceFeature faceFeature2;
        if (faceInfoList2.size() > 0) {
            //特征提取2
            faceFeature2 = new FaceFeature();
            int extractCode2 = faceEngine.extractFaceFeature(imageInfo2.getImageData(), imageInfo2.getWidth(), imageInfo2.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList2.get(0), faceFeature2);
            System.out.println("特征值大小：" + faceFeature2.getFeatureData().length);
        } else {
            System.out.println("图片2未识别出人脸");
            return -1.0f;
        }
        //特征比对
        FaceFeature targetFaceFeature = new FaceFeature();
        targetFaceFeature.setFeatureData(faceFeature.getFeatureData());
        FaceFeature sourceFaceFeature = new FaceFeature();
        sourceFaceFeature.setFeatureData(faceFeature2.getFeatureData());
        FaceSimilar faceSimilar = new FaceSimilar();
        int compareCode = faceEngine.compareFaceFeature(targetFaceFeature, sourceFaceFeature, faceSimilar);
        System.out.println("相似度：" + faceSimilar.getScore());
        return faceSimilar.getScore();
    }

    /**
     * 两个特征值比较
     *
     * @param b1
     * @param b2
     * @return
     */
    public float compareFace(byte[] b1, byte[] b2) {
        //特征比对
        FaceFeature targetFaceFeature = new FaceFeature();
        targetFaceFeature.setFeatureData(b1);
        FaceFeature sourceFaceFeature = new FaceFeature();
        sourceFaceFeature.setFeatureData(b2);
        FaceSimilar faceSimilar = new FaceSimilar();
        int compareCode = faceEngine.compareFaceFeature(targetFaceFeature, sourceFaceFeature, faceSimilar);
        System.out.println("相似度：" + faceSimilar.getScore());
        return faceSimilar.getScore();
    }

    /**
     * 图片和特征值比较
     *
     * @param imgPath1
     * @param b2
     * @return
     */
    public float compareFace(String imgPath1, byte[] b2) {
        //人脸检测
        ImageInfo imageInfo = getRGBData(new File(imgPath1));
        List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
        int detectCode = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList);
        System.out.println(faceInfoList);
        FaceFeature faceFeature = null;
        if (faceInfoList.size() > 0) {
            //特征提取
            faceFeature = new FaceFeature();
            int extractCode = faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList.get(0), faceFeature);
            System.out.println("特征值大小：" + faceFeature.getFeatureData().length);
        } else {
            System.out.println("图片1未识别出人脸");
            return -1.0f;
        }

        //特征比对
        FaceFeature targetFaceFeature = new FaceFeature();
        targetFaceFeature.setFeatureData(faceFeature.getFeatureData());
        FaceFeature sourceFaceFeature = new FaceFeature();
        sourceFaceFeature.setFeatureData(b2);
        FaceSimilar faceSimilar = new FaceSimilar();
        int compareCode = faceEngine.compareFaceFeature(targetFaceFeature, sourceFaceFeature, faceSimilar);
        System.out.println("相似度：" + faceSimilar.getScore());
        return faceSimilar.getScore();
    }


}
