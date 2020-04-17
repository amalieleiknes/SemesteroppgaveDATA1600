import Datamaskin.Customer;
import Datamaskin.Exceptions.InvalidEmailException;
import org.junit.Test;


import static org.junit.jupiter.api.Assertions.*;

public class testEmail {

    @Test
    public void testValidEmail() {
        assertTrue(Customer.validateEmail("henrik.lieng@oslomet.no"));
        assertTrue(Customer.validateEmail("example@example.com"));
        assertTrue(Customer.validateEmail("uk@domain.co.uk"));
    }

    @Test
    public void testInvalidEmail() {
        assertFalse(Customer.validateEmail(""));
        assertFalse(Customer.validateEmail("henrik.lieng"));
        assertFalse(Customer.validateEmail("@oslomet.no"));
        assertFalse(Customer.validateEmail("henrik.lieng@invalid"));
        assertFalse(Customer.validateEmail("test@"));
        assertFalse(Customer.validateEmail(";bot@evil.com"));
    }

}
