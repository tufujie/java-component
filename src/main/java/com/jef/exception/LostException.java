package com.jef.exception;

/**
 * 异常丢失
 * 旧的异常会被新的异常所覆盖
 * @author Jef
 * @create 2018/7/20 19:54
 */
public class LostException {
    public static void main(String[] args) {
        try {
            try {
                f();
            } finally {
                g();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void f() throws OneException {
        throw new OneException();
    }

    static void g() throws TwoException {
        throw new TwoException();
    }
}

class OneException extends Exception {}

class TwoException extends Exception {}