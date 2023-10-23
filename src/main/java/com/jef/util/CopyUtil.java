package com.jef.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

/**
 * 拷贝
 * 一般情况下，不使用赋值语句的比较容易实现深拷贝，使用赋值的只能实现浅拷贝
 * @author Jef
 * @date 2019/7/7
 */
public class CopyUtil {
    /**
     * 对象深度克隆---使用序列化进行深拷贝
     * @param obj 要克隆的对象
     * @return
     * 注意：
     * 使用序列化的方式来实现对象的深拷贝，但是前提是，对象必须是实现了 Serializable接口才可以，Map本身没有实现
     * Serializable 这个接口，所以这种方式不能序列化Map，也就是不能深拷贝Map。但是HashMap是可以的，因为它实现了Serializable。
     */
    public static <T extends Serializable> T clone(T obj) {
        T clonedObj = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.close();
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            clonedObj = (T) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clonedObj;
    }

    /*
     * 集合深拷贝
     * @author Jef
     * @date 2019/11/1
     * @param src
     * @return java.util.List<T>
     */
    public static <T> List<T> deepCopy(List<T> src) throws IOException,ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        List<T> dest = (List<T>) in.readObject();
        return dest;
    }

}