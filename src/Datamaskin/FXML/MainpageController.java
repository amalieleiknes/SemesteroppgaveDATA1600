package Datamaskin.FXML;

import Datamaskin.CustomerValidator;
import Datamaskin.Exceptions.InvalidEmailException;
import Datamaskin.Page;

import Datamaskin.images.ImageClass;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainpageController implements Initializable {
    @FXML private Button btnEnduserPage;
    @FXML private TextField txtUsername;
    @FXML private TextField txtPassword;
    @FXML private Button btnSuperuserPage;
    @FXML private TextField txtUserEmail;
    @FXML private TextField txtUserPassword;
    @FXML private Button btnUserOrders;
    @FXML private Label lblErrorEmail;


    private ImageClass image = new ImageClass();

    //Dette kastes pga bildene som lastes inn når siden lastes inn. Skal sjekke om det kan fjernes evt  - Hannah
    public MainpageController() throws FileNotFoundException {
    }

    //Todo: SKal denne brukes noe sted? - Hannah
    // bruke denne med try/catch for å legge til en verdi som skal brukes for å filtrere userSpecific order
    public void checkEmail() throws InvalidEmailException {
        String email = txtUserEmail.getText();
        CustomerValidator.validateEmail(email);
    }

    @FXML
    void toUserOrders() throws IOException {
        try {
            String email = txtUserEmail.getText();
            if (!CustomerValidator.validateEmail(email)) {
                throw new InvalidEmailException("Skriv inn gyldig e-postadresse");
            } else {
                Stage primaryStage = (Stage) btnUserOrders.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("UserspesificOrder.fxml"));
                Page.toUserspesificOrder(primaryStage, root);
                primaryStage.show();
            }
        } catch (InvalidEmailException e) {
            lblErrorEmail.setText(e.getMessage());
            }
    }

    // metode som åpner ny scene til superbrukersiden
    @FXML
    void toEnduserPage() throws IOException {
        Stage primaryStage = (Stage) btnEnduserPage.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("EnduserPage.fxml"));
        Page.toEnduserPage(primaryStage, root);
        primaryStage.show();
    }

    // metode som åpner ny scene til sluttbrukersiden (endret versjon av metoden) - hannah
    @FXML
    void toSuperUserPage() throws IOException {
        try {
           String username = txtUsername.getText();
           String password = txtPassword.getText();
            if(username == null || password == null){
                throw new NullPointerException("Noe gikk galt ved innlasting av brukernavn/passord");
            } else{
                boolean isMatching = username.matches("admin") && password.matches("admin");
                if(isMatching){
                    Stage primaryStage = (Stage) btnSuperuserPage.getScene().getWindow();
                    Parent root = FXMLLoader.load(getClass().getResource("SuperuserPage.fxml"));
                    Page.toSuperuserpage(primaryStage, root);
                    primaryStage.show();
                }
            }
        }catch(NullPointerException npe){
            System.err.println(npe.getMessage());
        }
    }

    @FXML
    void btnUserOrdersEnter(KeyEvent event) throws IOException {
        if (event.getCode().equals(KeyCode.ENTER)) {
            toUserOrders();
        }
    }
    @FXML
    void btnEnduserPageEnter(KeyEvent event) throws IOException {
        if (event.getCode().equals(KeyCode.ENTER)) {
            toEnduserPage();
        }
    }
    @FXML
    void btnSuperUserPage(KeyEvent event) throws IOException {
        if (event.getCode().equals(KeyCode.ENTER)) {
            toSuperUserPage();
        }
    }

    // kode for bildene som ligger på hovedsiden
    @FXML private ImageView hardwareImageView;
    private Image hardwareImage = image.createImage("./src/Datamaskin/images/hardware.jpg");

    @FXML private ImageView orderImageView;
    private Image orderImage = image.createImage("./src/Datamaskin/images/order.png");

    @FXML private ImageView buildImageView;
    private Image hammerImage = image.createImage("./src/Datamaskin/images/hammer.png");

    @FXML private ImageView adminImageView;
    private Image adminImage = image.createImage("./src/Datamaskin/images/admin.png");

    //Effekt for å blurre bilde på mainpage (det blå bildet)
    private DropShadow shadowEffect = new DropShadow();

    //Bilder settes i ImageViewet når siden lastes inn, samt effekt på bildet
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        image.setImageView(orderImageView, orderImage);
        image.setImageView(buildImageView, hammerImage);
        image.setImageView(adminImageView, adminImage);
        image.setImageView(hardwareImageView, hardwareImage);
        shadowEffect.setRadius(50);
        shadowEffect.setWidth(50);
        shadowEffect.setHeight(25);
        hardwareImageView.setEffect(shadowEffect);
    }
}
