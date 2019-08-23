package com.example.arcfaceserver.controller;

import com.example.arcfaceserver.common.CompareResult;
import com.example.arcfaceserver.common.SearchFaceWay;
import com.example.arcfaceserver.dao.FaceRepository;
import com.example.arcfaceserver.enity.FaceEntity;
import com.example.arcfaceserver.threadpool.FaceTask;
import com.example.arcfaceserver.threadpool.ThreadManager;
import com.example.arcfaceserver.util.ArcFaceUtil;
import com.example.arcfaceserver.util.FileUtil;
import com.example.arcfaceserver.util.LogUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.rmi.runtime.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@RestController
public class ArcSoftFaceServerController {

    private String testImagePath = "D:\\projects\\idreaProjects\\arcfaceserver\\images\\4.png";
    private String imgFileDir = "D:\\projects\\idreaProjects\\arcfaceserver\\images\\";

    @Autowired
    FaceRepository faceRepository;

    ThreadManager threadManager = new ThreadManager();

    @RequestMapping(value = "/registerface")
    String registerFace(@RequestParam("bytes") MultipartFile bytes, @RequestParam("name") String name, @RequestParam("groupId") int groupId, @RequestParam("featureType") int featureType, @RequestParam("flag") int flag) {
        LogUtil.D("==========================收到registerFace请求==============================flag:" + flag);
        boolean result = false;
        switch (flag) {
            case SearchFaceWay.FLAG_FEATURE:
                result = registerFaceByFeature(bytes, name, groupId, featureType);
                break;
            case SearchFaceWay.FLAG_IMG:
                result = registerFaceByImg(bytes, name);
                break;
        }
        LogUtil.D("==========================返回registerFace请求==============================result:" + result);
        if (result)
            return "1";
        else
            return "0";
    }

    @RequestMapping(value = "/compare")
    String searchFace(@RequestParam("bytes") MultipartFile bytes, @RequestParam("flag") int flag) {
        LogUtil.D("==========================收到searchFace请求==============================flag:" + flag);
        long start = System.currentTimeMillis();
        CompareResult result = new CompareResult();
        switch (flag) {
            case SearchFaceWay.FLAG_FEATURE:
//                result = searchFaceByFeatureSingleThread(bytes);
                result = searchFaceByFeatureMutiThread(bytes);
                break;
            case SearchFaceWay.FLAG_IMG:
                result = searchFaceByImg(bytes);
                break;
        }
        Long end = System.currentTimeMillis();
        LogUtil.D("-----------------------搜索结束，总共用时：" + (end - start) + "毫秒");
        LogUtil.D("==========================返回searchFace请求==============================similar:" + result.getName() + "," + result.getSimilar());
        return new Gson().toJson(result);
    }

    /**
     * 根据图片注册
     *
     * @param bytes
     * @param name
     * @return
     */
    private boolean registerFaceByImg(MultipartFile bytes, String name) {
        if (!bytes.isEmpty()) {
            File saveFile = new File(imgFileDir, File.separator + name + ".jpg");
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(saveFile);
                fos.write(bytes.getBytes());
                fos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 根据特征值注册
     *
     * @param bytes
     * @param name
     * @return
     */
    private boolean registerFaceByFeature(MultipartFile bytes, String name, int groupId, int featureType) {
        try {
            FaceEntity faceEntity = new FaceEntity(groupId, name, bytes.getBytes(), featureType);
            boolean isSaved = faceRepository.save(faceEntity);
            return isSaved;
//            return new FaceEntityDB().save(faceEntity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据特征值查找 单线程遍历
     *
     * @param bytes
     * @return
     */

    private CompareResult searchFaceByFeatureSingleThread(MultipartFile bytes) {
        float maxSimilar = -1.0f;
        String name = "noone";
        CompareResult result = new CompareResult(name, maxSimilar);
        try {
            byte[] uploadFeature = bytes.getBytes();
            List<FaceEntity> faceEntityList = faceRepository.findByGroupId(-1);
            LogUtil.D("找到人脸数量：" + faceEntityList.size());
            for (FaceEntity faceEntity : faceEntityList) {
                float similar = ArcFaceUtil.getInstance().compareFace(uploadFeature, faceEntity.getFeature());
                if (similar > maxSimilar) {
                    maxSimilar = similar;
                    result.setName(faceEntity.getName());
                    result.setSimilar(maxSimilar);
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 根据特征查找  多线程线程池快速
     *
     * @param bytes
     * @return
     */


    private CompareResult searchFaceByFeatureMutiThread(MultipartFile bytes) {
        if (bytes == null) {
            return null;
        }
        try {
            if (bytes.getBytes() == null) {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        CompareResult result = new CompareResult("noone", -1.0f);
        try {
            // 获取所有的group
            List<Integer> groupList = faceRepository.getGroupList();
            LogUtil.D("获取所有的group:" + groupList.toString());
            if (groupList != null) {
                // 为每个group创建任务
                List<FaceTask> taskByGroupList = FaceTask.createTaskByGroupList(groupList, bytes.getBytes(), faceRepository);
                LogUtil.D("创建的任务数量为："+taskByGroupList.size());
                // 执行任务
                List<Future<CompareResult>> futureList = threadManager.excute(taskByGroupList);
                // 等待每个任务执行完成 整合最大的相似度结果
                for (Future<CompareResult> compareResultFuture : futureList) {
                    try {
                        // 每个任务的超时时间5s
                        CompareResult result1 = compareResultFuture.get(5, TimeUnit.SECONDS);
                        if (result1 != null) {
                            if (result1.getSimilar() > result.getSimilar()) {
                                result = result1;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 根据图片文件进行查找
     *
     * @param bytes
     * @return
     */
    private CompareResult searchFaceByImg(MultipartFile bytes) {
        float maxSimilar = -1.0f;
        String name = "noone";
        CompareResult result = new CompareResult(name, maxSimilar);

        String tempFilePath = "tempFace.jpg";
        boolean isSaved = FileUtil.getInstance().saveFile(bytes, tempFilePath);
        if (!isSaved) {
            return result;
        }

        try {
            // 模拟从库中轮训
            File file = new File(imgFileDir);
            if (file.exists()) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    // 只处理 jpg和png
                    if (!files[i].getName().endsWith(".jpg") && !files[i].getName().endsWith(".png")) {
                        continue;
                    }
                    File imgFile = files[i];
                    float similar = ArcFaceUtil.getInstance().compareFace(tempFilePath, imgFile.getAbsolutePath());
                    if (similar > maxSimilar) {
                        maxSimilar = similar;
                        name = imgFile.getName();
                    }
                }
            }
            result.setSimilar(maxSimilar);
            result.setName(name);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
