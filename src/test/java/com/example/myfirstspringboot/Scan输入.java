package com.example.myfirstspringboot;

import java.util.Scanner;

/**
 * @author liango
 * @version 1.0
 * @since 2018-02-05 22:00
 */
public class Scan输入 {
    public static void main(String[] args) {
        app();
    }

    private static void app() {
        System.out.println("请在控制台输入：");
        Scanner scanner = new Scanner(System.in);
        String b = scanner.nextLine();
        System.out.println("b = " + b);
    }
}
