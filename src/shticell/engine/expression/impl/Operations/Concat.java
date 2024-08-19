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
        if (!(e1 instanceof String str1) || !(e2 instanceof String str2)) {
            throw new IllegalArgumentException("Both arguments must be of type String.");
        }
        return str1 + str2;
    }
}
