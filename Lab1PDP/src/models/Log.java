package models;

import java.util.ArrayList;
import java.util.List;

public class Log {
    private Integer accountId;

    public Log(Integer accountId, List<Record> records) {
        this.accountId = accountId;
        this.records = records;
    }

    private List<Record> records = new ArrayList<>();

    public Log(Integer accountId) {
        this.accountId = accountId;
    }

    public void addRecord(Record r) {
        records.add(r);
    }

    public List<Record> getRecords() {
        return this.records;
    }
}