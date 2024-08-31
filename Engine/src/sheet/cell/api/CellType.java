package sheet.cell.api;


import expression.api.Expression;

public enum CellType {

    NUMERIC(Double.class),
    STRING(String.class),
    BOOLEAN(Boolean.class),
    EXPRESSION(Expression.class),
    EMPTY(Object.class);
    private final Class<?> type;

    CellType(Class<?> type) {
        this.type = type;
    }

    public boolean isAssignableFrom(Class<?> aType) {
        return type.isAssignableFrom(aType);
    }

    public Class<?> getType() {
        return type;
    }
}
