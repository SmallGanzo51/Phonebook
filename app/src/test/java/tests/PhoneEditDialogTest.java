package tests;

import org.junit.jupiter.api.Test;
import phonebook.model.PhoneNumber;
import phonebook.model.PhoneType;
import phonebook.ui.dialogs.PhoneEditDialog;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class PhoneEditDialogTest {

    @Test
    void testDialogCreateAndOk() {
        // Создаём диалог с новым номером
        PhoneEditDialog dlg = new PhoneEditDialog((JDialog) null, null);
        assertFalse(dlg.isOk());

        // Создаём существующий номер
        PhoneNumber pn = new PhoneNumber("123", PhoneType.MOBILE);
        PhoneEditDialog dlg2 = new PhoneEditDialog((JDialog) null, pn);
        assertFalse(dlg2.isOk());
    }
}
