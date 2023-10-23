package com.jef.constant;

import com.jef.entity.OrderInfo;
import com.jef.entity.User;

import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.List;

/**
 * 基础集合
 * 用于测试
 * @author Jef
 * @date 2019/3/14
 */
public class BasicList {
    /**
     * 获取用户列表
     * @return
     */
    public static List<User> getUserList() {
        List<User> userList = Lists.newArrayList();
        User user2 = new User();
        user2.setId(2L);
        user2.setName(BasicConstant.STR_ONE);
        user2.setPhone(BasicConstant.USER_PHONE);
        user2.setAge(2);
        user2.setType(2);
        userList.add(user2);
        User user4 = new User();
        user4.setId(4L);
        user4.setName(BasicConstant.STR_THREE);
        user4.setPhone(BasicConstant.USER_PHONE);
        user4.setAge(2);
        user4.setType(1);
        userList.add(user4);
        User user1 = new User();
        user1.setId(1L);
        user1.setName(BasicConstant.USER_NAME);
        user1.setPhone(BasicConstant.USER_PHONE);
        user1.setAge(1);
        user1.setType(1);
        user1.setValue(new BigDecimal(1));
        userList.add(user1);
        User user3 = new User();
        user3.setId(3L);
        user3.setName(BasicConstant.STR_TWO);
        user3.setPhone(BasicConstant.USER_PHONE);
        user3.setAge(2);
        user3.setType(2);
        user3.setValue(new BigDecimal(2));
        userList.add(user3);
        return userList;
    }

    /**
     * 获取用户列表，包含空年龄、空名称
     * @return
     */
    public static List<User> getUserListV2() {
        List<User> users = getUserList();
        User user = new User();
        user.setId(3L);
        users.add(user);
        return users;
    }

    /**
     * 获取订单列表
     * @return
     */
    public static List<OrderInfo> getOrderInfoList() {
        List<OrderInfo> orderInfoList = Lists.newArrayList();
        OrderInfo orderInfo1 = new OrderInfo();
        orderInfo1.setId(1L);
        orderInfo1.setTotalPrice(null);
        orderInfo1.setCreatorID(1L);
        orderInfoList.add(orderInfo1);
        OrderInfo orderInfo2 = new OrderInfo();
        orderInfo2.setId(2L);
        orderInfo2.setTotalPrice(new BigDecimal(4));
        orderInfo2.setCreatorID(1L);
        orderInfoList.add(orderInfo2);
        OrderInfo orderInfo3 = new OrderInfo();
        orderInfo3.setId(3L);
        orderInfo3.setTotalPrice(new BigDecimal(6));
        orderInfo3.setCreatorID(2L);
        orderInfoList.add(orderInfo3);
        return orderInfoList;
    }

    /**
     * 获取订单列表
     * @return
     */
    public static List<OrderInfo> getOrderInfoListV2() {
        List<OrderInfo> orderInfoList = Lists.newArrayList();
        OrderInfo orderInfo1 = new OrderInfo();
        orderInfo1.setId(4L);
        orderInfo1.setTotalPrice(null);
        orderInfoList.add(orderInfo1);
        OrderInfo orderInfo2 = new OrderInfo();
        orderInfo2.setId(5L);
        orderInfo2.setTotalPrice(new BigDecimal(4));
        orderInfoList.add(orderInfo2);
        return orderInfoList;
    }
}