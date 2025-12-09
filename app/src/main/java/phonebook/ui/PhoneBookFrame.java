package phonebook.ui;

import phonebook.model.Contact;
import phonebook.model.PhoneBookModel;
import phonebook.ui.dialogs.ContactFormDialog;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Основное окно телефонного справочника.
 * Позволяет:
 * <ul>
 *     <li>Отображать список контактов в таблице</li>
 *     <li>Добавлять, редактировать и удалять контакты</li>
 *     <li>Поиск по ФИО и по номерам телефонов</li>
 *     <li>Масштабирование интерфейса через слайдер увеличения шрифта</li>
 * </ul>
 */
public class PhoneBookFrame extends JFrame {

    private static final Logger logger = LogManager.getLogger(PhoneBookFrame.class);

    private final PhoneBookModel model;
    private JTable table;
    private ContactTableModel tableModel;
    private TableRowSorter<ContactTableModel> sorter;
    private JTextField searchField;
    private JComboBox<String> searchCombo;
    private JSlider zoomSlider;

    public PhoneBookFrame(PhoneBookModel model) {
        super("Телефонный справочник");
        this.model = model;
        initUI();
        logger.debug("Главное окно PhoneBookFrame создано и инициализировано");
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JToolBar toolbar = new JToolBar();
        JButton addBtn = new JButton("Добавить");
        JButton editBtn = new JButton("Изменить");
        JButton delBtn = new JButton("Удалить");
        JButton saveBtn = new JButton("Сохранить");

        toolbar.add(addBtn);
        toolbar.add(editBtn);
        toolbar.add(delBtn);
        toolbar.addSeparator();
        toolbar.add(saveBtn);
        toolbar.addSeparator();

        searchCombo = new JComboBox<>(new String[]{"По ФИО", "По номеру"});
        searchField = new JTextField(20);
        JButton clearSearch = new JButton("Очистить");

        toolbar.add(new JLabel("Поиск: "));
        toolbar.add(searchCombo);
        toolbar.add(searchField);
        toolbar.add(clearSearch);

        add(toolbar, BorderLayout.NORTH);

        tableModel = new ContactTableModel(model.getContacts());
        table = new JTable(tableModel);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);

        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        resizeTableColumns();
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        zoomSlider = new JSlider(10, 30, 14);
        zoomSlider.setToolTipText("Масштаб (увеличение шрифта)");

        bottom.add(new JLabel("Масштаб:"), BorderLayout.WEST);
        bottom.add(zoomSlider, BorderLayout.CENTER);

        add(bottom, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> onAdd());
        editBtn.addActionListener(e -> onEdit());
        delBtn.addActionListener(e -> onDelete());

        saveBtn.addActionListener(e -> {
            model.saveToFile();
            JOptionPane.showMessageDialog(this, "База сохранена.", "OK", JOptionPane.INFORMATION_MESSAGE);
            logger.info("Контакты сохранены в файл");
        });

        clearSearch.addActionListener(e -> {
            searchField.setText("");
            applyFilter();
            logger.debug("Поиск очищен");
        });

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { applyFilter(); logger.debug("Поиск обновлен: {}", searchField.getText()); }
            public void removeUpdate(DocumentEvent e) { applyFilter(); logger.debug("Поиск обновлен: {}", searchField.getText()); }
            public void changedUpdate(DocumentEvent e) { applyFilter(); logger.debug("Поиск обновлен: {}", searchField.getText()); }
        });

        zoomSlider.addChangeListener(e -> {
            int size = zoomSlider.getValue();
            setComponentsFontSize(this.getContentPane(), size);
            table.setRowHeight(size + 12);
            resizeTableColumns();
            logger.debug("Изменен масштаб интерфейса: {}", size);
        });

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) onEdit();
            }
        });

        setComponentsFontSize(this.getContentPane(), zoomSlider.getValue());
        table.setRowHeight(zoomSlider.getValue() + 12);
    }

    private void resizeTableColumns() {
        TableColumnModel cm = table.getColumnModel();
        if (cm.getColumnCount() >= 3) {
            cm.getColumn(0).setPreferredWidth(250);
            cm.getColumn(1).setPreferredWidth(450);
            cm.getColumn(2).setPreferredWidth(150);
        }
    }

    private void applyFilter() {
        String text = searchField.getText().trim();
        int mode = searchCombo.getSelectedIndex();

        if (text.isEmpty()) {
            sorter.setRowFilter(null);
            return;
        }

        try {
            if (mode == 0) {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(text), 0));
            } else {
                sorter.setRowFilter(RowFilter.regexFilter(Pattern.quote(text), 1));
            }
        } catch (Exception ex) {
            sorter.setRowFilter(null);
            logger.warn("Ошибка при применении фильтра поиска", ex);
        }
    }

    private void onAdd() {
        ContactFormDialog dlg = new ContactFormDialog(this, null);
        dlg.setVisible(true);

        if (dlg.isOk()) {
            Contact c = dlg.getContact();
            model.addContact(c);
            tableModel.fireTableDataChanged();
            logger.info("Добавлен контакт: {}", c.fullName);
        }
    }

    private void onEdit() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this, "Выберите контакт для редактирования.", "Инфо", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(viewRow);
        Contact c = model.getContacts().get(modelRow);

        ContactFormDialog dlg = new ContactFormDialog(this, c);
        dlg.setVisible(true);

        if (dlg.isOk()) {
            tableModel.fireTableDataChanged();
            logger.info("Изменен контакт: {}", c.fullName);
        }
    }

    private void onDelete() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this, "Выберите контакт для удаления.", "Инфо", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(viewRow);
        Contact c = model.getContacts().get(modelRow);

        int ans = JOptionPane.showConfirmDialog(
                this,
                "Удалить контакт: " + c.fullName + "?",
                "Подтвердите",
                JOptionPane.YES_NO_OPTION
        );

        if (ans == JOptionPane.YES_OPTION) {
            model.removeContact(modelRow);
            tableModel.fireTableDataChanged();
            logger.info("Удален контакт: {}", c.fullName);
        }
    }

    private void setComponentsFontSize(Component comp, int size) {
        Font f = comp.getFont();
        if (f != null)
            comp.setFont(new Font(f.getName(), f.getStyle(), size));

        if (comp instanceof Container) {
            for (Component c : ((Container) comp).getComponents())
                setComponentsFontSize(c, size);
        }

        comp.revalidate();
        comp.repaint();
    }
}
