package com.jef.common.utils;

import org.omg.CORBA.ContextList;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * 工具类
 *
 * @author Jef
 * @create 2018/6/11 20:54
 */
public class ContextUtils {

    /**
     * 获取ApplicationContext
     * @return ApplicationContext
     */
    public static ApplicationContext getContext() {
        // 创建 Spring 容器
        ApplicationContext context = new ClassPathXmlApplicationContext("spring/spring-mvc.xml");
        return context;
    }

    /**
     * 获取ApplicationContext
     * @return ApplicationContext
     */
    public static ApplicationContext getContextFromBeansXML() {
       return getContextFromBeansXML("test/axeBeans.xml");
    }
    /**
     * 获取ApplicationContext
     * @return ApplicationContext
     */
    public static ApplicationContext getContextFromBeansXML(String xmlUrl) {
        // 创建 Spring 容器
        ApplicationContext context = new ClassPathXmlApplicationContext(xmlUrl);
        return context;
    }

    /**
     * 获取BeanFactory
     * @return BeanFactory
     */
    public static BeanFactory getBeanFactoryFromBeansXML() {
        // 创建 Spring 容器
        ApplicationContext context = new ClassPathXmlApplicationContext("test/axeBeans.xml");
        BeanFactory factory = context;
        return factory;
    }

    /**
     * 获取BeanFactory
     * @return BeanFactory
     */
    public static BeanFactory getBeanFactoryByFileSystemResource() {
        Resource res = new FileSystemResource("src/main/resources/test/axeBeans.xml");
        BeanFactory factory = new XmlBeanFactory(res);
        return factory;
    }
}
