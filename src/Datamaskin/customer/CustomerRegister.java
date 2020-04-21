package Datamaskin.customer;

import Datamaskin.Exceptions.InvalidEmailException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

public class CustomerRegister {
    public transient static ObservableList<Customer> CustomerRegister = FXCollections.observableArrayList();

    public void setCustomerToTV(TableView tv) {
        tv.setItems(CustomerRegister);
    }

    public void addElement(Customer aCustomer) {
        CustomerRegister.add(aCustomer);
    }


    public static void setExampleCustomers (){
        Customer customer1 = new Customer("bruker@bruker.no", "bruker");
        Customer customer2 = new Customer("eksempel@eksempel.no", "eksempel");

        CustomerRegister.addAll(customer1, customer2);
    }


    // kaller på denne metoden som sjekker email+passord match i array og returnerer email hvis den får en match
    public static String checkCredentials(String email, String password) throws InvalidEmailException {
        for(Customer aCustomer : CustomerRegister){
            if(email.equals(aCustomer.getEmail()) && password.equals(aCustomer.getPassword())){
                return aCustomer.getEmail();
            }
        }
        return null;
    }


}
