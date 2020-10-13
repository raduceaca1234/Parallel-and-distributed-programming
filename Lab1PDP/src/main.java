import models.*;
import utils.Generators;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.*;


/*
At a bank, we have to keep track of the balance of some accounts.
Also, each account has an associated log (the list of records of operations performed on that account).
Each operation record shall have a unique serial number, that is incremented for each operation performed in the bank.
We have concurrently run transfer operations, to be executer on multiple threads.
Each operation transfers a given amount of money from one account to someother account,
and also appends the information about the transfer to the logs of both accounts.
From time to time, as well as at the end of the program, a consistency check shall be executed.
It shall verify that the amount of money in each account corresponds with the operations records associated to that account,
and also that all operations on each account appear also in the logs of the source or destination of the transfer.
*/

public class main {
    private static HashMap<Integer, Log> createLogs(HashMap<Integer, Account> accounts) {
        // Also, each account has an associated log (the list of records of operations performed on that account).
        HashMap<Integer, Log> map = new HashMap<>();
        for (Map.Entry<Integer, Account> entry : accounts.entrySet()) {
            Account acc = entry.getValue();
            map.put(acc.getId(), new Log(acc.getId()));
        }
        return map;
    }

    private static HashMap<Integer, Account> copyAccounts(HashMap<Integer, Account> accMap) {
        // Copies the account map (for sequential use)
        HashMap<Integer, Account> accs = new HashMap<>();
        for (Map.Entry<Integer, Account> entry : accMap.entrySet()) {
            Account acc = entry.getValue();
            accs.put(acc.getId(), new Account(acc.getName(), acc.getId(), acc.getBalance()));
        }

        return accs;
    }

    private static List<Record> createRecordsForTransactions(HashMap<Integer, Transaction> transMap) {
        List<Record> records = new ArrayList<>();
        Random rand = new Random();
        transMap.values().forEach(t -> records.add(new Record(rand.nextInt(10000), t)));
        return records;
    }

    private static boolean verify(HashMap<Integer, Account> accounts, List<Record> records, HashMap<Integer, Account> newAccMap) {
        records.forEach(r -> {
            Transaction tr = r.getTransaction();
            Integer accid1 = tr.getAc1id();
            Integer accid2 = tr.getAc2id();
            Double amount = tr.getAmount();
            Account acc1 = accounts.get(accid1);
            Account acc2 = accounts.get(accid2);

                if (acc1.getBalance() - amount > 0) {
                    acc1.setBalance(acc1.getBalance() - amount);
                    acc2.setBalance(acc2.getBalance() + amount);
                    accounts.put(accid1, acc1);
                    accounts.put(accid2, acc2);
                }
             else {
                if (acc2.getBalance() - amount > 0) {
                    acc2.setBalance(acc2.getBalance() - amount);
                    acc1.setBalance(acc1.getBalance() + amount);
                    accounts.put(accid1, acc1);
                    accounts.put(accid2, acc2);
                }
            }
        });

        for (Map.Entry<Integer, Account> entrySet : accounts.entrySet()) {
            Integer key = entrySet.getKey();
            Account acc1 = accounts.get(key);
            Account acc2 = newAccMap.get(key);
            if (!acc1.getBalance().equals(acc2.getBalance())) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {

        int NUMBER_OF_THREADS = 1000000;
        int NUMBER_OF_ACCOUTNS = 10000;
        int NUMBER_OF_TRANSACTIONS = 100000;
        HashMap<Integer, Account> accMap = Generators.generateAccounts(NUMBER_OF_ACCOUTNS);
        HashMap<Integer, Log> logMap = createLogs(accMap);
        HashMap<Integer, Transaction> transMap = Generators.generateTransactions(NUMBER_OF_TRANSACTIONS, accMap);
        List<Record> records = createRecordsForTransactions(transMap);
        List<Thread> threads = new ArrayList<>();
        ReentrantLock mutex = new ReentrantLock();
        HashMap<Integer, Account> accountsCopy = copyAccounts(accMap);
        int trPerThread = NUMBER_OF_TRANSACTIONS / NUMBER_OF_THREADS;
        int remainTransactions = NUMBER_OF_TRANSACTIONS % NUMBER_OF_THREADS;
        int stop, start = 0;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            stop = start + trPerThread;   //stop index of current tread
            if (remainTransactions > 0) { //if remain left (nb of transactions that remained undone)
                stop += 1; //add one more step to the current thread
                remainTransactions -= 1;
            }
            Thread th = new MyThread(accMap, records, start, stop, mutex, logMap);
            threads.add(th);
            start = stop;
        }
        threads.forEach(Thread::start);

        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        boolean ok = verify(accountsCopy, records, accMap);

        long endTime = System.currentTimeMillis();
        System.out.println("Number of threads:"+NUMBER_OF_THREADS);
        System.out.println("Number of accounts:"+NUMBER_OF_ACCOUTNS);
        System.out.println("Number of transactions:"+NUMBER_OF_TRANSACTIONS);
        System.out.println("Work done :"+(double) (endTime - startTime) / (1000F)+" seconds");
        System.out.println("Verify consistency: "+ok);
    }


}

