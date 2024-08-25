package shticell.engine.expression.impl;

import shticell.engine.expression.api.Expression;
import shticell.engine.expression.impl.Operations.Ref;

import javax.crypto.interfaces.PBEKey;

public class StringExpression implements Expression {
    private String string;

    public StringExpression(String s) {
        this.string = s;
    }

    @Override
    public Object evaluate() {
        return string;
    }

    @Override
    public String getOperationName() {
        return "String";
    }

    @Override
    public String toString() {
        return string;
    }

}