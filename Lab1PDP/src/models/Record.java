package models;

public class Record {

    private Integer serialNum;
    private Transaction transaction;

    public Record(Integer serialNum, Transaction transaction) {
        this.serialNum = serialNum;
        this.transaction = transaction;
    }

    public Integer getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(Integer serialNum) {
        this.serialNum = serialNum;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public String toString(){
        return "ID: " + this.serialNum + "\n" + this.transaction;
    }

}