package com.example.arcfaceserver.enity;


import javax.persistence.*;
import java.io.Serializable;

/**
 * 人脸信息类 对应数据库中的表
 */

@Entity
@Table(name = "dayouface")
public class FaceEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    int groupId;
    String name;
    byte[] feature;
    int featureType;

    public FaceEntity() {
    }

    public FaceEntity(int groupId, String name, byte[] feature, int featureType) {
        this.groupId = groupId;
        this.name = name;
        this.feature = feature;
        this.featureType = featureType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getFeature() {
        return feature;
    }

    public void setFeature(byte[] feature) {
        this.feature = feature;
    }

    public int getFeatureType() {
        return featureType;
    }

    public void setFeatureType(int featureType) {
        this.featureType = featureType;
    }
}
