package com.example;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            throw new RuntimeException("Internal Exception");
        });

        thread.setName("오류 스레드");
        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println(" critical error happend in thread " + t.getName() + " the error is " + e.getMessage());
            }
        });
        thread.start();
        Thread.sleep(10000);

    }
}
