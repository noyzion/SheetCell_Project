package shticell.ui;

public enum MenuOption {
    READ_FILE(1, "Read File"),
    DISPLAY_SPREADSHEET(2, "Display Spreadsheet"),
    DISPLAY_SINGLE_CELL(3, "Display Single Cell"),
    UPDATE_SINGLE_CELL(4, "Update Single Cell"),
    DISPLAY_VERSIONS(5, "Display Versions"),
    SAVE_SYSTEM_STATE(6, "Save System State"),
    LOAD_SYSTEM_STATE(7, "Load System State"),
    EXIT(8, "Exit");

    private final int value;
    private final String description;

    MenuOption(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static MenuOption fromValue(int value) {
        for (MenuOption option : values()) {
            if (option.getValue() == value) {
                return option;
            }
        }
        throw new IllegalArgumentException("Invalid menu option value: " + value);
    }
}
