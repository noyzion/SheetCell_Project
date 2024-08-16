package shticell.engine.expression.impl.Operations;

import shticell.engine.expression.api.Expression;
import shticell.engine.expression.impl.BinaryExpression;

public class Pow extends BinaryExpression {
    public Pow(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    public String getOperationName() {
        return "POW";
    }

    @Override
    protected Object evaluate(Object e1, Object e2) {
        if (!(e1 instanceof Double) || !(e2 instanceof Double)) {
            throw new IllegalArgumentException("Invalid argument types for power operation.");
        }

        double base = (Double) e1;
        double exponent = (Double) e2;

        if (base == 0 && exponent < 0) {
            return Double.NaN;
        }

        return Math.pow(base, exponent);
    }
}