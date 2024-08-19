package shticell.engine.expression.impl;

import shticell.engine.expression.api.Expression;


public class NumberExpression implements Expression {
    private final double value;

    public NumberExpression(double value) {
        this.value = value;
    }

    public NumberExpression(String value) {
        this.value = Double.parseDouble(value);
    }

    @Override
    public Object evaluate() {
        return value;
    }

    @Override
    public String getOperationName()
    {
        return "Number";
    }
    @Override
    public String toString() {
        return String.valueOf(value);
    }
}