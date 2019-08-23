package com.example.arcfaceserver.threadpool;

import com.example.arcfaceserver.common.CompareResult;
import com.example.arcfaceserver.dao.FaceRepository;
import com.example.arcfaceserver.enity.FaceEntity;
import com.example.arcfaceserver.util.ArcFaceUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class FaceTask implements Callable<CompareResult> {

    private int groupId = -1;
    private byte[] feature = null;

    FaceRepository faceRepository;

    public FaceTask(int groupId, byte[] feature,FaceRepository faceRepository) {
        this.groupId = groupId;
        this.feature = feature;
        this.faceRepository=faceRepository;
    }

    @Override
    public CompareResult call() throws Exception {
        if (groupId != -1 && feature != null) {
            // 执行查询
            System.out.println("thread:"+Thread.currentThread().getName()+"--正在执行查询----groupId--"+groupId);
            float maxSimilar = -1.0f;
            String name = "noone";
            CompareResult result = new CompareResult(name, maxSimilar);
            try {
                byte[] uploadFeature = feature;
                List<FaceEntity> faceEntityList = faceRepository.findByGroupId(groupId);
                System.out.println("找到人脸数量：" + faceEntityList.size());
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
        return null;
    }

    public static List<FaceTask> createTaskByGroupList(List<Integer> groupIdList, byte[] feature,FaceRepository faceRepository) {
        List<FaceTask> faceTasks = new ArrayList<>();
        for (int i = 0; i < groupIdList.size(); i++) {
            FaceTask faceTask = new FaceTask(groupIdList.get(i), feature,faceRepository);
            faceTasks.add(faceTask);
        }
        return faceTasks;
    }
}
