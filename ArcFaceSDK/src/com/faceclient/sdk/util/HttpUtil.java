package com.faceclient.sdk.util;

import okhttp3.*;

import java.util.HashMap;
import java.util.Set;

public class HttpUtil {

    /**
     * 上传文件和参数
     *
     * @param url
     * @param normalParams 普通参数
     * @param fileParams   文件类型参数
     * @return
     */
    public static String upload(String url, HashMap<String, String> normalParams, HashMap<String, byte[]> fileParams) {
        OkHttpClient okHttpClient = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (normalParams != null) {
            Set<String> normalKeys = normalParams.keySet();
            for (String key : normalKeys) {
                builder.addFormDataPart(key, normalParams.get(key));
            }
        }
        if (fileParams != null) {
            Set<String> fileKeys = fileParams.keySet();
            for (String key : fileKeys) {
                builder.addFormDataPart(key, "", RequestBody.create(MediaType.parse("mutipart/form-data"), fileParams.get(key)));
            }
        }
        MultipartBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseJson = response.body().string();
                return responseJson;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
