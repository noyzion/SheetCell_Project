package shticell.engine.expression.impl.Operations;

import shticell.engine.expression.api.Expression;
import shticell.engine.expression.impl.BinaryExpression;
import shticell.engine.sheet.cell.api.CellType;

public class Minus extends BinaryExpression {

    public Minus(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    public String getOperationName() {
        return "MINUS";
    }


    @Override
    public CellType getCellType() {
        return CellType.NUMERIC;
    }

    @Override
    protected Object evaluate(Object e1, Object e2) {
        if (!(e1 instanceof Double)) {
            throw new IllegalArgumentException("First argument must be numeric. Received: "
                    + (e1 != null ? e1.getClass().getSimpleName() : "null"));
        }

        if (!(e2 instanceof Double)) {
            throw new IllegalArgumentException("Second argument must be numeric. Received: "
                    + (e2 != null ? e2.getClass().getSimpleName() : "null"));
        }

        double num1 = (Double) e1;
        double num2 = (Double) e2;

        return num1 - num2;
    }
}