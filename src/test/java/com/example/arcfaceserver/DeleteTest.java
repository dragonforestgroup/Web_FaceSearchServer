package com.example.arcfaceserver;

import com.example.arcfaceserver.product.dao.FacedbFactory;
import com.example.arcfaceserver.util.ArcFaceUtil;
import com.example.arcfaceserver.util.LogUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeleteTest {

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
     * 删除数据
     */
    @Test
    public void deleteTest() {
        String name = "韩寒";
        boolean isDeleted = facedbFactory.deleteByUname(name);
        if (isDeleted) {
            LogUtil.E("成功删除：" + name);
        }
    }


}
