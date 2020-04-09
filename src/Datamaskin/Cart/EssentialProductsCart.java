package Datamaskin.Cart;

import Datamaskin.Product.Product;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

// her har vi med essensielle fakta for handlekurven på det første steget

public class EssentialProductsCart {
    private SimpleStringProperty name;
    private SimpleStringProperty description;
    private SimpleIntegerProperty lifetime;
    private SimpleDoubleProperty price;

    public EssentialProductsCart(Product aProduct) {
        //String[] splittetProduct = aProduct.split(";");



    }

    public String getName() {
        return name.get();
    }
    public void setName(String componentName) {
        this.name = new SimpleStringProperty(componentName);
    }

    public String getDescription() {
        return description.get();
    }
    public void setDescription(String componentDescription) {
        this.description = new SimpleStringProperty(componentDescription);
    }

    public int getLifetime() {
        return lifetime.get();
    }
    public void setLifetime(int componentLifetime) {
        this.lifetime = new SimpleIntegerProperty(componentLifetime);
    }

    public double getPrice() {
        return price.get();
    }
    public void setPrice(double componentPrice) {
        this.price = new SimpleDoubleProperty(componentPrice);
    }
}
