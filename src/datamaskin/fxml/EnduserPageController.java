package datamaskin.fxml;

import datamaskin.cart.Cart;
import datamaskin.product.Product;
import datamaskin.product.ProductCategories;
import datamaskin.Page;
import datamaskin.images.ImageClass;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import static datamaskin.cart.Cart.*;
import static datamaskin.product.ProductCategories.*;

public class EnduserPageController implements Initializable {
    @FXML private Button btnGoBack;
    @FXML private Button btnGoToPay;
    @FXML private Label lblTotalPrice;
    @FXML private Label lblError;
    @FXML private ImageView mainpageImageView;

    @FXML private Label lblInfoGraphicCard;
    @FXML private Label lblInfoMemorycard;
    @FXML private Label lblInfoHarddrive;
    @FXML private Label lblInfoProcessor;
    @FXML private Label lblInfoPower;
    @FXML private Label lblInfoSoundcard;
    @FXML private Label lblInfoOpticaldisk;
    @FXML private Label lblInfoColor;
    @FXML private Label lblExpectedLifetime;

    @FXML private TableView<Product> tableviewCart;
    @FXML private TableColumn<Product, String> nameColumn;
    @FXML private TableColumn<Product, String> descriptionColumn;
    @FXML private TableColumn<Product, Integer> lifetimeColumn;
    @FXML private TableColumn<Product, Double> priceColumn;

    @FXML private ComboBox<Product> cBoxGraphicCard;
    @FXML private ComboBox<Product> cBoxMemorycard;
    @FXML private ComboBox<Product> cBoxHarddrive;
    @FXML private ComboBox<Product> cBoxProcessor;
    @FXML private ComboBox<Product> cBoxPower;
    @FXML private ComboBox<Product> cBoxSoundcard;
    @FXML private ComboBox<Product> cBoxOpticaldisk;
    @FXML private ComboBox<Product> cBoxColor;

    private final ImageClass image = new ImageClass();
    private final Image homeImage = image.createImage("./src/Datamaskin/images/mainpage.png");
    private static final Cart aCart = new Cart();

    @FXML void addToCart() {
        if (comboBoxesAreEmpty()) {
            lblError.setText("Husk å velge en komponent i alle choiceboksene!");
        } else {
            lblError.setText("");
            createCartObjectsFromGUI();
            aCart.getTotalPrice(lblTotalPrice);
        }
    }

    //Kastes fordi createImage-metoden kalles
    public EnduserPageController() throws FileNotFoundException {
    }

    // metode for å slette gamle objekter og for å lage nye objekter som kommer an på valg i choiceboksene
    private void createCartObjectsFromGUI() {
        if (Cart.isEmpty()) {  // kun hvis handlekurven er tom skal det lages helt nye produkter som legges til
            createProducts();
            lblExpectedLifetime.setText(String.valueOf(findExpectedLifetime()));
        } else {
            updateCart();               // oppdater alltid handlekurven ut fra det som er i choicebox
            lblExpectedLifetime.setText(String.valueOf(findExpectedLifetime()));
        }
    }

    // sjekke om handlekurven allerede har komponenter, da må de slettes for å legge til nye komponenter som bruker vil endre til
    private void updateCart() {
        if (!Cart.isEmpty()) {
            aCart.replaceElements(findIndex("Skjermkort"), addProduct(cBoxGraphicCard.getValue().getName(), ProductCategories.GraphicCard));
            aCart.replaceElements(findIndex("Minnekort"), addProduct(cBoxMemorycard.getValue().getName(), ProductCategories.Memorycard));
            aCart.replaceElements(findIndex("Harddisk"), addProduct(cBoxHarddrive.getValue().getName(), ProductCategories.Harddrive));
            aCart.replaceElements(findIndex("Prosessor"), addProduct(cBoxProcessor.getValue().getName(), ProductCategories.Processor));
            aCart.replaceElements(findIndex("Strømforsyning"), addProduct(cBoxPower.getValue().getName(), ProductCategories.Power));
            aCart.replaceElements(findIndex("Lydkort"), addProduct(cBoxSoundcard.getValue().getName(), ProductCategories.Soundcard));
            aCart.replaceElements(findIndex("Optisk disk"), addProduct(cBoxOpticaldisk.getValue().getName(), ProductCategories.OpticalDisk));
            aCart.replaceElements(findIndex("Farge"), addProduct(cBoxColor.getValue().getName(), ProductCategories.Color));
        }
    }

