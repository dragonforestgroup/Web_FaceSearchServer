package com.example.arcfaceserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
public class ArcfaceserverApplication {

	@RequestMapping("error")
	String index(){
		return "hello boy";
	}

	public static void main(String[] args) {
		initArcFace();
		SpringApplication.run(ArcfaceserverApplication.class, args);
	}

	private static void initArcFace() {
//		ArcFaceUtil.getInstance().init("D:\\projects\\idreaProjects\\arcfaceserver\\src\\lib");
//		FaceUtilFactory.getFaceUtil(Vender.ARCSOFT).init();
		System.out.println("虹软引擎初始化完成");
	}
}
