package nl.fontys.newswebapplication.domain.enums;

public enum Department {
    EDITORIAL,
    NEWSROOM,
    COPYRIGHTING,
    LAYOUT_DESIGN,
    ADVERTISING_MARKETING,
    ONLINE;

    public static boolean exists(int id) {
        for (Department department : Department.values()) {
            if (department.ordinal() == id ) {
                return true;
            }
        }
        return false;
    }
}
