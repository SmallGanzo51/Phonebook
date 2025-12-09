package tests;

import org.junit.jupiter.api.Test;
import phonebook.model.PhoneNumber;
import phonebook.model.PhoneType;

import static org.junit.jupiter.api.Assertions.*;

class PhoneNumberTest {

    @Test
    void testToString() {
        PhoneNumber pn = new PhoneNumber("123", PhoneType.WORK);
        assertEquals("123 (Рабочий)", pn.toString());
    }
}
