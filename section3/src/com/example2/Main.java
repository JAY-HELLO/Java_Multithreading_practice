package com.example2;

import java.math.BigInteger;

public class Main {
    public static void main(String[] args) {
        Thread thread = new Thread(new LongComputationTask(new BigInteger("20000"), new BigInteger("10000000")));
        thread.start();
        thread.interrupt();
    }
    private static class LongComputationTask implements Runnable{
        private BigInteger base;
        private BigInteger power;

        public LongComputationTask(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
            System.out.println("pow(base,power) = " + pow(base,power));
        }

        private BigInteger pow(BigInteger base, BigInteger power) {
            BigInteger result = BigInteger.ONE;
            for (BigInteger i = BigInteger.ZERO; i.compareTo(power)!=0; i=i.add(BigInteger.ONE)) {
                //가장 시간이 많이 걸리는 반복문 안에 매 반복마다 interrupted가 걸렸는지 확인하는 메소드를 통해서
                //인터럽트가 걸렸을 경우 0을 반환하도록 한다.
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Prematurely interrupted computation");
                    return BigInteger.ZERO;
                }
                result = result.multiply(base);
            }
            return result;
        }
    }
}
