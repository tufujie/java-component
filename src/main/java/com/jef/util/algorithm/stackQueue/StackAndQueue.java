package com.jef.util.algorithm.stackQueue;

import java.util.Stack;

/**
 * @author Jef
 * @date 2023/4/2
 */
public class StackAndQueue {
    // ⽤两个栈来实现⼀个队列，完成队列的Push和Pop操作。 队列中的元素为int类型。
    static Stack<Integer> stack1 = new Stack<Integer>();
    static Stack<Integer> stack2 = new Stack<Integer>();

    // 当执⾏push操作时，将元素添加到stack1
    public static void push(int node) {
        stack1.push(node);
    }

    public static int pop() {
        // 如果两个队列都为空则抛出异常,说明⽤户没有push进任何元素
        if (stack1.empty() && stack2.empty()) {
            throw new RuntimeException("Queue is empty!");
        }
        // 如果stack2不为空直接对stack2执⾏pop操作，
        if (stack2.empty()) {
            while (!stack1.empty()) {
                // 将stack1的元素按后进先出push进stack2⾥⾯
                stack2.push(stack1.pop());
            }
        }
        return stack2.pop();
    }

    public static boolean isPopOrder(int[] pushA, int[] popA) {
        if (pushA.length == 0 || popA.length == 0) {
            return false;
        }
        Stack<Integer> s = new Stack<Integer>();
        // ⽤于标识弹出序列的位置
        int popIndex = 0;
        for (int i = 0; i < pushA.length; i++) {
            s.push(pushA[i]);
            // 如果栈不为空，且栈顶元素等于弹出序列
            while (!s.empty() && s.peek() == popA[popIndex]) {
                // 出栈
                s.pop();
                // 弹出序列向后⼀位
                popIndex++;
            }
        }
        return s.empty();
    }
}