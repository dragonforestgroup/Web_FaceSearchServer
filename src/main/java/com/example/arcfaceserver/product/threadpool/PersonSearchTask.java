package com.example.arcfaceserver.product.threadpool;

import com.example.arcfaceserver.product.beans.Result;
import com.example.arcfaceserver.product.dao.PersonRepository;
import com.example.arcfaceserver.product.beans.Person;
import com.example.arcfaceserver.product.facetool.FaceUtilFactory;
import com.example.arcfaceserver.util.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class PersonSearchTask implements Callable<Result> {

    private int groupId = -1;
    private byte[] feature = null;
    private String vender;
    private String version;
    private String appid;

    PersonRepository personRepository;

    public PersonSearchTask(int groupId,
                            byte[] feature,
                            String vender,
                            String version,
                            String appid,
                            PersonRepository personRepository) {
        this.groupId = groupId;
        this.feature = feature;
        this.vender = vender;
        this.version = version;
        this.appid = appid;
        this.personRepository = personRepository;
    }

    @Override
    public Result call() throws Exception {
        Result result = null;
        Person findPerson = null;
        // 检查
        if (groupId == -1) {
            result = Result.errorResult("groupId不能为-1", null);
        } else if (feature == null) {
            result = Result.errorResult("特征码不能为空", null);
        } else if (vender == null || vender.equals("")) {
            result = Result.errorResult("厂商信息不能为空", null);
        } else if (version == null || version.equals("")) {
            result = Result.errorResult("版本信息不能为空", null);
        } else if (appid == null || appid.equals("")) {
            result = Result.errorResult("应用id不能为空", null);
        }
        // 查找
        else {
            // 执行查询
            LogUtil.D("thread:" + Thread.currentThread().getName() + "--正在执行查询----groupId--" + groupId);
            float maxSimilar = -1.0f;
            String name = "noone";
            try {
                byte[] uploadFeature = feature;
                List<Person> personList = personRepository.findBy(groupId, vender, version, appid);
                if (personList == null || personList.size() == 0) {
                    result = Result.errorResult("查询结果为空", null);
                } else {
                    LogUtil.D("找到人脸数量：" + personList.size());
                    for (Person person : personList) {
//                        float similar = ArcFaceUtil.getInstance().compareFace(uploadFeature, person.getFeature());
                        float similar = FaceUtilFactory.getFaceUtil(vender).compareFace(uploadFeature, person.getFeature());
                        if (similar > maxSimilar) {
                            maxSimilar = similar;
                            findPerson = person;
                        }
                    }
                    findPerson.setFeature(null);
                    result = Result.successResult(findPerson);
                    result.setSimilar(maxSimilar);
                }
            } catch (Exception e) {
                e.printStackTrace();
                result = Result.errorResult(e.getMessage(), null);
            }
        }
        return result;
    }

    public static List<PersonSearchTask> createTaskByGroupList(
            List<Integer> groupIdList,
            byte[] feature,
            String vender,
            String version,
            String appid,
            PersonRepository personRepository) {
        List<PersonSearchTask> faceTasks = new ArrayList<>();
        for (int i = 0; i < groupIdList.size(); i++) {
            PersonSearchTask faceTask = new PersonSearchTask(groupIdList.get(i), feature, vender, version, appid, personRepository);
            faceTasks.add(faceTask);
        }
        return faceTasks;
    }
}
