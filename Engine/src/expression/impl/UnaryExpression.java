package expression.impl;

import expression.api.Expression;

public abstract class UnaryExpression implements Expression {

    private final Expression expression1;

    public UnaryExpression(Expression expression1) {
        this.expression1 = expression1;
    }

    @Override
    public Object evaluate() {
        return evaluate(expression1.evaluate());
    }

    abstract protected Object evaluate(Object evaluate) throws NumberFormatException;


}