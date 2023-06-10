package example2;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static final int HIGHEST_PRICE = 1000;

    public static void main(String[] args) throws InterruptedException {
        InventoryDatabase inventoryDatabase = new InventoryDatabase();
        Random random = new Random();
        for(int i=0;i<100000;i++){
            inventoryDatabase.addItem(random.nextInt(HIGHEST_PRICE));
        }
        Thread writer = new Thread(()->{
            inventoryDatabase.addItem(random.nextInt(HIGHEST_PRICE));
            inventoryDatabase.removeItem(random.nextInt(HIGHEST_PRICE));
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }
        });

        writer.setDaemon(true);
        writer.start();
        int numberOfReadThreads = 7;
        List<Thread> readers = new ArrayList<>();

        for (int readeridx = 0; readeridx < numberOfReadThreads; readeridx++) {
            Thread reader = new Thread(()->{
                for(int i=0;i<10000;i++){
                    int upperBoundPrice = random.nextInt(HIGHEST_PRICE);
                    int lowerBoundPrice = upperBoundPrice>0 ? random.nextInt(upperBoundPrice) : 0;
                    inventoryDatabase.getNumberOfItemsInPriceRange(lowerBoundPrice, upperBoundPrice);
                }
            });
            reader.setDaemon(true);
            readers.add(reader);
        }
        long startReadingTime = System.currentTimeMillis();
        for (Thread reader : readers) {
            reader.start();
        }
        for (Thread reader : readers) {
            reader.join();
        }
        long endReadingTime = System.currentTimeMillis();

        System.out.println("(endReadingTime - startReadingTime) = " + (endReadingTime - startReadingTime));
    }
    public static class InventoryDatabase{
        //가격과 가격에 해당하는 상품수를 보여주는 데이터 주고를 위한 TreeMap 선언.
        private TreeMap<Integer, Integer> priceToCountMap = new TreeMap<>();
        private ReentrantLock lock = new ReentrantLock();

        public int getNumberOfItemsInPriceRange(int lowerBound, int upperBound) {
            lock.lock();
            try {
                Integer fromKey = priceToCountMap.ceilingKey(lowerBound);
                Integer toKey = priceToCountMap.floorKey(upperBound);

                if (fromKey == null || toKey == null) {
                    return 0;
                }
                NavigableMap<Integer, Integer> rangeOfPrices = priceToCountMap.subMap(fromKey, true, toKey, true);
                int sum = 0;
                for (int numberOfItemsForPrice : rangeOfPrices.values()) {
                    sum += numberOfItemsForPrice;
                }
                return sum;
            }finally {
                lock.unlock();
            }
        }

        public void addItem(int price){
            lock.lock();
            try {
                Integer numberOfItemsForPrice = priceToCountMap.get(price);
                if (numberOfItemsForPrice == null) {
                    priceToCountMap.put(price, 1);
                } else {
                    priceToCountMap.put(price, numberOfItemsForPrice + 1);
                }
            } finally {
                lock.unlock();
            }

        }
        public void removeItem(int price){
            lock.lock();
            try {
                Integer numberOfItemsForPrice = priceToCountMap.get(price);
                if (numberOfItemsForPrice == null || numberOfItemsForPrice == 1) {
                    priceToCountMap.remove(price);
                } else {
                    priceToCountMap.put(price, numberOfItemsForPrice - 1);
                }
            }finally{
                lock.unlock();
            }
        }
    }
}
