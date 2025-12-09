package tests;

import org.junit.jupiter.api.Test;
import phonebook.model.PhoneType;

import static org.junit.jupiter.api.Assertions.*;

class PhoneTypeTest {

    @Test
    void testFromString() {
        assertEquals(PhoneType.MOBILE, PhoneType.fromString("сотовый"));
        assertEquals(PhoneType.HOME, PhoneType.fromString("home"));
        assertEquals(PhoneType.WORK, PhoneType.fromString("рабочий"));
        assertEquals(PhoneType.FAX, PhoneType.fromString("fax"));
        assertEquals(PhoneType.OTHER, PhoneType.fromString("unknown"));
        assertEquals(PhoneType.OTHER, PhoneType.fromString(null));
    }

    @Test
    void testToString() {
        assertEquals("Сотовый", PhoneType.MOBILE.toString());
        assertEquals("Домашний", PhoneType.HOME.toString());
        assertEquals("Рабочий", PhoneType.WORK.toString());
        assertEquals("Факс", PhoneType.FAX.toString());
        assertEquals("Другое", PhoneType.OTHER.toString());
    }
}
