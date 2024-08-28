package shticell.engine.expression.impl.Operations;

import shticell.engine.expression.api.Expression;
import shticell.engine.expression.impl.UnaryExpression;
import shticell.engine.sheet.cell.api.CellType;

public class Abs extends UnaryExpression {

    public Abs(Expression ex1) {
        super(ex1);
    }

    @Override
    public String getOperationName() {
        return "ABS";
    }

    @Override
    public CellType getCellType() {
        return CellType.NUMERIC;
    }
    @Override
    protected Object evaluate(Object e1) {
        if (!(e1 instanceof Double)) {
            String actualType = e1 == null ? "null" : e1.getClass().getSimpleName();
            throw new IllegalArgumentException("Invalid argument type: Expected Double, but received " + actualType + ".");
        }
        double num = (Double) e1;
        return Math.abs(num);
    }
}