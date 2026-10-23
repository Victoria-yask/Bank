package ipap.bank.bankModel;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class BankCard {
    private long id;
    private SimpleStringProperty cardType;
    private SimpleStringProperty cardNumber;
    private SimpleDoubleProperty balance;
    private SimpleBooleanProperty isActive;

    public BankCard() {
        this.cardType = new SimpleStringProperty();
        this.cardNumber = new SimpleStringProperty();
        this.balance = new SimpleDoubleProperty();
        this.isActive = new SimpleBooleanProperty(true);
    }

    public BankCard(String cardType, String cardNumber, double balance,  boolean isActive) {
        this.cardType = new SimpleStringProperty(cardType);
        this.cardNumber = new SimpleStringProperty(cardNumber);
        this.balance = new SimpleDoubleProperty(balance);
        this.isActive = new SimpleBooleanProperty(isActive);
    }

    public BankCard(long id, String cardType, String cardNumber, double balance,  boolean isActive) {
        this.id = id;
        this.cardType = new SimpleStringProperty(cardType);
        this.cardNumber = new SimpleStringProperty(cardNumber);
        this.balance = new SimpleDoubleProperty(balance);
        this.isActive = new SimpleBooleanProperty(isActive);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCardType() {
        return cardType.get();
    }

    public SimpleStringProperty cardTypeProperty() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType.set(cardType);
    }

    public String getCardNumber() {
        return cardNumber.get();
    }

    public SimpleStringProperty cardNumberProperty() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber.set(cardNumber);
    }

    public double getBalance() {
        return balance.get();
    }

    public SimpleDoubleProperty balanceProperty() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance.set(balance);
    }

    public BankCard(SimpleBooleanProperty isActive) {
        this.isActive = isActive;
    }

    public boolean isIsActive() {
        return isActive.get();
    }

    public SimpleBooleanProperty isActiveProperty() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive.set(isActive);
    }

    @Override
    public String toString() {
        return cardType.get() + " " + cardNumber.get() + " (" + String.format("%,.2f", balance.get()) + " ₽)";
    }


}
