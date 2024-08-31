package expression.impl;

import expression.api.Expression;

public abstract class TrinaryExpression implements Expression {
    private final Expression expression1;
    private final Expression expression2;
    private final Expression expression3;

    public TrinaryExpression(Expression expression1, Expression expression2, Expression expression3) {
        this.expression1 = expression1;
        this.expression2 = expression2;
        this.expression3 = expression3;
    }

    @Override
    public Object evaluate() {
        return evaluate(expression1.evaluate(), expression2.evaluate(), expression3.evaluate());
    }

    abstract protected Object evaluate(Object evaluate, Object evaluate2, Object evaluate3) throws NumberFormatException;

}