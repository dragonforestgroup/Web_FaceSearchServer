package com.example.arcfaceserver.product.controller;

import com.example.arcfaceserver.product.beans.Result;
import com.example.arcfaceserver.product.beans.Vender;
import com.example.arcfaceserver.product.dao.PersonRepository;
import com.example.arcfaceserver.product.beans.Person;
import com.example.arcfaceserver.product.facetool.FaceUtilFactory;
import com.example.arcfaceserver.product.threadpool.PersonSearchTask;
import com.example.arcfaceserver.product.threadpool.PersonSearchTaskManager;
import com.example.arcfaceserver.util.FileUtil;
import com.example.arcfaceserver.util.LogUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@RestController
public class FaceSearchController {
    @Autowired
    PersonRepository personRepository;

    /**
     * 是否使用多线程
     */
    private boolean isMutiThread = true;

    @RequestMapping(value = "/search/image")
    String searchByImage(
            @RequestParam("groupid") int groupId,
            @RequestParam("image") MultipartFile image,
            @RequestParam("vender") String vender,
            @RequestParam("version") String version,
            @RequestParam("appid") String appid) {
        LogUtil.E("==========================收到searchFace请求==============================");
        long start = System.currentTimeMillis(); // 记录开始时间

        Result result = null;
        try {
            // 检查参数
            if (vender == null || vender.equals("")) {
                result = Result.errorResult("厂商信息不能为空", null);
            } else if (version == null || version.equals("")) {
                result = Result.errorResult("版本信息不能为空", null);
            } else if (appid == null || appid.equals("")) {
                result = Result.errorResult("应用id不能为空", null);
            }
            // 查找
            else {
                if (image != null && image.getBytes() != null) {
                    LogUtil.D("-------------根据图片搜索------------");
                    result = searchByImage(image.getBytes(), groupId, vender, version, appid);
                } else {
                    result = Result.errorResult("图片不能为空", null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.errorResult(e.getMessage(), null);
        }

        long end = System.currentTimeMillis(); // 记录结束时间
        LogUtil.E("==========================消耗时间(ms)==============================" + (end - start));

        LogUtil.E("==========================返回searchFace请求==============================result:" + result);
        // 返回请求
        return new Gson().toJson(result);
    }

    @RequestMapping(value = "/search/feature")
    String searchByFeature(
            @RequestParam("groupid") int groupId,
            @RequestParam("feature") MultipartFile feature,
            @RequestParam("vender") String vender,
            @RequestParam("version") String version,
            @RequestParam("appid") String appid) {
        LogUtil.E("==========================收到searchFace请求==============================");
        long start = System.currentTimeMillis(); // 记录开始时间

        Result result = null;
        try {
            // 检查参数
            if (vender == null || vender.equals("")) {
                result = Result.errorResult("厂商信息不能为空", null);
            } else if (version == null || version.equals("")) {
                result = Result.errorResult("版本信息不能为空", null);
            } else if (appid == null || appid.equals("")) {
                result = Result.errorResult("应用id不能为空", null);
            }
            // 查找
            else {
                if (feature != null && feature.getBytes() != null) {
                    LogUtil.D("-------------根据特征码搜索------------");
                    result = searchByFeature(feature.getBytes(), groupId, vender, version, appid);
                } else {
                    result = Result.errorResult("特征码不能为空", null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.errorResult(e.getMessage(), null);
        }

        long end = System.currentTimeMillis(); // 记录结束时间
        LogUtil.E("==========================消耗时间(ms)==============================" + (end - start));
        LogUtil.E("==========================返回searchFace请求==============================result:" + result);
        // 返回请求
        return new Gson().toJson(result);
    }

    @RequestMapping(value = "/search/base64")
    String searchByBase64(
            @RequestParam("groupid") int groupId,
            @RequestParam("base64") String base64,
            @RequestParam("vender") String vender,
            @RequestParam("version") String version,
            @RequestParam("appid") String appid) {
        LogUtil.E("==========================收到searchFace请求==============================");
        long start = System.currentTimeMillis(); // 记录开始时间

        Result result = null;
        try {
            // 检查参数
            if (vender == null || vender.equals("")) {
                result = Result.errorResult("厂商信息不能为空", null);
            } else if (version == null || version.equals("")) {
                result = Result.errorResult("版本信息不能为空", null);
            } else if (appid == null || appid.equals("")) {
                result = Result.errorResult("应用id不能为空", null);
            }

            // 查找
            else {
                if (base64 != null && base64.getBytes() != null) {
                    LogUtil.D("-------------根据base64编码搜索------------");
                    result = searchByBase64(base64, groupId, vender, version, appid);
                } else {
                    result = Result.errorResult("base64字符串不能为空", null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.errorResult(e.getMessage(), null);
        }
        long end = System.currentTimeMillis(); // 记录结束时间
        LogUtil.E("==========================消耗时间(ms)==============================" + (end - start));
        LogUtil.E("==========================返回searchFace请求==============================result:" + result);
        // 返回请求
        return new Gson().toJson(result);
    }

    /**
     * 按照特征码查找
     *
     * @param feature
     * @param groupId
     * @param vender
     * @param version
     * @param appid
     */
    private Result searchByFeature(byte[] feature, int groupId, String vender, String version, String appid) {
        if (isMutiThread) {
            return searchByFeatureMutiThread(feature, groupId, vender, version, appid);
        } else {
            return searchByFeatureSingleThread(feature, groupId, vender, version, appid);
        }
    }

    /**
     * 按照特征码查找 单线程
     *
     * @param feature
     * @param groupId
     * @param vender
     * @param version
     * @param appid
     */
    private Result searchByFeatureSingleThread(byte[] feature, int groupId, String vender, String version, String appid) {
        Result result = null;
        float maxSimilar = -1f;
        Person findPerson = null;
        List<Person> allPersonList = personRepository.findBy(groupId, vender, version, appid);
        if (allPersonList == null || allPersonList.size() == 0) {
            result = Result.errorResult("没有条件匹配的数据", null);
        } else {
            for (Person person : allPersonList) {
//                float similar = ArcFaceUtil.getInstance().compareFace(feature, person.getFeature());
                float similar = FaceUtilFactory.getFaceUtil(vender).compareFace(feature, person.getFeature());
                if (similar > maxSimilar) {
                    maxSimilar = similar;
                    findPerson = person;
                }
            }
            findPerson.setFeature(null);
            result = Result.successResult(findPerson);
            result.setSimilar(maxSimilar);
        }
        return result;
    }

    /**
     * 按照特征码查找 多线程
     *
     * @param feature
     * @param groupId
     * @param vender
     * @param version
     * @param appid
     */
    private Result searchByFeatureMutiThread(byte[] feature, int groupId, String vender, String version, String appid) {
        Result result = null;
        float maxSimilar = -1.0f;

        if (groupId > -1) {
            // 按照特定groupId搜索
            result = searchByFeatureSingleThread(feature, groupId, vender, version, appid);
        } else {
            // 全量搜索
            List<Integer> groupList = personRepository.getGroupList();
            if (groupList != null) {
                // 为每个group创建任务
                List<PersonSearchTask> taskByGroupList = PersonSearchTask.createTaskByGroupList(groupList, feature, vender, version, appid, personRepository);
                List<Future<Result>> futureList = PersonSearchTaskManager.getInstance().excute(taskByGroupList);
                // 等待每个任务执行完成 整合最大的相似度结果
                for (Future<Result> compareResultFuture : futureList) {
                    try {
                        // 每个任务的超时时间5s
                        Result tempResult = compareResultFuture.get(10, TimeUnit.SECONDS);
                        if (tempResult != null) {
                            if (tempResult.getSimilar() > maxSimilar) {
                                maxSimilar = tempResult.getSimilar();
                                result = tempResult;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        result = Result.errorResult(e.getMessage(), null);
                    }
                }
            }
        }
        return result;
    }

    /**
     * 按照图片文件查找
     *
     * @param image
     * @param groupId
     * @param vender
     * @param version
     * @param appid
     */
    private Result searchByImage(byte[] image, int groupId, String vender, String version, String appid) {
        Result result = null;
        String tempImgFile = "imageTemp.jpg";
        boolean isImgSaved = FileUtil.getInstance().saveFile(image, tempImgFile);
        if (!isImgSaved) {
            result = Result.errorResult("图片保存失败", null);
        } else {
//            byte[] feature = ArcFaceUtil.getInstance().getFeature(tempImgFile);
            byte[] feature = FaceUtilFactory.getFaceUtil(vender).getFeature(tempImgFile);
            if (feature == null) {
                result = Result.errorResult("提取特征码失败", null);
            } else {
                result = searchByFeature(feature, groupId, vender, version, appid);
            }
        }
        return result;
    }

    /**
     * 按照base64格式查找
     *
     * @param base64
     * @param groupId
     * @param vender
     * @param version
     * @param appid
     */
    private Result searchByBase64(String base64, int groupId, String vender, String version, String appid) {
        Result result = null;
        String tempImgFile = "base64Temp.jpg";
        boolean isImgSaved = FileUtil.getInstance().base64ToFile(base64, tempImgFile);
        if (!isImgSaved) {
            result = Result.errorResult("图片保存失败", null);
        } else {
//            byte[] feature = ArcFaceUtil.getInstance().getFeature(tempImgFile);
            byte[] feature = FaceUtilFactory.getFaceUtil(Vender.ARCSOFT).getFeature(tempImgFile);
            if (feature == null) {
                result = Result.errorResult("提取特征码失败", null);
            } else {
                result = searchByFeature(feature, groupId, vender, version, appid);
            }
        }
        return result;
    }
}
