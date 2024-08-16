package shticell.engine.expression.impl.Operations;

import shticell.engine.expression.api.Expression;
import shticell.engine.expression.impl.BinaryExpression;

public class Plus extends BinaryExpression {
    public Plus(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    public String getOperationName() {
        return "PLUS";
    }

    @Override
    protected Object evaluate(Object e1, Object e2) {
        if (!(e1 instanceof Double) || !(e2 instanceof Double)) {
            throw new IllegalArgumentException("Both arguments must be of type Double.");
        }

        double num1 = (Double) e1;
        double num2 = (Double) e2;

        // Perform addition
        return num1 + num2;
    }
}
