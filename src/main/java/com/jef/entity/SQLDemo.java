package com.jef.entity;

import java.io.Serializable;

/**
 * SQL示例
 *
 * @author Jef
 * @date 2020/9/29
 */
public class SQLDemo implements Serializable {

    private String id;

    private Integer status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}