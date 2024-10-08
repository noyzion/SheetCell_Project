package expression.impl;


import expression.api.Expression;

public abstract class BinaryExpression implements Expression {
    private final Expression expression1;
    private final Expression expression2;

    public BinaryExpression(Expression expression1, Expression expression2) {
        this.expression1 = expression1;
        this.expression2 = expression2;
    }

    @Override
    public Object evaluate()
    {
        return evaluate(expression1.evaluate(), expression2.evaluate());
    }

    abstract protected Object evaluate(Object evaluate, Object evaluate2) throws NumberFormatException;

}