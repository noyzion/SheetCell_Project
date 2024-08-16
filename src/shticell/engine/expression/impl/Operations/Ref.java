package shticell.engine.expression.impl.Operations;

import shticell.engine.expression.api.Expression;
import shticell.engine.expression.impl.UnaryExpression;
import shticell.engine.sheet.api.Sheet;

public class Ref extends UnaryExpression {

    public Ref(Expression expression1) {
        super(expression1);
    }

    //TODO!
    @Override
    protected Object evaluate(Object evaluate) throws NumberFormatException {
        return null;
    }

    @Override
    public String getOperationName() {
        return "REF";
    }
}
