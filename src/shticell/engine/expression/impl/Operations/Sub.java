package shticell.engine.expression.impl.Operations;

import shticell.engine.expression.api.Expression;
import shticell.engine.expression.impl.TrinaryExpression;
import shticell.engine.sheet.cell.api.CellType;

public class Sub extends TrinaryExpression {

    public Sub(Expression expression1, Expression expression2, Expression expression3) {
        super(expression1, expression2, expression3);
    }


    @Override
    public CellType getCellType() {
        return CellType.STRING;
    }

    @Override
    public String getOperationName() {
        return "SUB";
    }

    @Override
    protected Object evaluate(Object evaluate1, Object evaluate2, Object evaluate3) {
        if (!(evaluate1 instanceof String str1)) {
            throw new IllegalArgumentException("First argument must be a String. Received: "
                    + (evaluate1 != null ? evaluate1.getClass().getSimpleName() : "null"));
        }

        if (!(evaluate2 instanceof Double num2)) {
            throw new IllegalArgumentException("Second argument must be numeric. Received: "
                    + (evaluate2 != null ? evaluate2.getClass().getSimpleName() : "null"));
        }

        if (!(evaluate3 instanceof Double num3)) {
            throw new IllegalArgumentException("Third argument must be numeric. Received: "
                    + (evaluate3 != null ? evaluate3.getClass().getSimpleName() : "null"));
        }

        // Validate that both indices are whole numbers
        if (isWholeNumber(num2)) {
            throw new IllegalArgumentException("Second argument must be a whole number. Received: " + num2);
        }

        if (isWholeNumber(num3)) {
            throw new IllegalArgumentException("Third argument must be a whole number. Received: " + num3);
        }

        int startIndex = num2.intValue();
        int endIndex = num3.intValue();

        if (startIndex < 0 || startIndex > str1.length() || endIndex < startIndex || endIndex > str1.length()) {
            throw new IllegalArgumentException("Invalid indices: startIndex = " + startIndex + ", endIndex = " + endIndex);
        }

        try {
            return str1.substring(startIndex, endIndex);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Error extracting substring with indices: startIndex = " + startIndex + ", endIndex = " + endIndex, e);
        }
    }

    private boolean isWholeNumber(Double number) {
        return number % 1 != 0;
    }
}