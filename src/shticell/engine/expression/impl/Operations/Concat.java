package shticell.engine.expression.impl.Operations;

import shticell.engine.expression.api.Expression;
import shticell.engine.expression.impl.BinaryExpression;
import shticell.engine.sheet.cell.api.CellType;
import shticell.engine.sheet.cell.impl.EffectiveValueImp;

public class Concat extends BinaryExpression {
    public Concat(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    public String getOperationName() {
        return "CONCAT";
    }

    @Override
    public CellType getCellType() {
        return CellType.STRING;
    }

    @Override
    protected Object evaluate(Object e1, Object e2) {
        if (e1 == null) {
            throw new IllegalArgumentException("First argument cannot be empty.");
        }
        if (e2 == null) {
            throw new IllegalArgumentException("Second argument cannot be empty.");
        }
        if (!(e1 instanceof String str1)) {
            return "!UNDEFINED!";
        }

        if (!(e2 instanceof String str2)) {
            return "!UNDEFINED!";
        }

        return str1 + str2;
    }
}
