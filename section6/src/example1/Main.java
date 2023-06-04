package example1;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        InventoryCounter inventoryCounter = new InventoryCounter();
        IncrementingThread incrementingThread = new IncrementingThread(inventoryCounter);
        DecrementingThread decrementingThread = new DecrementingThread(inventoryCounter);

        /**
         * 각 스레드내의 각 작업이 여러개의 연산으로 이루어 지고 하나의 작업이 원자적 작업이 아님으로
         * 각 연산 사이 발생하는 문제로 인해 정확한 값이 나오지 않는다.
         */
        incrementingThread.start();
        decrementingThread.start();
        incrementingThread.join();
        decrementingThread.join();
        System.out.println("inventoryCounter = " + inventoryCounter.getItems());
    }

    public static class IncrementingThread extends Thread{
        private InventoryCounter inventoryCounter;

        public IncrementingThread(InventoryCounter inventoryCounter) {
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run(){
            for (int i = 0; i < 10000; i++) {
                inventoryCounter.increment();
            }
        }
    }
    public static class DecrementingThread extends Thread{
        private InventoryCounter inventoryCounter;

        public DecrementingThread(InventoryCounter inventoryCounter) {
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run(){
            for (int i = 0; i < 10000; i++) {
                inventoryCounter.decrement();
            }
        }
    }
    private static class InventoryCounter{
        private int items = 0;

        public synchronized void increment(){
            items++;
        }
        public synchronized void decrement(){
            items--;
        }
        public synchronized int getItems(){
            return items;
        }
    }
}
