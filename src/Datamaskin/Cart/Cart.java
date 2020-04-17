package Datamaskin.Cart;

import Datamaskin.Product.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.util.HashMap;

public class Cart {
    public transient static ObservableList<Product> Register = FXCollections.observableArrayList();

    public void attachTableview(TableView tv) {
        tv.setItems(Register);
    }

    public void addElement(Product aProduct) {
        Register.add(aProduct);
    }

    //Metode som fjerner gammelt element i handlelisten (hører til første brukerside) og legger til nytt
    public void replaceElements(int i, Product newProduct){
            Register.set(i, newProduct);
    }


    //Metode som returnerer totalsum av varer lagt til i handlekurv - Hannah
    public double getTotalPrice() {
        double totalSum = 0;
        for (Product product : Register) {
            totalSum += product.getPrice();
        }
        return totalSum;
    }


    //Metode som sletter alle elementer i handlelisten (brukes når bruker skal tilbake til hovedsiden) - Hannah
    public void deleteShoppingcart(){
        if(Register.size() != 0){
            Register.remove(0, Register.size());
        }
    }
}