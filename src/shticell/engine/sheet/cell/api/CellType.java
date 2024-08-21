package shticell.engine.sheet.cell.api;


import shticell.engine.expression.api.Expression;

public enum CellType {

    NUMERIC(Double.class),
    STRING(String.class),
    BOOLEAN(Boolean.class),
    EXPRESSION(Expression.class);

    private final Class<?> type;

    CellType(Class<?> type) {
        this.type = type;
    }

    public boolean isAssignableFrom(Class<?> aType) {
        return type.isAssignableFrom(aType);
    }
}