    // metode som oppretter produkter fra hver choicebox (hver string representerer navnet på produktet)
    private void createProducts() {
        aCart.addElement(addProduct(cBoxGraphicCard.getValue().getName(), ProductCategories.GraphicCard));
        aCart.addElement(addProduct(cBoxMemorycard.getValue().getName(), ProductCategories.Memorycard));
        aCart.addElement(addProduct(cBoxHarddrive.getValue().getName(), ProductCategories.Harddrive));
        aCart.addElement(addProduct(cBoxProcessor.getValue().getName(), ProductCategories.Processor));
        aCart.addElement(addProduct(cBoxPower.getValue().getName(), ProductCategories.Power));
        aCart.addElement(addProduct(cBoxSoundcard.getValue().getName(), ProductCategories.Soundcard));
        aCart.addElement(addProduct(cBoxOpticaldisk.getValue().getName(), ProductCategories.OpticalDisk));
        aCart.addElement(addProduct(cBoxColor.getValue().getName(), ProductCategories.Color));
    }

    @Override public void initialize(URL url, ResourceBundle rb) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
        lifetimeColumn.setCellValueFactory(new PropertyValueFactory<>("Lifetime"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("Price"));
        aCart.attachTableview(tableviewCart);

        image.setImageView(mainpageImageView, homeImage);

        setProducts(cBoxGraphicCard, GraphicCard, lblInfoGraphicCard);
        setProducts(cBoxMemorycard, Memorycard, lblInfoMemorycard);
        setProducts(cBoxHarddrive, Harddrive, lblInfoHarddrive);
        setProducts(cBoxProcessor, Processor, lblInfoProcessor);
        setProducts(cBoxPower, Power, lblInfoPower);
        setProducts(cBoxSoundcard, Soundcard, lblInfoSoundcard);
        setProducts(cBoxOpticaldisk, OpticalDisk, lblInfoOpticaldisk);
        setProducts(cBoxColor, Color, lblInfoColor);

        // kaller metode for å velge riktige choicebokser/ sette totalpris når man går tilbake fra neste side
        if (!Cart.isEmpty()) {
            setChosenCombobox(cBoxGraphicCard, cBoxMemorycard, cBoxHarddrive, cBoxProcessor, cBoxPower, cBoxSoundcard, cBoxOpticaldisk, cBoxColor);
            aCart.getTotalPrice(lblTotalPrice);
            lblExpectedLifetime.setText(String.valueOf(findExpectedLifetime()));
        }
    }

    // metode som setter comboboxer og lbl til verdier fra liste
    public void setProducts(ComboBox<Product> cBox, ObservableList<Product> aCategory, Label infoLabel){
        cBox.setItems(aCategory);
        formatComboBoxDexcription(cBox, infoLabel);
    }

    // metode for å sjekke om alle choiceboksene er valgt
    private boolean comboBoxesAreEmpty() {
        return cBoxGraphicCard.getValue()   == null ||
                cBoxMemorycard.getValue()   == null ||
                cBoxHarddrive.getValue()    == null ||
                cBoxProcessor.getValue()    == null ||
                cBoxPower.getValue()        == null ||
                cBoxSoundcard.getValue()    == null ||
                cBoxOpticaldisk.getValue()  == null ||
                cBoxColor.getValue()        == null;
    }

    // knapp som sender bruker til neste side
    @FXML void loadPayment() throws IOException {
        ObservableList<Product> items = tableviewCart.getItems();
        if (comboBoxesAreEmpty()) {
            lblError.setText("Du har ikke valgt alle nødvendige komponenter til din datamaskin.");
        }
        if (items.isEmpty()) {
            lblError.setText("Legg til dine valgte komponenter i handlekurven for å gå videre.");
            return;
        }
        Stage primaryStage = (Stage) btnGoToPay.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("ExtraOrderEnduserPage.fxml"));
        Page.toExtraOrderEnduserPage(primaryStage, root);
    }

    // knapp som sender bruker til forrige side + advarsel om å avslutte
    @FXML void goBack() throws IOException {
        //Man får en advarsel om at hvis man går til hovedsiden, vil bestillingen avsluttes - Hannah
        boolean goBackIsConfirmed = Page.alertConfirmed("Ønsker du å avslutte din bestilling og gå til hovedsiden?");
        if (goBackIsConfirmed) {
            Stage primaryStage = (Stage) btnGoBack.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("Mainpage.fxml"));
            Page.toMainpage(primaryStage, root);
            aCart.deleteShoppingcart();
        }
    }

    //Metoder som sender bruker videre eller tilbake ved å trykke på "Enter"-knappen
    @FXML void btnAddEnter(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            addToCart();
        }
    }

    // metode for å gå tilbake til hovedsiden
    @FXML void btnGoBackEnter(KeyEvent event) throws IOException {
        if (event.getCode().equals(KeyCode.ENTER)) {
            goBack();
            }
        }

    // metode for å gå til neste side
    @FXML void btnNextPageEnter(KeyEvent event) throws IOException {
        if (event.getCode().equals(KeyCode.ENTER)) {
            loadPayment();
        }
    }
}