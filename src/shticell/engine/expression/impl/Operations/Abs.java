package shticell.engine.expression.impl.Operations;

import shticell.engine.expression.api.Expression;
import shticell.engine.expression.impl.UnaryExpression;

public class Abs extends UnaryExpression {

    public Abs(Expression ex1) {
        super(ex1);
    }

    @Override
    public String getOperationName() {
        return "ABS";
    }

    @Override
    protected Object evaluate(Object e1) throws NumberFormatException {
        return Math.abs((Double) e1);
    }
}
