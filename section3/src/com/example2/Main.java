package com.example2;

import java.math.BigInteger;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new LongComputationTask(new BigInteger("20000"), new BigInteger("10000000")));

        //스레드를 데몬으로 설정하면 스레드는 주작업이 아닌 백그라운드 작업을 진행하게 된다
        //이 경우 메인 스레드가 종료된것 만으로 전체 앱이 종료된다.
        //thread.setDaemon(true);
        thread.start();
        Thread.sleep(100);
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
//                if (Thread.currentThread().isInterrupted()) {
//                    System.out.println("Prematurely interrupted computation");
//                    return BigInteger.ZERO;
//                }
                result = result.multiply(base);
            }
            return result;
        }
    }
}
