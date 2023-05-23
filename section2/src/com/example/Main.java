package com.example;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            System.out.println(" we are now in thread " + Thread.currentThread().getName());
            System.out.println("Thread priority = " + Thread.currentThread().getPriority());
        });

        thread.setName("새 스레드");
        thread.setPriority(Thread.MAX_PRIORITY);
        System.out.println(" we are in thread  = " + thread.currentThread().getName() + " before starting a new thread" );
        thread.start();
        System.out.println(" we are in thread  = " + thread.currentThread().getName() + " after starting a new thread" );
        Thread.sleep(10000);

    }
}
