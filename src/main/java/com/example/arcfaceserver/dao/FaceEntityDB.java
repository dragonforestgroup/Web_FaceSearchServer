package com.example.arcfaceserver.dao;

import com.example.arcfaceserver.enity.FaceEntity;
import com.example.arcfaceserver.util.ArcFaceUtil;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

public class FaceEntityDB {

    private Connection connection;
    private PreparedStatement pt;

    private boolean connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/faceDB?useSSL=false&serverTimezone=UTC", "root", "longlin1234");
            if (connection != null)
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void disConnect() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public boolean save(FaceEntity faceInfo) {
        if (!connect()) {
            System.out.println("连接失败");
            return false;
        }
        String sql = "insert into dayouface(groupid,name,feature,featuretype) values (?,?,?,?);";
        try {
            pt = connection.prepareStatement(sql);
            pt.setInt(1, faceInfo.getGroupId());
            pt.setString(2, faceInfo.getName());
//            pt.setBlob(3, new ByteArrayInputStream(faceInfo.getFeature()));
            pt.setBytes(3, faceInfo.getFeature());
            pt.setInt(4, faceInfo.getFeatureType());
            pt.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }
        return false;
    }

    public FaceEntity queryAndCompeleteFace(int groupId, byte[] feature) {
        FaceEntity faceEntity = new FaceEntity();
        if (!connect()) {
            System.out.println("连接失败");
            return null;
        }
        String sql = "select * from dayouface where groupid=" + groupId;
        try {
            float maxSimilar=-1.0f;
            ResultSet resultSet = pt.executeQuery(sql);
            while (resultSet.next()) {
                int searchedGroupId = resultSet.getInt("groupid");
                String name = resultSet.getString("name");
                InputStream ins = resultSet.getBinaryStream("feature");
                byte[] feature1 = new byte[1032];
                try {
                    ins.read(feature1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int featureType = resultSet.getInt("featureType");

                float similar = ArcFaceUtil.getInstance().compareFace(feature1, feature);
                if(similar>maxSimilar){
                    maxSimilar=similar;
                    faceEntity.setName(name);
                    faceEntity.setGroupId(groupId);
                    faceEntity.setFeatureType(featureType);
                    faceEntity.setFeature(feature1);
                }
            }
            return faceEntity;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }
        return faceEntity;
    }


//    @Resource
//    static JdbcTemplate jdbcTemplate;
//
//    public static void save(FaceEntity faceEntity){
//        if (faceEntity != null) {
//            String sql="insert into dayouface(groupid,name,feature,featuretype) values " + "(" +
//                    faceEntity.getGroupId() + "," +
//                    faceEntity.getName() + "," +
//                    faceEntity.getFeature() + "," +
//                    faceEntity.getFeatureType() + ")";
//            jdbcTemplate.execute(sql);
//        }
//    }
}
