package ipap.bank;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import java.io.IOException;

public class BankApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BankApplication.class.getResource("bank.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("");
        stage.getIcons().add(new Image(BankApplication.class.getResourceAsStream("bank_78392.png")));
        stage.setScene(scene);
        stage.show();
    }
}
