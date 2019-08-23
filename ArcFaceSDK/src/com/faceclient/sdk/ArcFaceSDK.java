package com.faceclient.sdk;

import com.faceclient.sdk.beans.Result;
import com.faceclient.sdk.beans.Vender;
import com.google.gson.Gson;
import com.faceclient.sdk.util.FileUtil;
import com.faceclient.sdk.util.HttpUtil;

import java.util.HashMap;

public class ArcFaceSDK {
    private static ArcFaceSDK instance = null;

    public static ArcFaceSDK getInstance() {
        if (instance == null)
            instance = new ArcFaceSDK();
        return instance;
    }

    String ip = "127.0.0.1";
    String port = "8084";
    String baseUrl = "http://" + ip + ":" + port + "/arcfaceserver";


    /**
     * 初始化地址 默认使用http
     *
     * @param ip
     * @param port
     */
    public void init(String ip, String port) {
        init(ip, port, false);
    }

    /**
     * 初始化地址 可选择设置http 和 https
     *
     * @param ip
     * @param port
     * @param isUseSSL
     */
    public void init(String ip, String port, boolean isUseSSL) {
        this.ip = ip;
        this.port = port;
        if (isUseSSL) {
            baseUrl = "https://" + ip + ":" + port + "/arcfaceserver";
        } else {
            baseUrl = "http://" + ip + ":" + port + "/arcfaceserver";
        }
    }

    /**
     * 注册人脸
     *
     * @param groupid   组织id
     * @param imagePath 图片路径
     * @param vender    厂商
     * @param version   版本
     * @param appid     应用id
     * @param uname     用户名
     * @return
     */
    public Result registerFace(
            int groupid,
            String imagePath,
            String vender,
            String version,
            String appid,
            String uname) {

        Result result = null;
        String registerUrl = baseUrl + "/register";

        HashMap<String, String> normalParams = new HashMap<>();
        normalParams.put("groupid", groupid + "");
        normalParams.put("vender", vender);
        normalParams.put("version", version);
        normalParams.put("appid", appid);
        normalParams.put("uname", uname);

        HashMap<String, byte[]> fileParams = new HashMap<>();
        fileParams.put("image", FileUtil.file2Bytes(imagePath));

        String resultJson = HttpUtil.upload(registerUrl, normalParams, fileParams);
        result = new Gson().fromJson(resultJson, Result.class);
        return result;
    }

    /**
     * 按照特征搜索人脸
     *
     * @param groupid 组织id
     * @param feature 特征值
     * @param vender  厂商
     * @param version 版本
     * @param appid   应用id
     * @return
     */
    public Result searchFaceByFeature(
            int groupid,
            byte[] feature,
            String vender,
            String version,
            String appid) {
        Result result = null;
        String searchUrl = baseUrl + "/search/feature";

        HashMap<String, String> normalParams = new HashMap<>();
        normalParams.put("groupid", groupid + "");
        normalParams.put("vender", vender);
        normalParams.put("version", version);
        normalParams.put("appid", appid);

        HashMap<String, byte[]> fileParams = new HashMap<>();
        fileParams.put("feature", feature);

        String resultJson = HttpUtil.upload(searchUrl, normalParams, fileParams);
        result = new Gson().fromJson(resultJson, Result.class);
        return result;
    }

    /**
     * 按照图片搜索人脸
     *
     * @param groupid   组织id
     * @param imagePath 图片路径
     * @param vender    厂商
     * @param version   版本
     * @param appid     应用id
     * @return
     */
    public Result searchFaceByImage(
            int groupid,
            String imagePath,
            String vender,
            String version,
            String appid) {
        Result result = null;
        String searchUrl = baseUrl + "/search/image";

        HashMap<String, String> normalParams = new HashMap<>();
        normalParams.put("groupid", groupid + "");
        normalParams.put("vender", vender);
        normalParams.put("version", version);
        normalParams.put("appid", appid);

        HashMap<String, byte[]> fileParams = new HashMap<>();
        fileParams.put("image", FileUtil.file2Bytes(imagePath));

        String resultJson = HttpUtil.upload(searchUrl, normalParams, fileParams);
        result = new Gson().fromJson(resultJson, Result.class);
        return result;
    }

    /**
     * 按照base64编码搜索人脸
     *
     * @param groupid 组织id
     * @param base64  图片base编码
     * @param vender  厂商
     * @param version 版本
     * @param appid   应用id
     * @return
     */
    public Result searchFaceByBase64(
            int groupid,
            String base64,
            String vender,
            String version,
            String appid) {
        Result result = null;
        String searchUrl = baseUrl + "/search/base64";

        HashMap<String, String> normalParams = new HashMap<>();
        normalParams.put("groupid", groupid + "");
        normalParams.put("vender", vender);
        normalParams.put("version", version);
        normalParams.put("appid", appid);
        normalParams.put("base64", base64);

        String resultJson = HttpUtil.upload(searchUrl, normalParams, null);
        result = new Gson().fromJson(resultJson, Result.class);
        return result;
    }


    public static void main(String[] args) {
        String imgPath = "D:\\projects\\idreaProjects\\arcfaceserver\\images\\peo\\韩龙林.jpg";
        Result result = ArcFaceSDK.getInstance().searchFaceByImage(
                1,
                imgPath,
                Vender.ARCSOFT,
                "v1.1",
                "MX001"
        );
        System.out.println(result);
    }
}
