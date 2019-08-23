package com.example.arcfaceserver.product.controller;

import com.example.arcfaceserver.product.beans.Result;
import com.example.arcfaceserver.product.beans.Vender;
import com.example.arcfaceserver.product.dao.FacedbFactory;
import com.example.arcfaceserver.product.dao.ImageRepository;
import com.example.arcfaceserver.product.dao.PersonRepository;
import com.example.arcfaceserver.product.beans.Image;
import com.example.arcfaceserver.product.beans.Person;
import com.example.arcfaceserver.product.facetool.FaceUtilFactory;
import com.example.arcfaceserver.util.FileUtil;
import com.example.arcfaceserver.util.LogUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FaceRegisterController {

    @Autowired
    PersonRepository personRepository;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    FacedbFactory facedbFactory;

    /**
     * 注册人脸
     *
     * @param groupId
     * @param image
     * @param vender
     * @param version
     * @param appid
     * @param uname
     * @return
     */
    @RequestMapping(value = "/register")
    String registerFace(
            @RequestParam("groupid") int groupId,
            @RequestParam("image") MultipartFile image,
            @RequestParam("vender") String vender,
            @RequestParam("version") String version,
            @RequestParam("appid") String appid,
            @RequestParam("uname") String uname) {
        LogUtil.D("==========================收到registerFace请求==============================");
        Result result = null;

        try {
            // 检查
            if (groupId < 0) {
                result = Result.errorResult("groupid必须大于0", null);
            } else if (image == null || image.getBytes() == null) {
                result = Result.errorResult("图片不能为空", null);
            } else if (vender == null || vender.equals("")) {
                result = Result.errorResult("厂商信息不能为空", null);
            } else if (version == null || version.equals("")) {
                result = Result.errorResult("版本信息不能为空", null);
            } else if (appid == null || appid.equals("")) {
                result = Result.errorResult("应用id不能为空", null);
            }
            // 进入注册
            else {
                // 提取特征
                String tempImgPath = "temp.jpg";
                boolean isFileSaved = FileUtil.getInstance().saveFile(image, tempImgPath);
                if (!isFileSaved) {
                    result = Result.errorResult("图片保存失败", null);
                } else {
//                    byte[] feature = ArcFaceUtil.getInstance().getFeature(tempImgPath);
                    byte[] feature = FaceUtilFactory.getFaceUtil(Vender.ARCSOFT).getFeature(tempImgPath);

                    if (feature != null) {
                        // 插入数据库
                        String uid = uname + "_" + System.currentTimeMillis();
                        Person person = new Person(groupId, feature, vender, version, appid, uname, uid);
                        Image image1 = new Image(uid, image.getBytes(), null);
                        boolean isRegistered = facedbFactory.register(person, image1);
                        if (isRegistered) {
                            Person returnPerson = new Person(groupId, null, vender, version, appid, uname, uid);
                            result = Result.successResult(returnPerson);
                        } else {
                            result = Result.errorResult("注册信息失败", null);
                        }
                    } else {
                        result = Result.errorResult("特征码提取失败", null);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.errorResult(e.getMessage(), null);
        }
        LogUtil.E("==========================返回registerFace请求==============================result:" + result);

        // 返回请求
        return new Gson().toJson(result);
    }
}
