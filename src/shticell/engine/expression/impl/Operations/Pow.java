package shticell.engine.expression.impl.Operations;

import jakarta.xml.bind.ValidationException;
import shticell.engine.expression.api.Expression;
import shticell.engine.expression.impl.BinaryExpression;
import shticell.engine.sheet.cell.api.CellType;

public class Pow extends BinaryExpression {
    public Pow(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    public String getOperationName() {
        return "POW";
    }

    @Override
    public CellType getCellType() {
        return CellType.NUMERIC;
    }
    @Override
    protected Object evaluate(Object e1, Object e2) {
        if (!(e1 instanceof Double)) {
            throw new IllegalArgumentException("Base argument must be numeric. Received: " + (e1 != null ? e1.getClass().getSimpleName() : "null"));
        }

        if (!(e2 instanceof Double)) {
            throw new IllegalArgumentException("Exponent argument must be numeric. Received: " + (e2 != null ? e2.getClass().getSimpleName() : "null"));
        }


        double base = (Double) e1;
        double exponent = (Double) e2;

        if (base == 0 && exponent < 0) {
            return Double.NaN;
        }

        return Math.pow(base, exponent);
    }
}