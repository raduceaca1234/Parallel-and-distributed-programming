import java.util.LinkedList;

public class Main {
    public static void main(String[] args)
            throws InterruptedException
    {
        // Object of a class that has both produce()
        // and consume() methods
        final PC pc = new PC();

        // Create producer thread
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try {
                    pc.produce();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // Create consumer thread
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try {
                    pc.consume();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // Start both threads
        t1.start();
        t2.start();

        // t1 finishes before t2
        t1.join();
        t2.join();
    }

    // This class has a list, producer (adds items to list
    // and consumber (removes items).
    public static class PC {

        // Create a list shared by producer and consumer
        // Size of list is 2.
        LinkedList<Integer> list = new LinkedList<>();

        int vect_A[] = { 3, -5, 4 };
        int vect_B[] = { 2, 6, 5 };
        int sum = 0;
        int c_sum = sum;
        int prod = 1;
        int c_prod = prod;
        int max = 3;
        int ok=1;
        int ok2=1;
        int value = 0;
        // Function called by producer thread
        public void produce() throws InterruptedException
        {
            while (true) {
                synchronized (this)
                {
                    // producer thread waits while list
                    // is full
                    while (value==max || ok==0)
                        wait();
                    // to insert the jobs in the list
                    prod=vect_A[value]*vect_B[value];

                    ok=0;
                    ok2=1;

                    System.out.println("Producer produced- "
                            +vect_A[value] + " * "+ vect_B[value]+ " = "+ prod);
                    value++;
                    // notifies the consumer thread that
                    // now it can start consuming
                    notify();

                    // makes the working of program easier
                    // to  understand
                    Thread.sleep(1000);
                }
            }
        }

        // Function called by consumer thread
        public void consume() throws InterruptedException
        {
            while (true) {
                synchronized (this)
                {
                    // consumer thread waits while list
                    // is empty
                    while (ok2==0)
                        wait();
                    // to retrive the ifrst job in the list
                    sum=sum+prod;

                    System.out.println("Consumer consumed-"
                            + sum);
                    ok2=0;
                    ok=1;
                    // Wake up producer thread
                    notify();

                    // and sleep
                    Thread.sleep(1000);
                }
            }
        }
    }
}