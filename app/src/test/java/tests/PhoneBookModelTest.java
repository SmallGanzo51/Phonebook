package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import phonebook.model.Contact;
import phonebook.model.PhoneBookModel;
import phonebook.model.PhoneType;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class PhoneBookModelTest {

    private PhoneBookModel model;

    @BeforeEach
    void setUp() {
        model = new PhoneBookModel();
        // Очистим временный файл, чтобы тесты не пересекались
        File f = new File("phonebook.dat");
        if (f.exists()) f.delete();
    }

    @Test
    void testAddAndRemoveContact() {
        Contact c = new Contact("Иванов Иван");
        model.addContact(c);
        assertEquals(1, model.getContacts().size());
        model.removeContact(0);
        assertEquals(0, model.getContacts().size());
    }

    @Test
    void testSaveAndLoad() {
        Contact c = new Contact("Петров Пётр");
        c.addPhone("111", PhoneType.HOME);
        c.note = "Заметка";
        model.addContact(c);

        model.saveToFile();

        PhoneBookModel loaded = new PhoneBookModel();
        loaded.loadFromFile();
        assertEquals(1, loaded.getContacts().size());
        Contact lc = loaded.getContacts().get(0);
        assertEquals("Петров Пётр", lc.fullName);
        assertEquals("111 (Домашний)", lc.phonesAsString());
        assertEquals("Заметка", lc.note);
    }
}
