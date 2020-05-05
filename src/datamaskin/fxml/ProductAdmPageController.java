package datamaskin.fxml;

import datamaskin.filbehandling.binarysaving.FileHandler;
import datamaskin.exceptions.ConvertersWithErrorHandling;
import datamaskin.exceptions.InvalidLifetimeException;
import datamaskin.exceptions.InvalidPriceException;
import datamaskin.Page;
import datamaskin.product.*;
import datamaskin.threadprogramming.ThreadReaderBinary;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;
import static datamaskin.product.ProductCategories.*;

public class ProductAdmPageController implements Initializable {
    @FXML private Button toSuperUserPage;
    @FXML private Label wrongInput;
    @FXML private Button btnAddComponent;
    @FXML private Button btnDeleteComponent;

    private final ConvertersWithErrorHandling.IntegerStringConverter intStrConverter
            = new ConvertersWithErrorHandling.IntegerStringConverter();
    private final ConvertersWithErrorHandling.DoubleFromStringConverter doubleStrConverter
            = new ConvertersWithErrorHandling.DoubleFromStringConverter();

    private Stage stage;

    @FXML private ThreadReaderBinary threadReaderBinaryTask;
    @FXML private TextField txtComponentname;
    @FXML private TextField txtDescription;
    @FXML private TextField txtLifetime;
    @FXML private TextField txtPrice;
    @FXML private TextField txtSearch;
    @FXML private ChoiceBox<String> cboxCategory;
    @FXML private ComboBox<String> cBoxFilter;
    @FXML private MenuButton menuDropdown;

    @FXML private Text txtInfoMessage;
    @FXML private TableView<Product> componentTableview;
    @FXML private TableColumn<Product, String> nameColumn;
    @FXML private TableColumn<Product, String> descriptionColumn;
    @FXML private TableColumn<Product, Integer> lifetimeColumn;
    @FXML private TableColumn<Product, Double> priceColumn;
    @FXML private TableColumn<Product, String> categoryColumn;

    public static ProductRegister aRegister = new ProductRegister();

    // Setter data i combobox - må velge en av disse kategoriene for å opprette produkter
    private void setData() {
        cboxCategory.getItems().addAll("Skjermkort", "Minnekort",
                "Harddisk", "Prosessor", "Strømforsyning", "Lydkort",
                "Optisk disk", "Farge", "Andre produkter");
    }

