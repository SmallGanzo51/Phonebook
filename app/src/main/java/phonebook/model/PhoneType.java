package phonebook.model;

/**
 * Перечисление типов телефонных номеров.
 * <p>
 * Возможные значения:
 * <ul>
 *     <li>MOBILE – Сотовый</li>
 *     <li>HOME – Домашний</li>
 *     <li>WORK – Рабочий</li>
 *     <li>FAX – Факс</li>
 *     <li>OTHER – Другое</li>
 * </ul>
 */
public enum PhoneType {
    MOBILE, HOME, WORK, FAX, OTHER;

    /**
     * Возвращает строковое представление типа телефона на русском языке.
     *
     * @return строковое представление типа
     */
    @Override
    public String toString() {
        switch (this) {
            case MOBILE: return "Сотовый";
            case HOME:   return "Домашний";
            case WORK:   return "Рабочий";
            case FAX:    return "Факс";
            default:     return "Другое";
        }
    }

    /**
     * Создаёт тип телефона из строки.
     * <p>
     * Поддерживаются как русские, так и английские варианты.
     *
     * @param s строка с типом телефона
     * @return соответствующий {@link PhoneType}, по умолчанию {@link #OTHER}
     */
    public static PhoneType fromString(String s) {
        if (s == null) return OTHER;
        switch (s.toLowerCase()) {
            case "сотовый": case "mobile": return MOBILE;
            case "домашний": case "home": return HOME;
            case "рабочий": case "work": return WORK;
            case "факс": case "fax": return FAX;
            default: return OTHER;
        }
    }
}
