package expression.impl;

import expression.api.Expression;
import sheet.cell.api.CellType;

public class StringExpression implements Expression {
    private String string;

    public StringExpression(String s) {
        this.string = s;
    }

    @Override
    public Object evaluate() {
        return string;
    }

    @Override
    public String getOperationName() {
        return "String";
    }

    @Override
    public String toString() {
        return string;
    }

    @Override
    public CellType getCellType() {
        return CellType.STRING;
    }

}