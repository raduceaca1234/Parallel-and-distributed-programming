package utils;

import models.Account;
import models.Transaction;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class Generators {

    public Generators() {

    }

    public static HashMap<Integer, Account> generateAccounts(int numAccounts) {
        HashMap<Integer, Account> accounts = new HashMap<>();
        for (int i = 0; i < numAccounts; i++) {
            while (true) {
                String name = new RandomString(8, new Random()).nextString();
                Random r = new Random();
                Double randomValue = 50 + (200000 - 50) * r.nextDouble();
                Integer id = r.nextInt(100000);
                Account acc = new Account(name, id, randomValue);
                Account inDict = accounts.get(acc.getId());
                if (inDict == null) {
                    accounts.put(acc.getId(), acc);
                    break;
                }
            }
        }
        return accounts;
    }

    public static HashMap<Integer, Transaction> generateTransactions(int numTransactions, HashMap<Integer, Account> accLst) {
        // function to generate a specified number of transactions
        HashMap<Integer, Transaction> trans = new HashMap<>();
        for (int i = 0; i < numTransactions; i++) {
            while (true) {
                List<Integer> accounts = new ArrayList<>(accLst.keySet());
                Random rand = new Random();
                Integer randomAcc1 = accounts.get(rand.nextInt(accounts.size()));
                Integer randomAcc2 = accounts.get(rand.nextInt(accounts.size()));
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                Random r = new Random();
                Integer id = r.nextInt(100000);
                Double ammount = rand.nextDouble() * 10000;
                Transaction tr = new Transaction(id, randomAcc1, randomAcc2, now.toString(), ammount);
                Transaction inDict = trans.get(tr.getTid());
                if (inDict == null) {
                    trans.put(tr.getTid(), tr);
                    break;
                }
            }
        }
        return trans;
    }

}

class RandomString {

    /**
     * Generate a random string.
     */
    public String nextString() {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf);
    }

    public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String lower = upper.toLowerCase(Locale.ROOT);

    public static final String digits = "0123456789";

    public static final String alphanum = upper + lower + digits;

    private final Random random;

    private final char[] symbols;

    private final char[] buf;

    public RandomString(int length, Random random, String symbols) {
        if (length < 1) throw new IllegalArgumentException();
        if (symbols.length() < 2) throw new IllegalArgumentException();
        this.random = Objects.requireNonNull(random);
        this.symbols = symbols.toCharArray();
        this.buf = new char[length];
    }

    /**
     * Create an alphanumeric string generator.
     */
    public RandomString(int length, Random random) {
        this(length, random, alphanum);
    }

    /**
     * Create an alphanumeric strings from a secure generator.
     */
    public RandomString(int length) {
        this(length, new SecureRandom());
    }
}