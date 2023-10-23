package com.jef.exception;

/**
 * 自定义异常
 * @author Jef
 * @create 20180714
 */
public class SimpleException extends Exception {
    public SimpleException() {
        super();
    }

    public SimpleException(String message) {
        super(message);
    }


    public static void main(String[] args) throws SimpleException {
        try {
            test();
        } catch (SimpleException e) {
            System.out.println(e);
        }
        test();
    }

    /**
     * 自定义异常抛出测试方法
     * @throws SimpleException
     */
    private static void test() throws SimpleException {
        System.err.println("抛出自定义异常");
        throw new SimpleException();
    }


}


