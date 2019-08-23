package com.example.arcfaceserver;

import com.example.arcfaceserver.product.dao.FacedbFactory;
import com.example.arcfaceserver.product.beans.Image;
import com.example.arcfaceserver.product.beans.Person;
import com.example.arcfaceserver.util.ArcFaceUtil;
import com.example.arcfaceserver.util.FileUtil;
import com.example.arcfaceserver.util.LogUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RegisterTest {

    @Before
    public void init() {
        System.out.println("test 初始化");
        ArcFaceUtil.getInstance().init("D:\\projects\\idreaProjects\\arcfaceserver\\src\\lib");
    }

    @Test
    public void contextLoads() {
    }

    @Autowired
    FacedbFactory facedbFactory;

    /**
     * 加入测试数据
     */
    //@Test
    public void registerTest() {
        String imgFileDir = "D:\\projects\\idreaProjects\\arcfaceserver\\images\\test";
        File file = new File(imgFileDir);
        if (file.exists()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                // 只处理 jpg和png
                if (!files[i].getName().endsWith(".jpg") && !files[i].getName().endsWith(".png")) {
                    continue;
                }
                File imgFile = files[i];
                byte[] feature = ArcFaceUtil.getInstance().getFeature(imgFile.getAbsolutePath());
                if (feature == null) {
                    LogUtil.D(imgFile.getName() + "提取特征值为空");
                    continue;
                }
                byte[] image = FileUtil.getInstance().readFile(imgFile.getAbsolutePath());
                if (image == null) {
                    LogUtil.D(imgFile.getName() + "读取图片文件为空");
                    continue;
                }
                int groupid = i + 1;
                String vender = "arcsoft";
                String version = "v1.1";
                String appid = "MX001";
                String uname = imgFile.getName().substring(0, imgFile.getName().indexOf("."));
                String uid = uname + "_" + System.currentTimeMillis();
                for (int k = 0; k < 10000; k++) {
                    String suid = uid + "_" + k;
                    String suname = uname + "_" + k;
                    Person person = new Person(groupid, feature, vender, version, appid, suname, suid);
                    Image image1 = new Image(suid, image, null);
                    facedbFactory.register(person, image1);
                }
            }

        }
    }

    /**
     * 加入真实数据
     */
    //@Test
    public void registerReal() {
        String imgFileDir = "D:\\projects\\idreaProjects\\arcfaceserver\\images\\peo";
        File file = new File(imgFileDir);
        if (file.exists()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                // 只处理 jpg和png
                if (!files[i].getName().endsWith(".jpg") && !files[i].getName().endsWith(".png")) {
                    continue;
                }
                File imgFile = files[i];
                byte[] feature = ArcFaceUtil.getInstance().getFeature(imgFile.getAbsolutePath());
                if (feature == null) {
                    LogUtil.D(imgFile.getName() + "提取特征值为空");
                    continue;
                }
                byte[] image = FileUtil.getInstance().readFile(imgFile.getAbsolutePath());
                if (image == null) {
                    LogUtil.D(imgFile.getName() + "读取图片文件为空");
                    continue;
                }
                int groupid = 110;
                String vender = "arcsoft";
                String version = "v1.1";
                String appid = "MX001";
                String uname = imgFile.getName().substring(0, imgFile.getName().indexOf("."));
                String uid = uname + "_" + System.currentTimeMillis();
                Person person = new Person(groupid, feature, vender, version, appid, uname, uid);
                Image image1 = new Image(uid, image, null);
                facedbFactory.register(person, image1);
            }

        }
    }

}
