package com.jef.util.algorithm.sort;

import com.jef.util.PrintUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * 数组排序工具类
 * 从小到大排序
 *
 * @author Jef
 * @date 2021/4/13
 */
public class ArraySortUtil {

    /**
     * 交换数组中的元素
     *
     * @param array 数组
     * @param i     交换元素a
     * @param j     交换元素b
     * @return void
     * @author Jef
     * @date 2022/1/5
     */
    public static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    /**
     * 冒泡排序（每一趟找出最大的）
     * 性能一般
     * 冒泡排序,每一趟找出最大的，总共比较次数为array.length - 1次，每次的比较次数为array.length - i - 1次，依次递减
     *
     * @param array
     * @author Jef
     * @date 2021/4/13
     */
    public static int[] bubbleSort(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = i + 1; j < array.length; j++) {
                if (array[i] > array[j]) {
                    int temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                }
            }
            System.out.print("第" + (i + 1) + "次排序结果:");
            PrintUtil.printArray(array);
        }
        // 这是冒泡的另一种解法
        /*for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                // 这里-i主要是每遍历一次都把最大的i个数沉到最底下去了，没有必要再替换了
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }*/
        return array;
    }

    /**
     * 选择排序（假定某个位置的值是最小值）
     * 性能一般
     *
     * @param array
     * @return java.lang.int[]
     * @author Jef
     * @date 2021/4/13
     */
    public static int[] simpleChooseSort(int[] array) {
        // 简单的选择排序
        for (int i = 0; i < array.length; i++) {
            int min = array[i];
            // 最小数的索引
            int n = i;
            for (int j = i + 1; j < array.length; j++) {
                // 找出最小的数
                if (array[j] < min) {
                    min = array[j];
                    n = j;
                }
            }
            array[n] = array[i];
            array[i] = min;

        }
        return array;
    }

    /**
     * 选择排序
     *
     * @param array
     * @return java.lang.int[]
     * @author Jef
     * @date 2022/1/5
     */
    public static int[] selectionSort(int[] array) {
        int min;
        int len = array.length;
        for (int i = 0; i < len - 1; i++) {
            min = i;
            for (int j = i + 1; j < len; j++) {
                if (array[j] < array[min]) {
                    min = j;
                }
            }
            int temp = array[i];
            array[i] = array[min];
            array[min] = temp;
            System.out.print("第" + (i + 1) + "次排序结果:");
            PrintUtil.printArray(array);
        }
        return array;
    }

    /**
     * 选择排序
     *
     * @param array
     * @return java.lang.int[]
     * @author Jef
     * @date 2022/1/5
     */
    public static int[] selectionSortV2(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < array.length; j++) {
                if (array[min] > array[j]) {
                    min = j;
                }
            }
            if (i != min) {
                int temp = array[i];
                array[i] = array[min];
                array[min] = temp;
            }
            System.out.print("第" + (i + 1) + "次排序结果:");
            PrintUtil.printArray(array);
        }
        return array;
    }


    /**
     * 桶排序（桶中出现的数组元素都做个标记1，然后将桶数组中有1标记的元素依次打印）
     * 简单, 但是不用,浪费内存
     *
     * @param array
     * @return java.lang.int[]
     * @author Jef
     * @date 2021/4/13
     */
    public static int[] bucketSort(int[] array) {
        Integer maxValue = 0;
        for (Integer arr : array) {
            if (arr > maxValue) {
                maxValue = arr;
            }
        }
        int[] arrayTwo = new int[maxValue + 1];
        for (int i = 0; i < array.length; i++) {
            int key = array[i];
            arrayTwo[key] = 1;
        }
        return arrayTwo;
    }

    public static int[] bucketSortV2(int[] arr) {
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < arr.length; i++) {
            max = Math.max(max, arr[i]);
            min = Math.min(min, arr[i]);
        }
        // 创建桶
        int bucketNum = (max - min) / arr.length + 1;
        ArrayList<ArrayList<Integer>> bucketArr = new ArrayList<>(bucketNum);
        for (int i = 0; i < bucketNum; i++) {
            bucketArr.add(new ArrayList<Integer>());
        }
        // 将每个元素放入桶
        for (int i = 0; i < arr.length; i++) {
            int num = (arr[i] - min) / (arr.length);
            bucketArr.get(num).add(arr[i]);
        }
        // 对每个桶进行排序
        for (int i = 0; i < bucketArr.size(); i++) {
            Collections.sort(bucketArr.get(i));
        }
        int[] resultArray = new int[arr.length];
        for (int i = 0; i < bucketArr.size(); i++) {
            ArrayList<Integer> list = bucketArr.get(i);
            for (int j = 0; j < list.size(); j++) {
                resultArray[j] = list.get(j);
            }
        }
        return resultArray;
    }

    /**
     * 在链表中添加元素的同时需要进行元素的排序
     *
     * @param list
     * @param i
     * @return void
     * @author Jef
     * @date 2022/1/5
     */
    public static void bucketsort(ArrayList<Integer> list, int i) {
        if (list == null)
            list.add(i);
            //这里采用的排序方式为插入排序
        else {
            int flag = list.size() - 1;
            while (flag >= 0 && list.get(flag) > i) {
                if (flag + 1 >= list.size())
                    list.add(list.get(flag));
                else
                    list.set(flag + 1, list.get(flag));
                flag--;
            }
            if (flag != (list.size() - 1))
                //注意这里是flag+1,自己可以尝试将这里换成flag看看,会出现数组越界的情况
                list.set(flag + 1, i);
            else
                list.add(i);
        }
    }

    public static int[] bucketsort(int[] array, int sum) {
        // 遍历得到数组中的最大值与最小值
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < array.length; i++) {
            min = min <= array[i] ? min : array[i];
            max = max >= array[i] ? max : array[i];
        }
        // 求出每个桶的长度,这里必须使用Double
        double size = (double) (max - min + 1) / sum;
        ArrayList<Integer> list[] = new ArrayList[sum];
        for (int i = 0; i < sum; i++) {
            list[i] = new ArrayList<Integer>();
        }
        // 将每个元素放入对应的桶之中同时进行桶内元素的排序
        for (int i = 0; i < array.length; i++) {
            System.out.println("元素:" + String.format("%-2s", array[i]) + ", 被分配到" + (int) Math.floor((array[i] - min) / size) + "号桶");
            bucketsort(list[(int) Math.floor((array[i] - min) / size)], array[i]);
        }
        System.out.println();
        for (int i = 0; i < sum; i++) {
            System.out.println(String.format("%-1s", i) + "号桶内排序:" + list[i]);
        }
        System.out.println();
        // 顺序遍历各个桶,得出我们 已经排序号的序列
        int[] arrayResult = new int[array.length];
        int k = 0;
        for (int i = 0; i < list.length; i++) {
            if (list[i] != null) {
                for (int j = 0; j < list[i].size(); j++) {
                    arrayResult[k++] = list[i].get(j);
                }
            }
        }
        return arrayResult;
    }

    /**
     * 插入排序
     *
     * @param array
     * @return java.lang.int[]
     * @author Jef
     * @date 2021/4/13
     */
    public static int[] directInsertSort(int[] array) {
        // 直接插入排序
        for (int i = 1; i < array.length; i++) {
            // 待插入元素
            int temp = array[i];
            int j;
            /*
             * for (j = i-1; j>=0 && a[j]>temp; j--) { //将大于temp的往后移动一位 a[j+1] = a[j]; }
             */
            for (j = i - 1; j >= 0; j--) {
                // 将大于temp的往后移动一位
                if (array[j] > temp) {
                    array[j + 1] = array[j];
                } else {
                    break;
                }
            }
            array[j + 1] = temp;
        }
        return array;
    }

    /**
     * 插入排序
     *
     * @param array
     * @return java.lang.int[]
     * @author Jef
     * @date 2021/4/13
     */
    public static int[] insertSort(int[] array) {
        for (int i = 1; i < array.length; i++) {
            // 插入的数
            int insertVal = array[i];
            // 被插入的位置(即插入的数准备和前一个数比较)
            int index = i - 1;
            // 如果插入的数比被插入位置的数小
            while (index >= 0 && insertVal < array[index]) {
                // 将把array[index]向后移动
                array[index + 1] = array[index];
                // 让index向前移动
                index--;
            }
            // 把插入的数放入合适位置
            array[index + 1] = insertVal;
            System.out.print("第" + i + "次排序结果:");
            PrintUtil.printArray(array);
        }
        return array;
    }

    /**
     * 插入排序
     *
     * @param array
     * @return java.lang.int[]
     * @author Jef
     * @date 2021/4/13
     */
    public static int[] insertSortV2(int[] array) {
        int n = array.length;
        for (int i = 1; i < n; i++) {
            if (array[i] < array[i - 1]) {
                int val = array[i];
                int j = i;
                while (j > 0 && val < array[j - 1]) {
                    array[j] = array[j - 1];
                    j--;
                }
                if (j != i) {
                    array[j] = val;
                }
            }
            System.out.print("第" + i + "次排序结果:");
            PrintUtil.printArray(array);
        }
        return array;
    }

    /**
     * 希尔排序（性能最好的排序）
     *
     * @param array
     * @return java.lang.int[]
     * @author Jef
     * @date 2021/4/13
     */
    public static int[] shellSort(int[] array) {
        // 分组间隔设置
        int d = array.length;
        while (true) {
            d = d / 2;
            // 类似插入排序，只是插入排序增量是1，这里增量是d,把1换成d就可以了
            for (int x = 0; x < d; x++) {
                for (int i = x + d; i < array.length; i = i + d) {
                    // temp为待插入元素
                    int temp = array[i];
                    int j;
                    for (j = i - d; j >= 0 && array[j] > temp; j = j - d) {
                        // 通过循环，逐个后移一位找到要插入的位置。
                        array[j + d] = array[j];
                    }
                    // 插入
                    array[j + d] = temp;
                }
            }
            if (d == 1) {
                break;
            }
        }
        return array;
    }

    /**
     * 希尔排序（性能最好的排序）
     *
     * @param array
     * @return java.lang.int[]
     * @author Jef
     * @date 2021/4/13
     */
    public static int[] shellSortV2(int[] array) {
        // 规定步长
        for (int step = array.length / 2; step > 0; step /= 2) {
            System.out.println("步长为" + step + "的分组排序:");
            // 步长确定之后就需要分批次的对分组进行插入排序
            for (int l = 0; l < step; l++) {
                // 插入排序的代码
                for (int i = l + step; i < array.length; i += step) {
                    int temp = array[i];
                    int j = i;
                    while (j > 0 && temp < array[j - step]) {
                        array[j] = array[j - step];
                        j -= step;
                        // 这里需要注意一点就是j-step可能会越界，所以我们需要继续进行判断，之前在插入排序中，步长始终是1，所以在while循环那里就会阻断，但是现在步长会发生变化
                        // 所以我们需要在这里提前进行判断，否则金辉发生数组越界的情况
                        if (j - step < 0) {
                            break;
                        }
                    }
                    if (j != i) {
                        array[j] = temp;
                    }
                }
                System.out.print("第" + (l + 1) + "次排序结果:");
                PrintUtil.printArray(array);
            }
        }
        return array;
    }

    /**
     * 二分插入排序
     *
     * @param array
     * @return java.lang.int[]
     * @author Jef
     * @date 2021/5/30
     */
    public static int[] binaryInsertSrot(int[] array) {
        for (int i = 0; i < array.length; i++) {
            int temp = array[i];
            int left = 0;
            int right = i - 1;
            int mid = 0;
            while (left <= right) {
                mid = (left + right) / 2;
                if (temp < array[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
            for (int j = i - 1; j >= left; j--) {
                array[j + 1] = array[j];
            }
            if (left != i) {
                array[left] = temp;
            }
        }
        return array;
    }

    /**
     * 快速排序
     *
     * @param array
     * @param left
     * @param right
     * @return java.lang.int[]
     * @author Jef
     * @date 2022/1/5
     */
    public static int[] quickSort(int[] array, int left, int right) {
        if (left < right) {
            int key = array[left];
            int start = left;
            int end = right;
            // 这部分便是算法的核心思想
            while (start < end) {
                // 从右向左找到第一个不大于基准值的元素
                while (start < end && array[end] >= key) {
                    end--;
                }
                if (start < end) {
                    array[start] = array[end];
                }
                //从左往右找到第一个不小于基准值的元素
                while (start < end && array[start] <= key) {
                    start++;
                }
                if (start < end) {
                    array[end] = array[start];
                }
            }
            array[start] = key;
            // 递归继续对剩余的序列排序
            quickSort(array, left, start - 1);
            quickSort(array, start + 1, right);
        }
        return array;
    }

    /**
     * 计数排序
     *
     * @param array
     * @return java.lang.int[]
     * @author Jef
     * @date 2022/1/5
     */
    public static int[] countSort(int[] array) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        // 先找出数组中的最大值与最小值
        for (int i = 0; i < array.length; i++) {
            if (array[i] < min)
                min = array[i];
            if (array[i] > max)
                max = array[i];
        }
        // 创建一个长度为max-min+1长度的数组来进行计数
        int[] figure = new int[max - min + 1];
        for (int i = 0; i < array.length; i++) {
            // 计算每个数据出现的次数
            figure[array[i] - min]++;
        }
        int begin = 0;
        // 创建一个新的数组来存储已经排序完成的结果
        int[] arrayTemp = new int[array.length];
        for (int i = 0; i < figure.length; i++) {
            // 循环将数据pop出来
            if (figure[i] != 0) {
                for (int j = 0; j < figure[i]; j++) {
                    arrayTemp[begin++] = min + i;
                }
            }
        }
        System.out.println("数据范围:" + min + "~" + max);
        System.out.println("计数结果:  ");
        for (int i = 0; i < array.length; i++) {
            System.out.println("         " + array[i] + "出现" + figure[array[i] - min] + "次");
        }
        return arrayTemp;
    }

    /**
     * 归并排序
     *
     * @param array
     * @return java.lang.int[]
     * @author Jef
     * @date 2022/1/5
     */
    public static int[] mergeSort(int[] array) {
        //分裂之后的数组如果只有1个元素的话,
        //那么就说明可以开始合并的过程了,所以直接返回.
        if (array.length < 2) {
            return array;
        }
        int middle = array.length / 2;
        //截取左右两个序列
        int[] left = Arrays.copyOfRange(array, 0, middle);
        int[] right = Arrays.copyOfRange(array, middle, array.length);
        return mergeSort(mergeSort(left), mergeSort(right));
    }

    public static int[] mergeSort(int[] left, int[] right) {
        int[] array = new int[left.length + right.length];
        int i = 0, j = 0, k = 0;
        // 注意终止条件是&&,只要有一个不满足,循环就结束
        while (i < left.length && j < right.length) {
            if (left[i] < right[j])
                array[k++] = left[i++];
            else
                array[k++] = right[j++];
        }
        //上面的循环跳出之后，就说明有且仅会有一个序列还有值了，所以需要再次检查各个序列，并且下面的两个循环是互斥的，只会执行其中的一个或者都不执行
        while (i < left.length) {
            array[k++] = left[i++];
        }
        while (j < right.length) {
            array[k++] = right[j++];
        }
        PrintUtil.printArray(array);
        return array;
    }

    /**
     * 堆排序-将待排序的数组构建成大根堆
     *
     * @param array
     * @param end
     * @return void
     * @author Jef
     * @date 2022/1/5
     */
    public static void buildbigheap(int[] array, int end) {
        // 从最后一个非叶子节点开始构建,依照从下往上,从右往左的顺序
        for (int i = end / 2; i >= 0; i--) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int big = i;
            // 判断小分支那个是大元素
            if (left < end && array[i] < array[left]) {
                i = left;
            }
            if (right < end && array[i] < array[right]) {
                i = right;
            }
            swap(array, i, big);
        }
    }

    /**
     * 堆排序-将待排序的数组构建成大根堆
     *
     * @param array
     * @author Jef
     * @date 2022/1/5
     */
    public static int[] buildbigheap(int[] array) {
        // 第一次构建大根堆
        ArraySortUtil.buildbigheap(array, array.length);
        for (int j = array.length - 1; j > 0; j--) {
            // 交换队头已经排序得到的最大元素与队尾元素
            ArraySortUtil.swap(array, 0, j);
            System.out.print("第" + (array.length - j) + "次排序:  ");
            PrintUtil.printArray(array);
            // 交换结束之后,大根堆已经内破坏,需要开始重新构建大根堆
            ArraySortUtil.buildbigheap(array, j);
        }
        return array;
    }

    //将待排序的数组构建成大根堆
    public static void buildbigheapV2(int[] array, int end) {
        // 从最后一个非叶子节点开始构建,依照从下往上,从右往左的顺序
        for (int i = end / 2; i >= 0; i--) {
            adjustnode(i, end, array);
        }
    }

    /**
     * 调整该节点及其以下的所有节点
     *
     * @param i
     * @param end
     * @param array
     * @return void
     * @author Jef
     * @date 2022/1/5
     */
    public static void adjustnode(int i, int end, int[] array) {
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        int big = i;
        // 判断小分支那个是大元素
        if (left < end && array[i] < array[left])
            i = left;
        if (right < end && array[i] < array[right])
            i = right;
        if (i != big) {
            // 交换顺序之后需要继续校验
            swap(array, i, big);
            // 重新校验,防止出现交换之后根节点小于孩子节点的情况
            adjustnode(i, end, array);
        }
    }

    /**
     * 堆排序-将待排序的数组构建成大根堆
     *
     * @param array
     * @author Jef
     * @date 2022/1/5
     */
    public static int[] buildbigheapV2(int[] array) {
        //第一次构建大根堆
        buildbigheapV2(array, array.length);
        for (int j = array.length - 1; j > 0; j--) {
            System.out.print("第" + (array.length - j) + "次排序前:  ");
            PrintUtil.printArray(array);
            //交换队头已经排序得到的最大元素与队尾元素
            swap(array, 0, j);
            System.out.print("第" + (array.length - j) + "次排序后:  ");
            PrintUtil.printArray(array);
            //交换结束之后,大根堆已经被破坏,需要开始重新构建大根堆
            buildbigheapV2(array, j);
        }
        return array;
    }

    /**
     * 将所有的数组合并成原来的数组
     *
     * @param list
     * @param array
     * @return void
     * @author Jef
     * @date 2022/1/5
     */
    public static void merge(ArrayList<Integer> list[], int[] array) {
        int k = 0;
        for (int i = 0; i < list.length; i++) {
            if (list[i] != null) {
                for (int j = 0; j < list[i].size(); j++) {
                    array[k++] = list[i].get(j);
                    System.out.print(array[k - 1] + " ");
                }
            }
            //合并完成之后需要将链表清空,否则元素会越来越多
            list[i].clear();
        }
        System.out.println();
    }

    //将所有的元素分散到各个链表之中
    public static void split(ArrayList<Integer> list[], int[] array, int k) {
        for (int j = 0; j < array.length; j++) {
            list[array[j] / k % 10].add(array[j]);
        }
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("个位开始数,第" + (String.valueOf(k).length()) + "位排序结果:");
        for (int j = 0; j < 10; j++) {
            System.out.println((String.valueOf(k).length()) + "号位,数值为" + j + "的链表结果:" + list[j]);
        }
    }

    /**
     * 基数排序
     *
     * @param array
     * @return void
     * @author Jef
     * @date 2022/1/5
     */
    public static int[] radixSort(int[] array) {
        ArrayList<Integer> list[] = new ArrayList[10];
        for (int i = 0; i < 10; i++) {
            list[i] = new ArrayList<Integer>();
        }
        int max = Integer.MIN_VALUE;
        //第一次遍历获得序列中的最大值
        for (int i = 0; i < array.length; i++) {
            if (array[i] > max)
                max = array[i];
        }
        int k = 1;
        for (int i = 0; i < String.valueOf(max).length(); i++) {
            split(list, array, k);
            System.out.println("第" + (i + 1) + "次排序");
            merge(list, array);
            k = k * 10;
        }
        return array;
    }


}