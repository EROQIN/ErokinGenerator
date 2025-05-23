package com.yupi.acm;

import java.util.Scanner;

/**
 * ACM 输入模板
 * @author ${author}
 */
public class MainTemplate {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        <#if loop>
        while(scanner.hasNextInt()){
        </#if>
            //读取输入元素个数
            int n = scanner.nextInt();
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                arr[i] = scanner.nextInt();
            }

            //处理问题逻辑，根据需要进行输出
            //示例：计算数组元素之和
            int sum = 0;
            for (int num:arr) {
                sum += num;
            }
            System.out.println("${outputText}" + sum);
        <#if loop>
        }
        </#if>
        scanner.close();
    }
}