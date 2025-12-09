package phonebook.ui.dialogs;

import phonebook.model.PhoneNumber;
import phonebook.model.PhoneType;

import javax.swing.*;
import java.awt.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Диалог для добавления или редактирования одного телефонного номера.
 * Позволяет пользователю:
 * <ul>
 *     <li>Ввод номера телефона</li>
 *     <li>Выбор типа телефона (Сотовый, Домашний, Рабочий, Факс, Другое)</li>
 * </ul>
 * Диалог является модальным.
 */
public class PhoneEditDialog extends JDialog {

    private static final Logger logger = LogManager.getLogger(PhoneEditDialog.class);

    /** Поле для ввода номера телефона */
    private JTextField numberField;

    /** Комбо-бокс для выбора типа телефона */
    private JComboBox<PhoneType> typeCombo;

    /** Флаг успешного завершения диалога (true, если нажата кнопка OK) */
    private boolean ok = false;

    /** Редактируемый или созданный телефонный номер */
    private PhoneNumber phone;

    /**
     * Конструктор диалога.
     *
     * @param owner родительский диалог или окно
     * @param existing телефонный номер для редактирования,
     *                 или null, если добавляется новый номер
     */
    public PhoneEditDialog(Dialog owner, PhoneNumber existing) {
        super(owner, true);
        setTitle(existing == null ? "Добавить телефон" : "Изменить телефон");
        init(existing);
        pack();
        setLocationRelativeTo(owner);
        logger.debug("Открыт PhoneEditDialog для {}", existing == null ? "нового номера" : existing.number);
    }

    /**
     * Инициализация графических компонентов диалога и их расположения.
     *
     * @param existing телефонный номер для редактирования,
     *                 или null, если создается новый номер
     */
    private void init(PhoneNumber existing) {
        setLayout(new BorderLayout(8, 8));

        JPanel center = new JPanel(new GridLayout(2, 2, 6, 6));
        center.add(new JLabel("Номер:"));
        numberField = new JTextField(20);
        center.add(numberField);

        center.add(new JLabel("Тип:"));
        typeCombo = new JComboBox<>(PhoneType.values());
        center.add(typeCombo);

        add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton okBtn = new JButton("OK");
        JButton cancelBtn = new JButton("Отмена");
        bottom.add(okBtn);
        bottom.add(cancelBtn);
        add(bottom, BorderLayout.SOUTH);

        if (existing != null) {
            numberField.setText(existing.number);
            typeCombo.setSelectedItem(existing.type);
            logger.debug("Загружен существующий номер для редактирования: {}", existing.number);
        }

        okBtn.addActionListener(e -> saveAndClose());
        cancelBtn.addActionListener(e -> cancelAndClose());
    }

    /**
     * Сохраняет введённый номер и закрывает диалог с флагом OK.
     */
    private void saveAndClose() {
        String num = numberField.getText().trim();
        if (num.isEmpty()) {
            logger.warn("Попытка сохранить пустой номер");
            JOptionPane.showMessageDialog(this, "Номер не может быть пустым.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        PhoneType t = (PhoneType) typeCombo.getSelectedItem();
        phone = new PhoneNumber(num, t);
        ok = true;
        logger.info("Телефон сохранён: {}", num);
        dispose();
    }

    /**
     * Отменяет редактирование и закрывает диалог без сохранения.
     */
    private void cancelAndClose() {
        ok = false;
        logger.debug("PhoneEditDialog закрыт без сохранения");
        dispose();
    }

    /**
     * Возвращает результат диалога.
     *
     * @return true, если пользователь нажал кнопку OK и данные сохранены
     */
    public boolean isOk() {
        return ok;
    }

    /**
     * Возвращает созданный или отредактированный телефонный номер.
     *
     * @return объект PhoneNumber с введёнными данными
     */
    public PhoneNumber getPhone() {
        return phone;
    }
}
