package com.example.arcfaceserver;

import com.example.arcfaceserver.dao.FaceRepository;
import com.example.arcfaceserver.enity.FaceEntity;
import com.example.arcfaceserver.util.ArcFaceUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ArcfaceserverApplicationTests {

	@Before
	public void init(){
		System.out.println("test 初始化");
		ArcFaceUtil.getInstance().init("D:\\projects\\idreaProjects\\arcfaceserver\\src\\lib");
	}

	@Test
	public void contextLoads() {
	}

	@Autowired
	FaceRepository faceRepository;

	/**
	 * 批量注册
	 */
//	@Test
	public void registerAll(){
		String imgFileDir="D:\\projects\\idreaProjects\\arcfaceserver\\images\\peo";
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
				FaceEntity faceEntity=new FaceEntity(123,imgFile.getName(),feature,1);
				faceRepository.save(faceEntity);
			}
		}
	}


	/**
	 * 批量注册
	 */
	//@Test
	public void regisgterTestCase(){
		String imgFileDir="D:\\projects\\idreaProjects\\arcfaceserver\\images\\test";
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

				for(int k=0;k<1000;k++){
					FaceEntity faceEntity12=new FaceEntity(12,imgFile.getName()+"_test_"+k,feature,1);
					FaceEntity faceEntity13=new FaceEntity(13,imgFile.getName()+"_test_"+k,feature,1);
					FaceEntity faceEntity14=new FaceEntity(14,imgFile.getName()+"_test_"+k,feature,1);
					FaceEntity faceEntity15=new FaceEntity(15,imgFile.getName()+"_test_"+k,feature,1);
					FaceEntity faceEntity16=new FaceEntity(16,imgFile.getName()+"_test_"+k,feature,1);
					FaceEntity faceEntity17=new FaceEntity(17,imgFile.getName()+"_test_"+k,feature,1);
					FaceEntity faceEntity18=new FaceEntity(18,imgFile.getName()+"_test_"+k,feature,1);
					FaceEntity faceEntity19=new FaceEntity(19,imgFile.getName()+"_test_"+k,feature,1);
					FaceEntity faceEntity20=new FaceEntity(20,imgFile.getName()+"_test_"+k,feature,1);


					faceRepository.save(faceEntity12);
					faceRepository.save(faceEntity13);
					faceRepository.save(faceEntity14);
					faceRepository.save(faceEntity15);
					faceRepository.save(faceEntity16);
					faceRepository.save(faceEntity17);
					faceRepository.save(faceEntity18);
					faceRepository.save(faceEntity19);
					faceRepository.save(faceEntity20);
				}
			}
		}
	}


}
