package Datamaskin.FXML;

import Datamaskin.CustomerValidator;
import Datamaskin.Exceptions.InvalidEmailException;
import Datamaskin.Page;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainpageController implements Initializable {
    @FXML private Button toEnduserPage;

    @FXML private TextField txtUsername;
    @FXML private TextField txtPassword;
    @FXML private Button toSuperuserPage;

    @FXML private TextField txtUserEmail;
    @FXML private TextField txtUserPassword;
    @FXML private Button btnUserOrders;

    @FXML private Label lblErrorEmail;


    public MainpageController() throws FileNotFoundException {
    }

    @FXML void btnUserOrders(ActionEvent event) throws IOException {
        try{
            String email = txtUserEmail.getText();

            if(!CustomerValidator.validateEmail(email)){
                throw new InvalidEmailException("Skriv inn gyldig e-postadresse");
            } else {
                Stage primaryStage = (Stage) btnUserOrders.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("UserspesificOrder.fxml"));
                Page.toUserspesificOrder(primaryStage, root);
                primaryStage.show();
            }
        }
        catch(InvalidEmailException e){
            lblErrorEmail.setText(e.getMessage());
        }
    }

    // bruke denne med try/catch for å legge til en verdi som skal brukes for å filtrere userSpecific order
    public void checkEmail() throws InvalidEmailException {
        String email = txtUserEmail.getText();
        CustomerValidator.validateEmail(email);
    }


    //Metode som gjør at du kan gå til neste side ved å trykke på "Enter-knappen" - Hannah
    @FXML
    void btnLoginEnter(KeyEvent event) throws IOException {
        if (event.getCode().equals(KeyCode.ENTER)) {
            Stage primaryStage = (Stage) toSuperuserPage.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("SuperuserPage.fxml"));
            Page.toSuperuserpage(primaryStage, root);
        }
    }

    @FXML
    void btnUserOrdersEnter(KeyEvent event) throws IOException {
        if (event.getCode().equals(KeyCode.ENTER)) {
            Stage primaryStage = (Stage) btnUserOrders.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("UserspesificOrder.fxml"));
            Page.toUserspesificOrder(primaryStage, root);
        }
    }

    @FXML
    void btnBuildEnter(KeyEvent event) throws IOException {
        if (event.getCode().equals(KeyCode.ENTER)) {
            Stage primaryStage = (Stage) toEnduserPage.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("EnduserPage.fxml"));
            Page.toEnduserPage(primaryStage, root);
        }
    }

    // metode som åpner ny scene til superbrukersiden
    @FXML void toEnduserPage(ActionEvent event) throws IOException {
        Stage primaryStage = (Stage) toEnduserPage.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("EnduserPage.fxml"));
        Page.toEnduserPage(primaryStage, root);
    }

    // metode som åpner ny scene til sluttbrukersiden
    @FXML void toSuperUserPage(ActionEvent event) throws IOException {
        String username = null;
        String password = null;
        try {
            username = txtUsername.getText();
            password = txtPassword.getText();
        } catch(Exception e){
            e.printStackTrace();
        }

        if (username.matches("admin") && password.matches("admin")) {
            try {
                Stage primaryStage = (Stage) toSuperuserPage.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("SuperuserPage.fxml"));
                Page.toSuperuserpage(primaryStage, root);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    // kode for bildene som ligger på hovedsiden
    @FXML private ImageView hardwareImageView;
    private Image hardwareImage = createImage("./src/Datamaskin/images/hardware.jpg");

    @FXML private ImageView orderImageView;
    private Image orderImage = createImage("./src/Datamaskin/images/order.png");

    @FXML private ImageView buildImageView;
    private Image hammerImage = createImage("./src/Datamaskin/images/hammer.png");

    @FXML private ImageView adminImageView;
    private Image adminImage = createImage("./src/Datamaskin/images/admin.png");

    //metode som oppretter et bilde via path og returnerer et bilde
    private Image createImage(String path) throws FileNotFoundException {
        FileInputStream imageStream = new FileInputStream(path);
        return new Image(imageStream);
    }

    //metode som kobler imageviewet med bildet - hannah
    private void setImageView(ImageView iv, Image image){
        iv.setImage(image);
    }

    //Effekt for å blurre bilde på mainpage (det blå bildet)
    private DropShadow shadowEffect = new DropShadow();

    //Bilder settes i ImageViewet når siden lastes inn, samt effekt på bildet
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setImageView(orderImageView, orderImage);
        setImageView(buildImageView, hammerImage);
        setImageView(adminImageView, adminImage);
        setImageView(hardwareImageView, hardwareImage);
        shadowEffect.setRadius(50);
        shadowEffect.setWidth(50);
        shadowEffect.setHeight(25);
        hardwareImageView.setEffect(shadowEffect);
    }
}
