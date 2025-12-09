package phonebook;

import phonebook.model.PhoneBookModel;
import phonebook.ui.PhoneBookFrame;

import javax.swing.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Главный класс приложения телефонного справочника.
 * <p>
 * Запускает графический интерфейс в потоке Swing Event Dispatch Thread (EDT),
 * загружает сохранённую базу контактов и открывает основное окно {@link PhoneBookFrame}.
 */
public class MainFrame {

    private static final Logger logger = LogManager.getLogger(MainFrame.class);

    /**
     * Точка входа в программу.
     * <p>
     * Создаёт модель телефонной книги, загружает данные из файла и открывает главное окно.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            logger.info("Запуск приложения Телефонный справочник");
            PhoneBookModel model = new PhoneBookModel();
            model.loadFromFile();
            logger.info("База контактов загружена ({} контактов)", model.getContacts().size());
            PhoneBookFrame frame = new PhoneBookFrame(model);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            logger.info("Главное окно отображено пользователю");
        });
    }
}
