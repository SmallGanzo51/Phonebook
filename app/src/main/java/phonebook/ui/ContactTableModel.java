package phonebook.ui;

import phonebook.model.Contact;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Модель таблицы для отображения списка контактов в JTable.
 * Предоставляет данные для столбцов:
 * <ul>
 *     <li>ФИО</li>
 *     <li>Телефоны</li>
 *     <li>Заметка</li>
 * </ul>
 * Использует список объектов {@link Contact} в качестве источника данных.
 */
public class ContactTableModel extends AbstractTableModel {

    /** Названия столбцов таблицы */
    private final String[] cols = {"ФИО", "Телефоны", "Заметка"};

    /** Список контактов, отображаемых в таблице */
    private final List<Contact> data;

    /**
     * Конструктор модели таблицы.
     *
     * @param data список контактов для отображения
     */
    public ContactTableModel(List<Contact> data) {
        this.data = data;
    }

    /**
     * Возвращает количество строк в таблице.
     *
     * @return количество контактов в списке
     */
    @Override
    public int getRowCount() {
        return data.size();
    }

    /**
     * Возвращает количество столбцов в таблице.
     *
     * @return количество столбцов
     */
    @Override
    public int getColumnCount() {
        return cols.length;
    }

    /**
     * Возвращает имя столбца по его индексу.
     *
     * @param col индекс столбца
     * @return имя столбца
     */
    @Override
    public String getColumnName(int col) {
        return cols[col];
    }

    /**
     * Возвращает значение ячейки в таблице.
     *
     * @param row индекс строки
     * @param col индекс столбца
     * @return объект, который будет отображён в ячейке
     */
    @Override
    public Object getValueAt(int row, int col) {
        Contact c = data.get(row);
        switch (col) {
            case 0: return c.fullName;
            case 1: return c.phonesAsString();
            case 2: return c.note;
            default: return "";
        }
    }
}
