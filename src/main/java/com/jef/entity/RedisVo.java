package com.jef.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Jef
 * @create 2018/7/13 11:18
 */
public class RedisVo implements Serializable {

    private static final long serialVersionUID = -3302246946926982103L;

    private String objectKey;

    private String key;

    private Date createTime;

    public String getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "RedisVo: {objectKey:" + objectKey + ",key:" + key + ",createTime:" + createTime + "}";
    }
}
