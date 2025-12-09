package phonebook.model;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Модель телефонной книги.
 * <p>
 * Предоставляет методы для работы со списком контактов:
 * <ul>
 *     <li>Добавление и удаление контактов</li>
 *     <li>Сохранение списка контактов в файл</li>
 *     <li>Загрузка списка контактов из файла</li>
 * </ul>
 * Используется сериализация для хранения данных в бинарном файле "phonebook.dat".
 */
public class PhoneBookModel {

    private static final Logger logger = LogManager.getLogger(PhoneBookModel.class);

    /** Список контактов */
    private List<Contact> contacts = new ArrayList<>();

    /** Файл для хранения контактов */
    private final File storageFile = new File("phonebook.dat");

    /**
     * Возвращает список всех контактов.
     *
     * @return список контактов
     */
    public List<Contact> getContacts() {
        return contacts;
    }

    /**
     * Добавляет новый контакт в телефонную книгу.
     *
     * @param c контакт для добавления
     */
    public void addContact(Contact c) {
        contacts.add(c);
        logger.info("Добавлен контакт: {}", c.fullName);
    }

    /**
     * Удаляет контакт по индексу.
     *
     * @param index индекс контакта в списке
     */
    public void removeContact(int index) {
        if (index >= 0 && index < contacts.size()) {
            Contact removed = contacts.remove(index); // сохраняем удалённый контакт
            logger.info("Удалён контакт: {}", removed.fullName);
        }
    }

    /**
     * Сохраняет текущий список контактов в файл {@link #storageFile}.
     * В случае ошибки выводит сообщение через JOptionPane.
     */
    public void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storageFile))) {
            oos.writeObject(contacts);
            logger.info("База успешно сохранена в файл: {}", storageFile.getAbsolutePath());
        } catch (IOException e) {
            logger.error("Ошибка при сохранении базы: {}", e.getMessage(), e);
            JOptionPane.showMessageDialog(null, "Ошибка при сохранении: " + e.getMessage());
        }
    }

    /**
     * Загружает список контактов из файла {@link #storageFile}.
     * Если файл отсутствует, список остаётся пустым.
     * В случае ошибки выводит сообщение через JOptionPane.
     */
    @SuppressWarnings("unchecked")
    public void loadFromFile() {
        if (!storageFile.exists()) {
            logger.warn("Файл базы не найден: {}", storageFile.getAbsolutePath());
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storageFile))) {
            contacts = (List<Contact>) ois.readObject();
            logger.info("База успешно загружена из файла: {}", storageFile.getAbsolutePath());
        } catch (Exception e) {
            logger.error("Ошибка при загрузке базы: {}", e.getMessage(), e);
            JOptionPane.showMessageDialog(null, "Ошибка при загрузке: " + e.getMessage());
        }
    }
}
