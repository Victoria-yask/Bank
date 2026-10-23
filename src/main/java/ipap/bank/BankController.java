package ipap.bank;

import ipap.bank.bankModel.BankCard;
import ipap.bank.bankModel.BankService;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BankController {
    @FXML
    private Button createDebitCard;

    @FXML
    private ListView<BankCard> listDebitCard;

    @FXML
    private Button closeDebitCard;

    @FXML
    private Label messageDebitCard;

    @FXML
    private Button createCreditCard;

    @FXML
    private ListView<BankCard> listCreditCard;

    @FXML
    private ChoiceBox<BankCard> fromCard;

    @FXML
    private ChoiceBox<BankCard> toCard;

    @FXML
    private TextField amount;

    @FXML
    private Button transferMoney;

    @FXML
    private Label messageTransfer;

    @FXML
    private Label messageCreditCard;


    private final BankService bankService = new BankService();

    @FXML
    public void initialize() throws SQLException {
        listDebitCard();
        listCreditCard();
        loadAllFromCards();
        loadAllToCards();
        amount();


//в поле fromCard сохранить выбранную карту и обновить список toCard
        fromCard.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                loadAllToCards();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void listDebitCard() throws SQLException {
        List<BankCard> debitCards = bankService.getAllDebitCards();
        listDebitCard.setItems(FXCollections.observableArrayList(debitCards));
    }

    @FXML
    public void createDebitCard() throws SQLException {
        int count = bankService.getDebitCardCount();
        if (count >= 5) {
            showMessage(messageDebitCard, "Нельзя оформить больше 5 дебетовых карт!", "red");
            return;
        }
        bankService.createDebitCard();
        listDebitCard();
        loadAllFromCards();
        loadAllToCards();
        showMessage(messageDebitCard, "Дебетовая карта оформлена!", "green");
    }

    @FXML
    public void closeDebitCard() {
        BankCard closeCard = listDebitCard.getSelectionModel().getSelectedItem();
        if (closeCard.getBalance() > 0) {
            showMessage(messageDebitCard, "Нельзя закрыть карту: на ней есть средства!", "red");
            return;
        }
        try {
            bankService.closeDebitCard(closeCard.getId());
            listDebitCard();
            listCreditCard();
            loadAllFromCards();
            loadAllToCards();
            showMessage(messageDebitCard, "Карта закрыта", "green");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void createCreditCard() throws SQLException {
        int count = bankService.getCreditCardCount();
        if (count >= 3) {
            showMessage(messageCreditCard, "Нельзя оформить больше 3 кредитных карт!", "red");
            return;
        }
        bankService.createCreditCard();
        listCreditCard();
        loadAllFromCards();
        loadAllToCards();
        showMessage(messageCreditCard, "Кредитная карта оформлена!", "green");
    }

    public void listCreditCard() throws SQLException {
        List<BankCard> creditCards = bankService.getAllCreditCards();
        listCreditCard.setItems(FXCollections.observableArrayList(creditCards));
    }

    public void loadAllFromCards() throws SQLException {
        List<BankCard> allFromCards = bankService.getAllCards();
        ArrayList<BankCard> cardsWithMoney = new ArrayList<>();
        for (BankCard card : allFromCards) {
            if (card.getBalance() > 0) {
                cardsWithMoney.add(card);
            }
        }
        fromCard.setItems(FXCollections.observableArrayList(cardsWithMoney));
    }

    public void loadAllToCards() throws SQLException {
        BankCard selectedFromCard = fromCard.getValue();
        List<BankCard> allToCards = bankService.getAllCards();

        ArrayList<BankCard> filteredToCards = new ArrayList<>();
        if (selectedFromCard != null) {
            for (BankCard card : allToCards) {
                if (card.getId() != selectedFromCard.getId()) {
                    filteredToCards.add(card);
                }
            }
        } else {
            filteredToCards.addAll(allToCards);
        }
        toCard.setItems(FXCollections.observableArrayList(filteredToCards));
        toCard.getSelectionModel().clearSelection();
    }

    private void amount() {
        amount.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*(\\.\\d*)?")) {
                return change;
            }
            return null;
        }));
        amount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                messageTransfer.setText("");
                return;
            }
            BankCard selectedFromCard = fromCard.getValue();
            if (selectedFromCard == null) {
                return;
            }

            try {
                double enteredAmount = Double.parseDouble(newValue);
                if (enteredAmount > selectedFromCard.getBalance()) {
                    showMessage(messageTransfer, "Сумма превышает баланс карты!", "red");
                    transferMoney.setDisable(true);

                } else {
                    messageTransfer.setText("");
                    transferMoney.setDisable(false);

                }
            } catch (NumberFormatException e) {
            }
        });
    }

    @FXML
    public void transferMoney()  {
        BankCard from = fromCard.getValue();
        BankCard to = toCard.getValue();
        String amountText = amount.getText();
        try {
            double sum = Double.parseDouble(amountText);

            bankService.transferMoney(from.getId(), to.getId(), sum);
            amount.clear();

            showMessage(messageTransfer, "Перевод " + sum + " ₽ выполнен!", "green");

            loadAllFromCards();
            loadAllToCards();
            listDebitCard();
            listCreditCard();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showMessage(Label label, String text, String color) {
        label.setText(text);
        label.setStyle("-fx-text-fill: " + color + ";");

        PauseTransition pause = new PauseTransition(Duration.seconds(3));

        pause.setOnFinished(event -> {
            if (label.getText().equals(text)) {
                label.setText("");
                label.setStyle("");
            }
        });
        pause.play();
    }
}