    // knappen for å legge til et nytt produkt i listen
    @FXML void addComponent() {
        Product aProduct = createProductObjectFromGUI();
        if (aProduct != null) {
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
        String lifetimeString;
        int lifetime;
        String priceString;
        double price;
        String category;
        //Todo: Denne er satt til "missing-Image" enn så lenge, til vi får lagt til at admin
        //todo: kan velge selv hvilket bilde som skal være når de oppretter nye komponenter
        String imageUri = "./src/Datamaskin/images/missingImage.png";

        if (isEmptyOrBlank(txtComponentname) ||
                isEmptyOrBlank(txtDescription) ||
                isEmptyOrBlank(txtLifetime) ||
                isEmptyOrBlank(txtPrice) ||
                cboxCategory.getSelectionModel().getSelectedItem() == null) {
            wrongInput.setText("Fyll ut alle felter over.");

        } else {
            try {
                name = txtComponentname.getText();
                description = txtDescription.getText();
                lifetimeString = txtLifetime.getText();
                priceString = txtPrice.getText();
                category = cboxCategory.getSelectionModel().getSelectedItem();

                if (!ProductValidator.validateName(name)) {
                    throw new IllegalArgumentException("Skriv inn et gyldig komponentnavn");
                }
                if (!ProductValidator.validateDescription(description)) {
                    throw new IllegalArgumentException("Skriv inn en gyldig beskrivelse");
                }
                if (!ProductValidator.validateLifetime(lifetimeString)) {
                    throw new InvalidLifetimeException("Skriv inn et gyldig antall år (1-35)");
                } else {
                    lifetime = Integer.parseInt(txtLifetime.getText());
                }
                if (!ProductValidator.validatePrice(priceString)) {
                    throw new InvalidPriceException("Skriv inn en gyldig pris (0.01-99 999.99)");
                } else {
                    price = Double.parseDouble(txtPrice.getText());
                }

                if (!ProductValidator.validateCategory(category)) {
                    throw new IllegalArgumentException("Vennligst velg kategori");
                }

                //oppretter produktet med alle riktige attributter etter at de er sjekket for feil
                Product aProduct = new Product(name, description, lifetime, price, category, imageUri);

                // metode som også legger til produktet i riktig kategori-array
                ProductCategories.setData(aProduct, category);

                // returnerer produktet
                return aProduct;

            } catch (InvalidPriceException | IllegalArgumentException | InvalidLifetimeException e) {
                wrongInput.setText(e.getMessage());
            }
        }
        return null;
    }

    // knapp for å slette komponent fra produktlista
    @FXML void btnDeleteComponentEnter(KeyEvent event) throws IOException {
        if (event.getCode().equals(KeyCode.ENTER)) {
            deleteComponent();
        }
    }

    @FXML void deleteComponent() throws IOException {
        Product deleteItem = componentTableview.getSelectionModel().getSelectedItem();
        if (deleteItem != null) {
            boolean deleteConfirmed = Page.alertConfirmed("Ønsker du å slette " + deleteItem.getName() + " fra listen?");
            if (deleteConfirmed) {
                ProductRegister.deleteElement(deleteItem);

                // her slettes elementet også fra arrayet
                deleteFromRegister(deleteItem);
            }
        }
    }

    // elementet som slettes i TV slettes fra riktig array/ hashmap så det ikke kommer opp i choiceboksene hos sluttbruker
    private void deleteFromRegister(Product aProduct) {
        if (GraphicCard.contains(aProduct)) {
            GraphicCard.remove(aProduct);
        } else if (Memorycard.contains(aProduct)) {
            Memorycard.remove(aProduct);
        } else if (Harddrive.contains(aProduct)) {
            Harddrive.remove(aProduct);
        } else if (Processor.contains(aProduct)) {
            Processor.remove(aProduct);
        } else if (Power.contains(aProduct)) {
            Power.remove(aProduct);
        } else if (Soundcard.contains(aProduct)) {
            Soundcard.remove(aProduct);
        } else if (OpticalDisk.contains(aProduct)) {
            OpticalDisk.remove(aProduct);
        } else if (Color.contains(aProduct)) {
            Color.remove(aProduct);
        } else if (OtherProducts.contains(aProduct)) {
            OtherProducts.remove(aProduct);
        }
    }

    @Override public void initialize(URL url, ResourceBundle rb) {
        lifetimeColumn.setCellFactory(TextFieldTableCell.forTableColumn(intStrConverter));
        priceColumn.setCellFactory(TextFieldTableCell.forTableColumn(doubleStrConverter));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        categoryColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
        lifetimeColumn.setCellValueFactory(new PropertyValueFactory<>("Lifetime"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("Price"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("Category"));

        ProductRegister.setComponentToTV(componentTableview);

        ObservableList<String> filterChoices = FXCollections.observableArrayList();
        filterChoices.addAll("Navn", "Kategori", "Levetid", "Pris");

        cBoxFilter.setItems(filterChoices);
        cBoxFilter.setValue("Kategori");

        // for å ha kategoriene i nedtrekkslista fra før av
        setData();
    }

    // metode for å legge til produktet ved å trykke enter
    @FXML void btnAddProdEnter(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            addComponent();
        }
    }

    // kode for å komme tilbake til hovedmenyen for superbruker
    @FXML void toSuperUserPage() throws IOException {
        if (ProductRegister.allCategoriesArePresent()) {
            Stage primaryStage = (Stage) toSuperUserPage.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("SuperuserPage.fxml"));
            Page.toSuperuserpage(primaryStage, root);
        } else {
            Page.simpleAlertInformation("Du kan ikke forlate siden enda. Det må være minst èn komponent " +
                    "i hver kategori før du kan gå tilbake til hovedsiden.");
        }
    }

    @FXML void btnGoBackEnter(KeyEvent event) throws IOException {
        if (event.getCode().equals(KeyCode.ENTER)) {
            toSuperUserPage();
        }
    }

    //metode for å samle alle elementene som arver fra Control i en liste
    private Control[] generateListOfControlElements(){
        return new Control[]{
            menuDropdown, txtComponentname, txtDescription, txtLifetime,
            txtPrice, txtSearch, cboxCategory, cBoxFilter, componentTableview,
            btnAddComponent, btnDeleteComponent, toSuperUserPage};
    }

    //metode som leser fra binær fil med tråd - hannah
    @FXML void openFromFile(ActionEvent event) {
        Path filePathToRead = FileHandler.getFilePathToJobj(stage);
        if (filePathToRead != null) {
            txtInfoMessage.setText("Laster inn valgt fil....");
            ProductRegister.clearTableView(componentTableview);
            threadReaderBinaryTask = new ThreadReaderBinary(filePathToRead);
            threadReaderBinaryTask.setOnSucceeded(this::threadDoneReadingBinary);
            threadReaderBinaryTask.setOnFailed(this::threadFailedReadingBinary);
            Thread thread = new Thread(threadReaderBinaryTask);
            thread.start();
            txtSearch.setText("");

            toggleElements(generateListOfControlElements(),true);
        }
    }

    //Metode som setter disabled på elementer
    private void toggleElements(Control[] elementList, boolean isDisabled){
        //Les om denne her https://www.geeksforgeeks.org/arraylist-foreach-method-in-java/
        //Todo: for å bruke lambda, er man nødt til å ha List<Control> og ikke primitiv liste Control[]
        //todo: Usikker på om vi skal gå for denne metoden, eller lambda

        //elementList.forEach(element -> element.setDisable(isDisabled));

        for(Control element : elementList){
            element.setDisable(isDisabled);
        }

    }

    //når tråden er ferdig med oppgaven, gjør den dette:
    private void threadDoneReadingBinary(WorkerStateEvent event) {
        txtInfoMessage.setText("");
        if (ProductRegister.ProductRegister.isEmpty()) {
            txtInfoMessage.setText("Filen du lastet inn inneholder ingen produkter.");
        }
        toggleElements(generateListOfControlElements(), false);
        ProductRegister.setComponentToTV(componentTableview);
    }

    //hvis tråden feilet i oppgaven (lese fra binær fil)
    private void threadFailedReadingBinary(WorkerStateEvent event) {
        ProductRegister.clearTableView(componentTableview);
        toggleElements(generateListOfControlElements(), false);
        txtInfoMessage.setText("Det oppsto en feil. Kunne ikke hente ut ordreoversikt.");
        System.out.println("Feil i henting av binær fil. Kunne ikke lese");
    }

    //lagre til binær fil
    @FXML void saveToFile(ActionEvent event) {
        FileHandler.saveFile(stage, aRegister);
    }

    //Metoder slik at innholdet i Produkt-tableViewet på adminsiden endres direkte i tblViewet
    @FXML void txtProductNameEdited(TableColumn.CellEditEvent<Product, String> event) {
        if (ConvertersWithErrorHandling.isInputNotNull(event.getNewValue())) {
            event.getRowValue().setName(event.getNewValue());
        }
        componentTableview.refresh();
    }

    @FXML void txtProductDescriptionEdited(TableColumn.CellEditEvent<Product, String> event) {
        if (ConvertersWithErrorHandling.isInputNotNull(event.getNewValue())) {
            event.getRowValue().setDescription(event.getNewValue());
        }
        componentTableview.refresh();
    }

    @FXML void txtProductLifetimeEdited(TableColumn.CellEditEvent<Product, Integer> event) {
        if (intStrConverter.getSuccessfulIntValue()) {
            event.getRowValue().setLifetime(event.getNewValue());
        }
        componentTableview.refresh();
    }

    @FXML
    void txtProductPriceEdited(TableColumn.CellEditEvent<Product, Double> event) {
        if (doubleStrConverter.getSuccessfulDoubleValue()) {
            event.getRowValue().setPrice(event.getNewValue());
        }
        componentTableview.refresh();
    }

    @FXML void txtProductCategoryEdited(TableColumn.CellEditEvent<Product, String> event) {
        if (ConvertersWithErrorHandling.isCategoryMatchingInput(event.getNewValue())) {
            String string = event.getNewValue().toLowerCase();
            String input = string.substring(0, 1).toUpperCase() + string.substring(1);
            event.getRowValue().setCategory(input);
        }
        componentTableview.refresh();
    }

    // kode for filtrering
    @FXML private void filterChoiceChanged() throws IOException {
        filter();
    }

    @FXML private void searchTxtEntered() throws IOException {
        filter();
    }

    private void filter() throws IOException {
        // oppretter en ny liste for filtrert data med alle produktene
        FilteredList<Product> filteredData = new FilteredList<>((ProductRegister.ProductRegister), p -> true);

        // hver gang verdien i søkefeltet endres skjer følgende
        txtSearch.textProperty().addListener((observable, oldVerdi, newVerdi) -> {

            // listen med filtrert data sjekker gjennom produktlisten og ser om den finner et produkt som matcher
            filteredData.setPredicate(aProduct -> {

                String smallLetters = newVerdi.toLowerCase();       // henter den nye verdien og gjør den om til små bokstaver
                if (newVerdi.matches("[a-zA-Z-æøå. -_0-9()@]*")) {
                    if (newVerdi.isEmpty()) {                       // Hvis feltet er tomt skal alle personer vises
                        return true;
                    }

                    // Sammenligner alle kolonner med filtertekst, etter valgt cbox
                    if (cBoxFilter.getValue().toLowerCase().equals("navn")) {
                        if (aProduct.getName().toLowerCase().contains(smallLetters)) {
                            return true;
                        }

                    }
                    if (cBoxFilter.getValue().toLowerCase().equals("kategori")) {
                        if (aProduct.getCategory().toLowerCase().contains(smallLetters)) {
                            return true;
                        }
                    }
                    if (cBoxFilter.getValue().toLowerCase().equals("levetid")) {
                        if (String.valueOf(aProduct.getLifetime()).startsWith(smallLetters)) {
                            if (String.valueOf(aProduct.getLifetime()).matches(smallLetters)) {
                                return true;
                            }
                        }
                    }
                    if (cBoxFilter.getValue().toLowerCase().equals("pris")) {
                        if (String.valueOf(aProduct.getPrice()).startsWith(smallLetters)) {
                            if (smallLetters.endsWith(".0")) {
                                if (String.valueOf(aProduct.getPrice()).matches(smallLetters)) {
                                    return true;
                                }
                            } else {
                                if (String.valueOf(aProduct.getPrice()).matches(smallLetters + ".0")) {
                                    return true;
                                }
                            }
                        }
                    }
                }
                return false;
            });
        });

        // oppretter en sortert liste fra filtrert data og binder den sammen med tabellen
        SortedList<Product> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(componentTableview.comparatorProperty());
        componentTableview.setItems(sortedData);
    }
}