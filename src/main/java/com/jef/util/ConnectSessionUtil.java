package com.jef.util;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

/**
 * 连接数据库
 *
 * @author Jef
 * @create 2018/5/15 19:45
 */
public class ConnectSessionUtil {

    public static SqlSession session = null;

    /**
     * 使用xml获取数据源
     * @return
     */
    public static synchronized SqlSession getSqlSession() {
        if (session != null) {
            return session;
        } else {
            // mybatis的配置文件
            String resource = "mybatis-config.xml";
            // 使用类加载器加载mybatis的配置文件（它也加载关联的映射文件）
            InputStream is = ConnectSessionUtil.class.getClassLoader().getResourceAsStream(resource);
            // 构建sqlSession的工厂
            SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
            // 使用MyBatis提供的Resources类加载mybatis的配置文件（它也加载关联的映射文件）
            // Reader reader = Resources.getResourceAsReader(resource);
            // 构建sqlSession的工厂
            // SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
            // 创建能执行映射文件中sql的sqlSession
            session = sessionFactory.openSession();
            return session;
        }
    }

    /**
     * 对数据库进行新增、修改、删除时均需要commit，不然数据不会做出修改，也可以在openSession()中使用重载的方法，设置自动提交为true，这里为便于测试，采用手动提交的方式
     * @param sqlSession
     */
    public static void commitAndClose(SqlSession sqlSession) {
        sqlSession.commit();
        sqlSession.close();
    }
}
