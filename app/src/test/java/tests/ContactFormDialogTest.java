package tests;

import org.junit.jupiter.api.Test;
import phonebook.model.Contact;
import phonebook.ui.dialogs.ContactFormDialog;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class ContactFormDialogTest {

    @Test
    void testDialogCreate() {
        ContactFormDialog dlg = new ContactFormDialog((JFrame) null, null);
        assertFalse(dlg.isOk());

        Contact c = new Contact("Иванов");
        ContactFormDialog dlg2 = new ContactFormDialog((JFrame) null, c);
        assertFalse(dlg2.isOk());
    }
}
