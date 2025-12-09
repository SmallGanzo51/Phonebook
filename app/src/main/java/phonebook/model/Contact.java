package phonebook.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Класс, представляющий контакт в телефонной книге.
 * <p>
 * Контакт содержит:
 * <ul>
 *     <li>ФИО</li>
 *     <li>Список телефонных номеров {@link PhoneNumber}</li>
 *     <li>Заметку (опционально)</li>
 * </ul>
 */
public class Contact implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(Contact.class);

    /** Полное имя контакта */
    public String fullName;

    /** Список телефонов контакта */
    public List<PhoneNumber> phones = new ArrayList<>();

    /** Заметка к контакту */
    public String note = "";

    /**
     * Конструктор контакта с указанием полного имени.
     *
     * @param fullName полное имя контакта
     */
    public Contact(String fullName) {
        this.fullName = fullName;
        logger.debug("Создан новый контакт: {}", fullName);
    }

    /**
     * Добавляет новый телефон к контакту.
     *
     * @param number номер телефона
     * @param type тип телефона {@link PhoneType}
     */
    public void addPhone(String number, PhoneType type) {
        PhoneNumber pn = new PhoneNumber(number, type);
        phones.add(pn);
        logger.info("Добавлен телефон '{}' к контакту '{}'", pn, fullName);
    }

    /**
     * Возвращает все телефоны контакта в виде строки,
     * разделённой точкой с запятой.
     *
     * @return строка со всеми телефонами
     */
    public String phonesAsString() {
        return phones.stream()
                .map(PhoneNumber::toString)
                .collect(Collectors.joining("; "));
    }
}
