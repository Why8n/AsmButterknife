package com.yn.test;

public class Test extends Thread {

    public Test(Runnable r) {
        super(r);
    }

    public static void main(String[] agrs) {
//        String reg = "access\\$\\d{3,}$";
//        String reg = "access\\$[01]+$";
//        System.out.println("access$000".matches(reg));
//        System.out.println("access$100".matches(reg));
//        System.out.println("access$1000".matches(reg));
//        System.out.println("access$0".matches(reg));
//        System.out.println("access$".matches(reg));
//        System.out.println("access$123".matches(reg));
//        System.out.println("access$1100a".matches(reg));
//        System.out.println("access$1a0".matches(reg));
        System.out.println(
                String.format("access$%03d", 9 * 100)
        );
    }
}
