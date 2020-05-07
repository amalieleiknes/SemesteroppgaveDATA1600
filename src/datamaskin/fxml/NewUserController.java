package datamaskin.fxml;

import datamaskin.Page;
import datamaskin.filbehandling.CustomerFormatter;
import datamaskin.filbehandling.FileSaverTxt;
import datamaskin.users.Customer;
import datamaskin.users.CustomerValidator;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.nio.file.Path;
import java.nio.file.Paths;

public class NewUserController {
    @FXML private TextField txtEmail, txtPassword, txtPassword2;
    @FXML private Label lblErrorEmail, lblErrorPassword;

    private FileSaverTxt filesaver = new FileSaverTxt();

    // metode som lager en ny bruker hvis den møter kravene
    public Customer createCustomerFromGUI() {
        try{
            String email = txtEmail.getText();
            String password = txtPassword.getText();
            String password2 = txtPassword2.getText();

            // sjekker om eposten finnes fra før og om passordene er like
            if(CustomerValidator.validateAvailability(email, CustomerValidator.getCustomerList())){ // true returneres om eposten finnes fra før
                lblErrorEmail.setText("Epostadressen er allerede tilknyttet en kunde");}

            else if(!CustomerValidator.validateEmail(email)){ // false returneres om eposten er ugyldig
                lblErrorEmail.setText("Skriv inn en gyldig epostadresse");}

            // validerer epost og passord og sjekker om de er i riktig format/ lengde
            else if(!CustomerValidator.validatePassword(password)){ // false returneres om passordet er for kort
                lblErrorEmail.setText("");
                 lblErrorPassword.setText("Passordet må fylle følgende krav: Minst 3 tegn langt, uten mellomrom og uten æ, ø og å");}

            else if(!password.equals(password2)){ // false returneres om passordene er ulike
                lblErrorPassword.setText("Passordene du har skrevet inn er ikke like!");}

            // hvis epost og passord er i riktig format, og de andre if-ene ikke slår inn lages en ny kunde
            else if(CustomerValidator.validateEmail(email) && CustomerValidator.validatePassword(password) &&
                    !CustomerValidator.validateAvailability(email, CustomerValidator.getCustomerList())) {

                return new Customer(email, password);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    // når knappen trykkes kalles metoden for å lage ny bruker og for at vinduet skal lukkes hvis vellykket
    @FXML void makeNewUser(Event event) throws Exception {
        if(createCustomerFromGUI()!=null){
            Customer aCustomer = createCustomerFromGUI();

            //kode som lagrer orderen til forhåndsdefinert filsti (alle ordre samlet i csv.fil)
            Path customerPath = Paths.get("./src/Datamaskin/users/allCustomers.csv");
            String formattedCustomer = CustomerFormatter.formatCustomerToString(aCustomer);
            filesaver.appendToFile("\n", customerPath);
            filesaver.appendToFile(formattedCustomer, customerPath);
            
            //kode som gjør at man får tak i EnduserSendOrderPageController
            //og set-metodene i den klassen
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("EnduserSendOrderPage.fxml"));
            Parent parent = loader.load();
            Scene scene = new Scene(parent);

            EnduserSendOrderPageController controller = loader.getController();
            String email    = txtEmail.getText();
            String password = txtPassword.getText();
            if(!email.trim().isEmpty() && !password.trim().isEmpty()) {
                controller.setTxtPassword(password);
                controller.setTxtEmail(email);

                closeWindow(event);

                //når alt er registrert og validert, lukkes vinduet og stagen endres til den allerede
                // definerte endusersendorder-page som vi har satt som "Page.sendOrderPage".
                Stage stage = Page.sendOrderPage;
                stage.setScene(scene);
                stage.show();

            }
            controller.ifShoppingcartIsNull();
        }
    }

    @FXML void btnMakeNewUser(KeyEvent event) throws Exception {
        if (event.getCode().equals(KeyCode.ENTER)) {
            makeNewUser(event);
        }
    }

    // metode som lukker vinduet automatisk når det er laget en ny bruker
    public void closeWindow(Event event){
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
}