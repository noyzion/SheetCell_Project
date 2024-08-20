package shticell.engine.expression.impl.Operations;

import shticell.engine.expression.api.Expression;
import shticell.engine.expression.impl.BinaryExpression;

public class Concat extends BinaryExpression {
    public Concat(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    public String getOperationName() {
        return "CONCAT";
    }

    @Override
    protected Object evaluate(Object e1, Object e2) {
        if (!(e1 instanceof String str1)) {
            String actualType = e1 == null ? "null" : e1.getClass().getSimpleName();
            throw new IllegalArgumentException("Invalid type for the first argument: Expected String, but received " + actualType + ".");
        }

        if (!(e2 instanceof String str2)) {
            String actualType = e2 == null ? "null" : e2.getClass().getSimpleName();
            throw new IllegalArgumentException("Invalid type for the second argument: Expected String, but received " + actualType + ".");
        }
        return str1 + str2;
    }
}
