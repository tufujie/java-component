package com.jef.exception;

/**
 * finally始终运行
 * 始终从上到下执行
 * @author Jef
 * @create 2018/7/17 19:39
 */
public class AlwaysFinally {
    public static void main(String[] args) {
        for (int i = 5; i >= -5; i--) {
            try {
                System.out.println("enter in block");
                try {
                    BasicException.basicException(5, i);
                } finally {
                    System.out.println("finally in in block, i=" + i);
                }
            } catch (Exception e) {
                System.out.println("catch");
                // 当遇到break或者continue时，finally也会继续执行
                break;
            } finally {
                System.out.println("finally in out block, i=" + i);
            }
        }
    }
}
