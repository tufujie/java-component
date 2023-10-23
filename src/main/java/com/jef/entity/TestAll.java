package com.jef.entity;

import org.apache.solr.client.solrj.beans.Field;

import java.util.Date;

/**
 * 测试实体
 *
 * @author Jef
 * @create 2018/6/1 14:56
 */
public class TestAll {
    @Field
    public Long id;
    /**
     * 名称
     */
    @Field
    public String testName;
    /**
     * 电话
     */
    @Field
    public String testPhone;

    /**
     * 创建时间
     */
    @Field
    public Date createTime;

    @Field
    public Object data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestPhone() {
        return testPhone;
    }

    public void setTestPhone(String testPhone) {
        this.testPhone = testPhone;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 测试返回对象
     */
    public TestAll getTestAll(String name) {
        testName = name;
        return this;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
