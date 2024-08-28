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
        if (!(e1 instanceof String str1)) {
            String actualType = e1 == null ? "null" : e1.getClass().getSimpleName();
            return "!UNDEFINED!";
        }

        if (!(e2 instanceof String str2)) {
            String actualType = e2 == null ? "null" : e2.getClass().getSimpleName();
            return "!UNDEFINED!";
        }

        return str1 + str2;
    }
}
