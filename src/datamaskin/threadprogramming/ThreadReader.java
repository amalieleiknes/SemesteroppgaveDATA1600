package datamaskin.threadprogramming;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import datamaskin.product.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ThreadReader extends Task<ObservableList<Product>> {
    private final String path;

    public ThreadReader(String path){
        this.path = path;
    }

    @Override
    protected ObservableList<Product> call() throws Exception {
        ObservableList<Product> listOfProducts = FXCollections.observableArrayList();

        try {
            Thread.sleep(1500);
            BufferedReader reader = Files.newBufferedReader(Paths.get(path));
            String line = reader.readLine();

            while (line!= null) {
                listOfProducts.add(parseToProduct(line));
                line = reader.readLine();
            }
        } catch (InterruptedException | IOException e) {
            throw new IOException("Noe gikk galt ved henting av informasjon: " + e.getMessage());
        }
        return listOfProducts;
    }

    private Product parseToProduct(String line) throws IOException {
        String[] split = line.split(";");
        if(split.length != 5){
            throw new IOException("Ikke riktig bruk av delimiter");
        }

        String productName      = split[0];
        String produtInfo       = split[1];
        int producLifetime      = parseToInteger(split[2], "Levetid er ikke et tall.");
        double productPrice     = parseToDouble(split[3], "Prisen er ikke et tall.");
        String productCategory  = split[4];
        return new Product(productName, produtInfo, producLifetime, productPrice, productCategory);
    }

    //Todo: har prøvd å lage disse to metodene under til generiske metoder. Får prøve igjen senere
    private double parseToDouble(String str, String errorMessage) throws IOException {
        double stringToDouble;
        try{
            stringToDouble = Double.parseDouble(str);
        } catch (NumberFormatException e){
            throw new IOException("Ikke gyldig tall");
        }
        return stringToDouble;
    }

    private int parseToInteger(String str, String errorMessage) throws IOException {
        int stringToInt;
        try{
            stringToInt = Integer.parseInt(str);
        } catch (NumberFormatException e){
            throw new IOException("Ikke gyldig tall");
        }
        return stringToInt;
    }

}