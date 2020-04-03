package Datamaskin.FXML;

import Datamaskin.nyScene;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class HovedsideController {

    @FXML private Button tilSluttbrukerside;
    @FXML private Button tilSuperbrukerside;

    // metode som åpner ny scene til superbrukersiden
    @FXML void tilSluttbrukerside(ActionEvent event) throws IOException {
        Stage primaryStage = (Stage) tilSluttbrukerside.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("Sluttbrukerside.fxml"));
        primaryStage.setTitle("Hei sluttbruker");
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.show();
    }


    // metode som åpner ny scene til sluttbrukersiden
    @FXML void tilSuperbrukerside(ActionEvent event) throws IOException {
        Stage primaryStage = (Stage) tilSuperbrukerside.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("Superbrukerside.fxml"));
        nyScene.tilSuperbrukerside(primaryStage, root);
    }

}
