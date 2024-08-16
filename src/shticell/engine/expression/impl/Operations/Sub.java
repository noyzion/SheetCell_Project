package shticell.engine.expression.impl.Operations;

import shticell.engine.expression.api.Expression;
import shticell.engine.expression.impl.TrinaryExpression;

public class Sub extends TrinaryExpression {

    public Sub(Expression expression1, Expression expression2, Expression expression3) {
        super(expression1, expression2, expression3);
    }

    @Override
    public String getOperationName() {
        return "SUB";
    }

    @Override
    protected Object evaluate(Object evaluate1, Object evaluate2, Object evaluate3) {
        if (!(evaluate1 instanceof String)) {
            throw new IllegalArgumentException("First argument must be a String.");
        }

        if (!(evaluate2 instanceof Double) || !(evaluate3 instanceof Double)) {
            throw new IllegalArgumentException("Second and third arguments must be numeric.");
        }

        String str = (String) evaluate1;
        int startIndex = ((Double) evaluate2).intValue();
        int endIndex = ((Double) evaluate3).intValue();

        if (startIndex < 0 || startIndex > str.length() || endIndex < startIndex || endIndex > str.length()) {
            return "!UNDEFINED!"; // Return undefined for invalid substring indices
        }

        try {
            return str.substring(startIndex, endIndex);
        } catch (IndexOutOfBoundsException e) {
            return "!UNDEFINED!"; // Return undefined for any unexpected index issues
        }
    }
}