package phonebook.model;

import java.io.Serial;
import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Класс, представляющий один телефонный номер контакта.
 * <p>
 * Содержит сам номер и его тип {@link PhoneType}.
 */
public class PhoneNumber implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LogManager.getLogger(PhoneNumber.class);

    /** Номер телефона в виде строки */
    public String number;

    /** Тип телефона */
    public PhoneType type;

    /**
     * Конструктор телефонного номера.
     *
     * @param number номер телефона
     * @param type тип телефона {@link PhoneType}
     */
    public PhoneNumber(String number, PhoneType type) {
        this.number = number;
        this.type = type;
        logger.debug("Создан PhoneNumber: {} ({})", number, type);
    }

    /**
     * Возвращает строковое представление телефонного номера
     * в формате "номер (тип)".
     *
     * @return строковое представление телефонного номера
     */
    @Override
    public String toString() {
        return number + " (" + type.toString() + ")";
    }
}
