package com.yn.test;

public class Test extends Thread {

    public Test(Runnable r) {
        super(r);
    }

    public static void main(String[] agrs) {
        System.out.println("Hello World!");
    }
}
