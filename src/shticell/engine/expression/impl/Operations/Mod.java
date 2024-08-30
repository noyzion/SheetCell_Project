package shticell.engine.expression.impl.Operations;

import shticell.engine.expression.api.Expression;
import shticell.engine.expression.impl.BinaryExpression;
import shticell.engine.sheet.cell.api.CellType;

public class Mod extends BinaryExpression {

    public Mod(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    public String getOperationName() {
        return "MOD";
    }


    @Override
    public CellType getCellType() {
        return CellType.NUMERIC;
    }

    @Override
    protected Object evaluate(Object e1, Object e2) {
        if (e1 == null) {
            throw new IllegalArgumentException("First argument cannot be empty.");
        }
        if (e2 == null) {
            throw new IllegalArgumentException("Second argument cannot be empty.");
        }

        if (!(e1 instanceof Double)) {
            throw new IllegalArgumentException("First argument must be numeric. Received: " + (e1 != null ? e1.getClass().getSimpleName() : "null"));
        }

        if (!(e2 instanceof Double)) {
            throw new IllegalArgumentException("Second argument must be numeric. Received: " + (e2 != null ? e2.getClass().getSimpleName() : "null"));
        }

        double numerator = (Double) e1;
        double denominator = (Double) e2;

        if (denominator == 0) {
            return Double.NaN; // Return NaN for modulus by zero
        }

        return numerator % denominator;
    }
}
