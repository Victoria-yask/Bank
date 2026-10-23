package ipap.bank.bankModel;
import ipap.bank.BankDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BankService {

    public void createDebitCard() throws SQLException {
        String cardNumber =  "2202";
        Random random = new Random();
        for (int i = 0; i < 12; i++) {
            cardNumber = cardNumber + random.nextInt(10);
        }

        String sql = "INSERT INTO bank_cards (card_type, card_number, balance) VALUES ('Дебетовая', ?, 0.00)";

        try (Connection connection = BankDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cardNumber);
            statement.executeUpdate();
        }
    }

    public List<BankCard> getAllDebitCards() throws SQLException {
        ArrayList<BankCard> debitCards = new ArrayList<>();
        String sql = "SELECT id, card_type, card_number, balance, is_active FROM bank_cards WHERE card_type = 'Дебетовая' AND is_active = true";
        try (Connection connection = BankDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String type = resultSet.getString("card_type");
                String number = resultSet.getString("card_number");
                double balance = resultSet.getDouble("balance");
                boolean isActive = resultSet.getBoolean("is_active");

                BankCard card = new BankCard(id, type, number, balance, isActive);
                debitCards.add(card);
            }
        }
        return debitCards;
    }

    public void closeDebitCard(long cardId) throws SQLException {
        String sql = "UPDATE bank_cards SET is_active = false WHERE id = ? AND balance = 0.00";
        try (Connection connection = BankDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, cardId);
            statement.executeUpdate();
        }
    }


    public void createCreditCard() throws SQLException {
        String cardNumber = "5381";
        Random random = new Random();
        for (int i = 0; i < 12; i++) {
            cardNumber = cardNumber + random.nextInt(10);
        }

        String sql = "INSERT INTO bank_cards (card_type, card_number, balance) VALUES ('Кредитная', ?, 10000.00)";

        try (Connection connection = BankDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cardNumber);
            statement.executeUpdate();
        }
    }

    public List<BankCard> getAllCreditCards() throws SQLException {
        ArrayList<BankCard> creditCards = new ArrayList<>();
        String sql = "SELECT id, card_type, card_number, balance, is_active FROM bank_cards WHERE card_type = 'Кредитная' AND is_active = true";

        try (Connection connection = BankDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String type = resultSet.getString("card_type");
                String number = resultSet.getString("card_number");
                double balance = resultSet.getDouble("balance");
                boolean isActive = resultSet.getBoolean("is_active");

                BankCard card = new BankCard(id, type, number, balance, isActive);
                creditCards.add(card);
            }
        }
        return creditCards;
    }

    public List<BankCard> getAllCards() throws SQLException {
        ArrayList<BankCard> allCards = new ArrayList<>();
        allCards.addAll(getAllDebitCards());
        allCards.addAll(getAllCreditCards());
        return allCards;
    }

    public void transferMoney(long fromCardId, long toCardId, double amount) throws SQLException {
        String fromCard = "UPDATE bank_cards SET balance = balance - ? WHERE id = ?";
        String toCard = "UPDATE bank_cards SET balance = balance + ? WHERE id = ?";
        String transactions = "INSERT INTO transactions (from_card_id, to_card_id, amount) VALUES (?, ?, ?)";

        try (Connection connection = BankDB.getConnection();
             PreparedStatement fromCardStmt = connection.prepareStatement(fromCard);
             PreparedStatement toCardStmt = connection.prepareStatement(toCard);
             PreparedStatement transactionsStmt = connection.prepareStatement(transactions)) {
            {
                fromCardStmt.setDouble(1, amount);
                fromCardStmt.setLong(2, fromCardId);
                fromCardStmt.executeUpdate();

                toCardStmt.setDouble(1, amount);
                toCardStmt.setLong(2, toCardId);
                toCardStmt.executeUpdate();

                transactionsStmt.setLong(1, fromCardId);
                transactionsStmt.setLong(2, toCardId);
                transactionsStmt.setDouble(3, amount);
                transactionsStmt.executeUpdate();
            }
        }
    }

    public int getDebitCardCount() throws SQLException {
        return getAllDebitCards().size();
    }

    public int getCreditCardCount() throws SQLException {
        return getAllCreditCards().size();
    }

}
