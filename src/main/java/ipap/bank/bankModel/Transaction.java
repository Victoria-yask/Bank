package ipap.bank.bankModel;

import java.time.LocalDateTime;

public class Transaction {

    private long id;
    private long fromCardId;
    private long toCardId;
    private double amount;
    private LocalDateTime transactionDate;

    public Transaction(long fromCardId, long toCardId, double amount) {
        this.fromCardId = fromCardId;
        this.toCardId = toCardId;
        this.amount = amount;
    }

    public Transaction(long id, long fromCardId, long toCardId, double amount, LocalDateTime transactionDate) {
        this.id = id;
        this.fromCardId = fromCardId;
        this.toCardId = toCardId;
        this.amount = amount;
        this.transactionDate = transactionDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFromCardId() {
        return fromCardId;
    }

    public void setFromCardId(long fromCardId) {
        this.fromCardId = fromCardId;
    }

    public long getToCardId() {
        return toCardId;
    }

    public void setToCardId(long toCardId) {
        this.toCardId = toCardId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
}
