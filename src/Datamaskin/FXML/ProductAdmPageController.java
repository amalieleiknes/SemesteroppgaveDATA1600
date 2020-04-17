package Datamaskin.FXML;

import Datamaskin.Exceptions.InvalidLifetimeException;
import Datamaskin.Exceptions.InvalidPriceException;
import Datamaskin.Filbehandling.SaveToBinaryFile;
import Datamaskin.Page;
import Datamaskin.Product.ProductCategories;
import Datamaskin.Product.ProductRegister;
import Datamaskin.Product.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProductAdmPageController implements Initializable{

    @FXML private Button tilSuperbrukerside;
    @FXML private Text wrongInput;

    // inputfields for å lage et produkt/ komponent
    @FXML private TextField txtComponentname;
    @FXML private TextField txtDescription;
    @FXML private TextField txtLifetime;
    @FXML private TextField txtPrice;
    @FXML private ChoiceBox<String> cboxCategory;

    @FXML private MenuButton menuDropdown;
    @FXML private MenuItem saveToFile;
    @FXML private MenuItem openFromFile;

    // metode for å lage kategoriene
    private void setData(){
        cboxCategory.getItems().addAll("Skjermkort", "Minnekort",
                "Harddisk", "Prosessor", "Strømforsyning", "Lydkort",
                "Optisk disk" , "Farge", "Andre produkter");
    }

    // konfigurerer tabellen
    @FXML private TableView<Product> componentTableview;
    @FXML private TableColumn<Product, String> nameColumn;
    @FXML private TableColumn<Product, String> descriptionColumn;
    @FXML private TableColumn<Product, String> lifetimeColumn;
    @FXML private TableColumn<Product, Integer> priceColumn;
    @FXML private TableColumn<Product, String> categoryColumn;

    // oppretter et nytt objekt av typen Produktregister
    public static ProductRegister aRegister = new ProductRegister();

    // knappen for å legge til et nytt produkt i listen
    @FXML void addComponent(ActionEvent event) {
        Product aProduct = createProductObjectFromGUI();
        if(aProduct != null) {
            aRegister.addElement(aProduct);
            emptyTextfield();
        }
    }

    // nullstiller tekstfelt når det blir lagt til et nytt produkt
    private void emptyTextfield() {
        txtComponentname.setText("");
        txtDescription.setText("");
        txtLifetime.setText("");
        txtPrice.setText("");
        wrongInput.setText("");
    }

    //metode som sjekker om tekstfeltene på adminsiden er tomme eller med white-spaces - Hannah
    private boolean isEmptyOrBlank(TextField textfield) {
        return textfield.getText().isEmpty() || textfield.getText().trim().isEmpty();
    }

    // sjekker inputfields for feil og legger til i array
    private Product createProductObjectFromGUI() {
        String name;
        String description;
        int lifetime;
        double price;
        String category;

        if (    isEmptyOrBlank(txtComponentname) ||
                isEmptyOrBlank(txtDescription)   ||
                isEmptyOrBlank(txtLifetime)      ||
                isEmptyOrBlank(txtPrice)         ||
                cboxCategory.getSelectionModel().getSelectedItem() == null) {
            wrongInput.setText("Fyll ut alle felter over.");

        } else {
            try {
                name = txtComponentname.getText();
                Product.validateName(name);

                description = txtDescription.getText();
                Product.validateDescription(description);

                lifetime = Integer.parseInt(txtLifetime.getText());
                Product.validateLifetime(lifetime);

                price = Double.parseDouble(txtPrice.getText());
                Product.validatePrice(price);

                category = cboxCategory.getSelectionModel().getSelectedItem();
                Product.validateCategory(category);

                //oppretter produktet med alle riktige attributter etter at de er sjekket for feil
                Product aProduct = new Product(name, description, lifetime, price, category);

                // metode som også legger til produktet i riktig kategori-array
                ProductCategories.setData(aProduct, category, name);

                // returnerer produktet
                return aProduct;


            } catch (InvalidPriceException | IllegalArgumentException | InvalidLifetimeException e) {
                wrongInput.setText(e.getMessage());
            }
        }
        return null;
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
        lifetimeColumn.setCellValueFactory(new PropertyValueFactory<>("Lifetime"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("Price"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("Category"));

        aRegister.leggTilKomponent(componentTableview);

        // for å ha kategoriene i nedtrekkslista fra før av
        setData();
    }

    // kode for å komme tilbake til hovedmenyen for superbruker
    @FXML void tilSuperbrukerside(ActionEvent event) throws IOException {
        Stage primaryStage = (Stage) tilSuperbrukerside.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("SuperuserPage.fxml"));
        Page.toSuperuserpage(primaryStage, root);
    }


    @FXML
    void btnAddProdEnter(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            Product aProduct = createProductObjectFromGUI();
            if(aProduct != null) {
                aRegister.addElement(aProduct);
                emptyTextfield();
            }
        }
    }

    @FXML
    void btnGoBackEnter(KeyEvent event) throws IOException {
        if (event.getCode().equals(KeyCode.ENTER)) {
            Stage primaryStage = (Stage) tilSuperbrukerside.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("SuperuserPage.fxml"));
            Page.toSuperuserpage(primaryStage, root);
        }
    }

    @FXML
    void btnMenuEnter(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            menuDropdown.show();
            menuDropdown.fire();
        }
    }


    // litt enkel filbehandling her, lagre til binære filer? og lage for ordre også
    @FXML void openFromFile(ActionEvent event) {

    }

    @FXML void saveToFile(ActionEvent event) {

        SaveToBinaryFile.readFile("Hei");
    }
}
