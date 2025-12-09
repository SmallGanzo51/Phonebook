package tests;

import org.junit.jupiter.api.Test;
import phonebook.model.Contact;
import phonebook.model.PhoneType;

import static org.junit.jupiter.api.Assertions.*;

class ContactTest {

    @Test
    void testAddPhone() {
        Contact c = new Contact("Иванов Иван");
        c.addPhone("123456", PhoneType.MOBILE);
        assertEquals(1, c.phones.size());
        assertEquals("123456 (Сотовый)", c.phonesAsString());
    }

    @Test
    void testNote() {
        Contact c = new Contact("Петров Пётр");
        c.note = "Тестовая заметка";
        assertEquals("Тестовая заметка", c.note);
    }
}
