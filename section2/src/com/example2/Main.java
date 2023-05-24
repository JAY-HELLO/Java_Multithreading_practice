package com.example2;

public class Main {
    public static void main(String[] args) {
        Thread thread = new NewThread();
        thread.start();
    }

    private static class NewThread extends Thread{
        @Override
        public void run(){
            System.out.println("Thread.currentThread().getName() = " + Thread.currentThread().getName());

        }
    }
}
