package com.example3;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        List<Long> inputNumbers = Arrays.asList(100000000L,3435L,35435L,2324L,4656L,23L,2435L,5566L);
        //각각의 리스트 안의 값을 팩토리얼로 계산 할 것.

        List<FactorialThread> threads = new ArrayList<>();
        //모든 입력번호에 대해 개별적인 스레드 객체를 생성
        for (long inputNumber : inputNumbers) {
            threads.add(new FactorialThread(inputNumber));
        }
        for(Thread thread: threads){
            thread.start();
        }
        //모든 스레드의 join 메서드는 스레드가 종료되어야만 반환된다.

        //만약 하나의 스레드의 계산이 너무 오래걸린다면 2초후에 끝나게 한다 하지만 어플리케이션은 종료되지 않음으로
        //데몬으로 설정해 메인스레드가 종료된다면 종료되도록 한다.
        for (Thread thread : threads) {
            thread.setDaemon(true); //하나의 스레드
            thread.join(2000);
        }
        for (int i = 0; i < inputNumbers.size(); i++) {
            FactorialThread factorialThread = threads.get(i);
            if (factorialThread.isFinished()) {
                System.out.println("Factorial of " + inputNumbers.get(i) + " is " + factorialThread.getResult());
            }else{
                System.out.println("The calculation for " + inputNumbers.get(i) + " is still in progress");
            }

        }
    }

    public static class FactorialThread extends Thread{
        private long inputNumber;
        private BigInteger result = BigInteger.ZERO;
        private boolean isFinished = false;

        public FactorialThread(long inputNumber) {
            this.inputNumber = inputNumber;
        }

        @Override
        public void run(){
            this.result = factorial(inputNumber);
            this.isFinished = true;
        }

        public BigInteger factorial(long n) {
            BigInteger tempResult = BigInteger.ONE;

            for(long i = n; i > 0; i--) {
                tempResult = tempResult.multiply(new BigInteger(Long.toString(i)));
            }
            return tempResult;
        }
        public boolean isFinished() {
            return isFinished;
        }

        public BigInteger getResult() {
            return result;
        }
    }
}
