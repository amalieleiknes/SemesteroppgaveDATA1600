package datamaskin.filbehandling;

import datamaskin.orders.FinalOrderOverview;
import datamaskin.product.Product;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.List;

public interface iReadFromOrderFile {
    List<Product> readFromOrderFile(String path) throws IOException;

    Product parseToProduct(String line) throws IOException;

    //Generisk metode som returnerer en type arvet av Number (Skal kunne returnere double og int
    double parseToDouble(String str, String errorMessage) throws IOException;

    int parseToInteger(String str, String errorMessage) throws IOException;
}
