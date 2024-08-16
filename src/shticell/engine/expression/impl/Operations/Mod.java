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
    protected Object evaluate(Object e1, Object e2) throws NumberFormatException {
        return (Double) e1 % (Double) e2;
    }
}
