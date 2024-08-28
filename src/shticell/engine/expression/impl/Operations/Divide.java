package shticell.engine.expression.impl.Operations;

import shticell.engine.expression.api.Expression;
import shticell.engine.expression.impl.BinaryExpression;
import shticell.engine.sheet.cell.api.CellType;

public class Divide extends BinaryExpression {

    public Divide(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    public String getOperationName() {
        return "DIVIDE";
    }

    @Override
    public CellType getCellType() {
        return CellType.NUMERIC;
    }

    @Override
    protected Object evaluate(Object e1, Object e2) {
        if (!(e1 instanceof Double)) {
            String actualType = e1 == null ? "null" : e1.getClass().getSimpleName();
            throw new IllegalArgumentException("Invalid type for the first argument: Expected Double, but received " + actualType + ".");
        }

        if (!(e2 instanceof Double)) {
            String actualType = e2 == null ? "null" : e2.getClass().getSimpleName();
            throw new IllegalArgumentException("Invalid type for the second argument: Expected Double, but received " + actualType + ".");
        }


        double numerator = (Double) e1;
        double denominator = (Double) e2;

        if (denominator == 0) {
            return Double.NaN;
        }

        return numerator / denominator;
    }
}