package datamaskin.filbehandling;

import datamaskin.orders.FinalOrderOverview;
import datamaskin.product.Product;
import java.util.List;

public class OrderFormatter {
    private static String DELIMITER = ";";

    //metode som tar imot en liste av produkter (her regnes det som en ferdig ordre som sendes inn)
    //metoden gjør om hvert produkt i listen til en String og "appender" det til en større String.
    public static String formatListOfProductToString(List<Product> listOfProduct) {
        StringBuffer stringOfProducts = new StringBuffer();
        for (Product product : listOfProduct) {
            String formattedProduct = formatProductToString(product);
            stringOfProducts.append(formattedProduct);
            stringOfProducts.append("\n");
        }
        return stringOfProducts.toString();
    }

    //Metode som lager et produkt til en String med en DELIMITER som skiller hver attributt-verdi.
    private static String formatProductToString(Product product) {
        return product.getName() + DELIMITER + product.getDescription() +
                DELIMITER + product.getLifetime() + DELIMITER + product.getPrice() +
                DELIMITER + product.getCategory() + DELIMITER + product.getImageUri();
    }

//todo: Kan denne slettes?
/*    //Metode som gjør om en liste til en String og "appender" den til en StringBuffer.
    public static StringBuffer formatListOfFinalOrdersToString(List<FinalOrderOverview> listOfFinalOrders) {
        StringBuffer stringOfFinalOrders = new StringBuffer();
        for (FinalOrderOverview finalOrder : listOfFinalOrders) {
            String formattedFinalOrder = formatFinalOrderOverViewToString(finalOrder);
            stringOfFinalOrders.append(formattedFinalOrder);
            stringOfFinalOrders.append("\n");
        }
        return stringOfFinalOrders;
    }*/

    //Metode som lager bestilling til en String med en DELIMITER som skiller hver attributt-verdi.
    public static String formatFinalOrderOverViewToString(FinalOrderOverview finalOrder) {
        return finalOrder.getOrderID() + DELIMITER + finalOrder.getEmail() +
                DELIMITER + finalOrder.getOrderDate() + DELIMITER + finalOrder.getTotalPrice();
    }
}
