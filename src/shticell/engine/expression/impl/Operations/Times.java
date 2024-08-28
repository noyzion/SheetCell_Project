package shticell.engine.expression.impl.Operations;

import jakarta.xml.bind.ValidationException;
import shticell.engine.expression.api.Expression;
import shticell.engine.expression.impl.BinaryExpression;
import shticell.engine.sheet.cell.api.CellType;

public class Times extends BinaryExpression {
    public Times(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    public String getOperationName() {
        return "TIMES";
    }


    @Override
    public CellType getCellType() {
        return CellType.NUMERIC;
    }

    @Override
    protected Object evaluate(Object e1, Object e2) {
        if (!(e1 instanceof Double)) {
            throw new IllegalArgumentException("First argument must be numeric. Received: " + e1.getClass().getSimpleName());
        }

        if (!(e2 instanceof Double)) {
            throw new IllegalArgumentException("Second argument must be numeric. Received: " + e2.getClass().getSimpleName());
        }

        double num1 = (Double) e1;
        double num2 = (Double) e2;

        return num1 * num2;
    }
}
