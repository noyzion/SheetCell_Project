package shticell.engine.expression.impl.Operations;

import shticell.engine.expression.api.Expression;
import shticell.engine.expression.impl.BinaryExpression;

public class Mod extends BinaryExpression {

    public Mod(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    public String getOperationName() {
        return "MOD";
    }

    @Override
    protected Object evaluate(Object e1, Object e2) {
        if (!(e1 instanceof Double) || !(e2 instanceof Double)) {
            throw new IllegalArgumentException("Argument must be numeric.");
        }

        double numerator = (Double) e1;
        double denominator = (Double) e2;

        if (denominator == 0) {
            return Double.NaN; // Return NaN for modulus by zero
        }

        return numerator % denominator;
    }
}
