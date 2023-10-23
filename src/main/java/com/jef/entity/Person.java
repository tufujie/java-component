package com.jef.entity;

/**
 * 人类
 * @author Jef
 * @date 2020/6/18 0018
 */
public class Person {

    private String id;

    private String name;

    /**
     * 年龄
     */
    private Integer age;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}