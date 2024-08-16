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
        if (!(e1 instanceof String) || !(e2 instanceof String)) {
            throw new IllegalArgumentException("Both arguments must be of type String.");
        }

        String str1 = (String) e1;
        String str2 = (String) e2;

        // Perform concatenation
        return str1 + str2;
    }
}
