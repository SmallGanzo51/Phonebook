package phonebook.ui.dialogs;

import phonebook.model.Contact;
import phonebook.model.PhoneNumber;
import phonebook.model.PhoneType;

import javax.swing.*;
import java.awt.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Диалог для добавления или редактирования контакта в телефонной книге.
 * Позволяет:
 * <ul>
 *   <li>Ввод ФИО</li>
 *   <li>Добавление/редактирование/удаление телефонов</li>
 *   <li>Ввод заметки</li>
 * </ul>
 * Диалог является модальным.
 */
public class ContactFormDialog extends JDialog {

    private static final Logger logger = LogManager.getLogger(ContactFormDialog.class);

    /** Поле для ввода ФИО контакта */
    private JTextField nameField;

    /** Поле для ввода заметки контакта */
    private JTextArea noteArea;

    /** Модель списка телефонов */
    private DefaultListModel<PhoneNumber> phoneListModel;

    /** Список телефонов контакта */
    private JList<PhoneNumber> phoneList;

    /** Флаг успешного завершения (OK) */
    private boolean ok = false;

    /** Редактируемый контакт (null если добавляем новый) */
    private Contact contact;

    /**
     * Конструктор диалога.
     *
     * @param owner родительское окно
     * @param contactToEdit контакт для редактирования, null если добавляем новый
     */
    public ContactFormDialog(Frame owner, Contact contactToEdit) {
        super(owner, true);
        this.contact = contactToEdit;
        setTitle(contact == null ? "Добавить контакт" : "Редактировать контакт");
        init();
        pack();
        setLocationRelativeTo(owner);
        logger.debug("Открыт ContactFormDialog для {}", contact == null ? "нового контакта" : contact.fullName);
    }

    /**
     * Инициализация GUI компонентов и их расположения.
     */
    private void init() {
        setLayout(new BorderLayout(8, 8));

        JPanel top = new JPanel(new BorderLayout(4, 4));
        top.add(new JLabel("ФИО:"), BorderLayout.WEST);
        nameField = new JTextField(30);
        top.add(nameField, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        phoneListModel = new DefaultListModel<>();
        phoneList = new JList<>(phoneListModel);
        phoneList.setVisibleRowCount(6);
        JScrollPane phoneScroll = new JScrollPane(phoneList);

        JButton addPhone = new JButton("Добавить телефон");
        JButton editPhone = new JButton("Изменить");
        JButton delPhone = new JButton("Удалить");

        JPanel phoneButtons = new JPanel();
        phoneButtons.setLayout(new BoxLayout(phoneButtons, BoxLayout.Y_AXIS));
        phoneButtons.add(addPhone);
        phoneButtons.add(Box.createVerticalStrut(6));
        phoneButtons.add(editPhone);
        phoneButtons.add(Box.createVerticalStrut(6));
        phoneButtons.add(delPhone);

        JPanel center = new JPanel(new BorderLayout(6, 6));
        center.add(new JLabel("Телефоны:"), BorderLayout.NORTH);
        center.add(phoneScroll, BorderLayout.CENTER);
        center.add(phoneButtons, BorderLayout.EAST);
        add(center, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(4, 4));

        noteArea = new JTextArea(3, 40);
        noteArea.setLineWrap(true);
        noteArea.setWrapStyleWord(true);
        JScrollPane noteScroll = new JScrollPane(noteArea);

        JPanel notePanel = new JPanel(new BorderLayout());
        notePanel.add(new JLabel("Заметка:"), BorderLayout.NORTH);
        notePanel.add(noteScroll, BorderLayout.CENTER);

        bottomPanel.add(notePanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton okBtn = new JButton("OK");
        JButton cancelBtn = new JButton("Отмена");
        buttonPanel.add(okBtn);
        buttonPanel.add(cancelBtn);

        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        if (contact != null) {
            nameField.setText(contact.fullName);
            noteArea.setText(contact.note);
            for (PhoneNumber p : contact.phones) phoneListModel.addElement(p);
            logger.debug("Загружены данные контакта для редактирования: {}", contact.fullName);
        }

        // ---------- Действия кнопок ----------
        addPhone.addActionListener(e -> {
            logger.debug("Нажата кнопка 'Добавить телефон'");
            PhoneEditDialog ped = new PhoneEditDialog(this, null);
            ped.setVisible(true);
            if (ped.isOk()) {
                phoneListModel.addElement(ped.getPhone());
                logger.info("Телефон добавлен: {}", ped.getPhone().number);
            }
        });

        editPhone.addActionListener(e -> {
            int idx = phoneList.getSelectedIndex();
            if (idx < 0) {
                logger.warn("Попытка редактировать телефон без выбора");
                JOptionPane.showMessageDialog(this, "Выберите телефон для редактирования.", "Инфо", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            PhoneNumber pn = phoneListModel.get(idx);
            PhoneEditDialog ped = new PhoneEditDialog(this, pn);
            ped.setVisible(true);
            if (ped.isOk()) {
                phoneListModel.set(idx, ped.getPhone());
                logger.info("Телефон изменён: {} -> {}", pn.number, ped.getPhone().number);
            }
        });

        delPhone.addActionListener(e -> {
            int idx = phoneList.getSelectedIndex();
            if (idx >= 0) {
                PhoneNumber pn = phoneListModel.get(idx);
                phoneListModel.remove(idx);
                logger.info("Телефон удалён: {}", pn.number);
            }
        });

        okBtn.addActionListener(e -> saveAndClose());
        cancelBtn.addActionListener(e -> cancelAndClose());
    }

    /**
     * Сохраняет введённые данные в контакт и закрывает диалог с флагом OK.
     */
    private void saveAndClose() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            logger.warn("Попытка сохранить контакт с пустым ФИО");
            JOptionPane.showMessageDialog(this, "ФИО не может быть пустым.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (contact == null) {
            contact = new Contact(name);
            logger.info("Создан новый контакт: {}", name);
        } else {
            logger.info("Редактируется контакт: {}", contact.fullName);
            contact.fullName = name;
        }

        // Сохраняем телефоны
        contact.phones.clear();
        for (int i = 0; i < phoneListModel.size(); i++) {
            contact.phones.add(phoneListModel.get(i));
        }

        // Сохраняем заметку
        contact.note = noteArea.getText().trim();
        logger.debug("Сохранена заметка: {}", contact.note);

        ok = true;
        logger.debug("Контакт сохранён и диалог закрыт: {}", contact.fullName);
        dispose();
    }

    /**
     * Отменяет редактирование и закрывает диалог без сохранения.
     */
    private void cancelAndClose() {
        ok = false;
        logger.debug("Закрыт ContactFormDialog без сохранения");
        dispose();
    }

    /**
     * Возвращает результат диалога.
     *
     * @return true, если пользователь нажал OK и данные сохранены
     */
    public boolean isOk() {
        return ok;
    }

    /**
     * Возвращает контакт с введёнными данными.
     *
     * @return объект Contact, заполненный из формы
     */
    public Contact getContact() {
        return contact;
    }
}
