package shticell.engine.expression.impl.Operations;

import jakarta.xml.bind.ValidationException;
import shticell.engine.expression.api.Expression;
import shticell.engine.expression.impl.BinaryExpression;
import shticell.engine.sheet.cell.api.CellType;

public class Plus extends BinaryExpression {
    public Plus(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    public String getOperationName() {
        return "PLUS";
    }


    @Override
    public CellType getCellType() {
        return CellType.NUMERIC;
    }

    public Object evaluate(Object e1, Object e2) {
        if (e1 == null) {
            throw new IllegalArgumentException("First argument cannot be empty.");
        }
        if (e2 == null) {
            throw new IllegalArgumentException("Second argument cannot be empty.");
        }
        if (!(e1 instanceof Double)) {
            throw new IllegalArgumentException("First argument must be of type Double. Received: " + e1.getClass().getSimpleName());
        }
        if (!(e2 instanceof Double)) {
            throw new IllegalArgumentException("Second argument must be of type Double. Received: " + e2.getClass().getSimpleName());
        }
        double num1 = (Double) e1;
        double num2 = (Double) e2;

        return num1 + num2;
    }
}
