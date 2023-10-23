package com.jef.entity;

import com.jef.util.DateTimeUtil;

import com.google.common.collect.Lists;
import org.apache.solr.client.solrj.beans.Field;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 用户信息
 *
 * @author Jef
 * @create 2018/5/15 19:18
 */
public class User implements Serializable {

    private static final long serialVersionUID = -8114343202784128116L;
    @Field
    private Long id;
    @Field
    private String name;
    @Field
    private String phone;
    @Field
    private int age;
    @Field
    private int age2;

    private Date createTime;

    private Date auditTime;

    private OrderInfo orderInfo;

    /**
     * int测试
     */
    private int intTest;
    /**
     * BigDecimal测试
     */
    private BigDecimal value;

    /**
     * 类型，进行分类处理
     */
    private Integer type;
    /**
     * is测试
     */
    private Integer isMain;
    /**
     * admin
     */
    private boolean admin;

    List<OrderInfo> orderInfos = Lists.newArrayList();

    public User(Long id) {
        this.id = id;
    }

    public User(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public User(String name, String phone, Integer age, Date createTime) {
        this.name = name;
        this.phone = phone;
        this.age = age;
        this.createTime = createTime;
    }

    public User() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    @Override
    public String toString() {
        return "id=" + this.getId() + "；名称=" + this.getName() + "；年龄=" + this.getAge() + "；电话=" + this.getPhone() + "；类型=" + this.getType() + "；创建时间=" + DateTimeUtil.formatDate(this.getCreateTime())
                + "认证时间=" + DateTimeUtil.formatDate(this.getAuditTime()) + "\n";
    }

    public List<OrderInfo> getOrderInfos() {
        return orderInfos;
    }

    public void setOrderInfos(List<OrderInfo> orderInfos) {
        this.orderInfos = orderInfos;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    public Integer getAge2() {
        return age2;
    }

    public void setAge2(Integer age2) {
        this.age2 = age2;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public int getIntTest() {
        return intTest;
    }

    public void setIntTest(int intTest) {
        this.intTest = intTest;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Integer getIsMain() {
        return isMain;
    }

    public void setIsMain(Integer isMain) {
        this.isMain = isMain;
    }

    public boolean getAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age);
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setAge2(int age2) {
        this.age2 = age2;
    }

}
