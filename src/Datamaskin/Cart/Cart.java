package Datamaskin.Cart;

import Datamaskin.Product.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

public class Cart {
    public transient static ObservableList<Product> Register = FXCollections.observableArrayList();

    public void addComponent(TableView tv) {
        tv.setItems(Register);
    }

    public void addElement(Product etProduct) {
        Register.add(etProduct);
    }

    public void deleteElements(){
        if(Register.size() != 0) {
            for (int i = 7; i > -1; i--) {
                Register.remove(i);
            }
        }
    }


    //Metode som sletter alle elementer i handlelisten (brukes når bruker skal tilbake til hovedsiden)
    public void deleteShoppingcart(){
        if(Register.size() != 0){
            Register.remove(0, Register.size());
        }
    }
}