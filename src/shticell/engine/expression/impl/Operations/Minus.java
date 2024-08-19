package shticell.engine.expression.impl.Operations;

import shticell.engine.expression.api.Expression;
import shticell.engine.expression.impl.BinaryExpression;

public class Minus extends BinaryExpression {

    public Minus(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    public String getOperationName() {
        return "MINUS";
    }

    @Override
    protected Object evaluate(Object e1, Object e2) {
        if (!(e1 instanceof Double) || !(e2 instanceof Double)) {
            throw new IllegalArgumentException("Argument must be numeric.");
        }

        double num1 = (Double) e1;
        double num2 = (Double) e2;

        return num1 - num2;
    }
}