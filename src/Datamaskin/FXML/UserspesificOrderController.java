package Datamaskin.FXML;

import Datamaskin.Filbehandling.ReadFromOrderFile;
import Datamaskin.Product.Product;
import Datamaskin.orders.FinalOrderOverview;
import Datamaskin.Page;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class UserspesificOrderController implements Initializable {
        @FXML private Button toMainpage;
        @FXML private TableView<FinalOrderOverview> allOrders;
        @FXML private TableColumn<FinalOrderOverview, String> emailColumn;
        @FXML private TableColumn<FinalOrderOverview, String> orderIDColumn;
        @FXML private TableColumn<FinalOrderOverview, LocalDate> orderDateColumn;
        @FXML private TableColumn<FinalOrderOverview, Double> totalPriceColumn;
        @FXML private Button btnOrderDetails;

        @FXML private TableView<Product> tblOrderInfo;
        @FXML private TableColumn<Product, String> productName;
        @FXML private TableColumn<Product, String> productInfo;
        @FXML private TableColumn<Product, Integer> productLifetime;
        @FXML private TableColumn<Product, Double> productPrice;
        @FXML private TableColumn<Product, String> productCategory;

        private ReadFromOrderFile reader = new ReadFromOrderFile();

        //Metode som viser innholdet i orderen når bruker trykker på en ordre
        @FXML
        void selectedOrderItemEvent(MouseEvent event) throws IOException {
                FinalOrderOverview order = allOrders.getSelectionModel().getSelectedItem();

                if (order == null) return;
                try {
                        String path = "./src/Datamaskin/sentOrdersPath/" + order.getOrderID() + ".csv";
                        ObservableList<Product> listOfProducts = reader.readFromOrderFile(path);
                        tblOrderInfo.getItems().addAll(listOfProducts);
                        tblOrderInfo.setItems(listOfProducts);
                        productName.setCellValueFactory(new PropertyValueFactory<>("Name"));
                        productInfo.setCellValueFactory(new PropertyValueFactory<>("Description"));
                        productLifetime.setCellValueFactory(new PropertyValueFactory<>("Lifetime"));
                        productPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
                        productCategory.setCellValueFactory(new PropertyValueFactory<>("Category"));

                }catch(FileNotFoundException e){
                        System.err.println("Noe gikk galt ved innlasting av filstien" + e.getMessage());
                }
        }


        @FXML void orderDetails(ActionEvent event) {

        }

        //knappen "tilbake" tar brukeren med tilbake til menysiden for superbruker
        @FXML void toMainpage() throws IOException {
                Stage primaryStage = (Stage) toMainpage.getScene().getWindow();
                Page.toMainpage(primaryStage, FXMLLoader.load(getClass().getResource("Mainpage.fxml")));
        }

        // metoder for å legge inn ordreregisteret på denne siden
        @Override public void initialize(URL url, ResourceBundle rb) {
                emailColumn.setCellValueFactory(new PropertyValueFactory<>("Email"));
                orderIDColumn.setCellValueFactory(new PropertyValueFactory<>("OrderID"));
                orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("OrderDate"));
                totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("TotalPrice"));

                EnduserSendOrderPageController.OrderRegister.addOrder(allOrders);
        }

        // metode så man kommer til hovedsiden ved å trykke enter
        @FXML void btnToMainpageEnter(KeyEvent event) throws IOException {
        if (event.getCode().equals(KeyCode.ENTER)) {
                toMainpage();
        }


        // for å søke etter emailen til bruker skjult, så bare brukerens ordre kommer opp


    /*// kode for filtrering
    @FXML void filtreringsFelt() {
        FilteredList<Order> filtrertData = new FilteredList<>((Register), p -> true);
        String userEmail;

        //hver gang verdien endres skjer følgende
        userEmail.textProperty().addListener((observable, gammelVerdi, nyVerdi) -> {
            filtrertData.setPredicate(person -> {

                String småBokstaver = nyVerdi.toLowerCase();

                if (nyVerdi.matches("[a-zA-Z. -_0-9()@]*")) {    //

                    // Hvis feltet er tomt skal alle personer vises
                    if (nyVerdi.isEmpty()) {
                        return true;
                    }

                    // Sammenligner alle kolonner med filtertekst
                     if (person.getEpost().toLowerCase().contains(småBokstaver)) {
                        return true;
                    }
                } return false;
            });
        });

        // oppretter en sortert liste binder den sammen med tabellen
        SortedList<Order> sortertData = new SortedList<>(filtrertData);
        sortertData.comparatorProperty().bind(tabell1.comparatorProperty());

        // legger til sotrert og filtert data til tabellen
        tabell1.setItems(sortertData);
    }
*/
        }
}

