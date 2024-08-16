package shticell.engine.sheet.cell.impl;

import shticell.engine.expression.api.Expression;
import shticell.engine.expression.impl.Operations.*;
import shticell.engine.sheet.cell.api.CellType;
import shticell.engine.sheet.cell.api.EffectiveValue;

import java.util.ArrayList;
import java.util.List;


public class EffectiveValueImp implements EffectiveValue {
    private CellType cellType;
    private Object value;

    public EffectiveValueImp(CellType cellType, Object value) {
        this.cellType = cellType;
        this.value = value;
    }

    @Override
    public CellType getCellType() {
        return cellType;
    }

    @Override
    public Object getValue() {
        return value;
    }


    //TODO!
    @Override
    public <T> T extractValueWithExpectation(Class<T> type) {
        if (cellType.isAssignableFrom(type)) {
            return type.cast(value);
        }
        // error handling... exception ? return null ?
        return null;
    }

    @Override
    public void calculateValue(String originalValue) {
        if (originalValue.isEmpty()) {
            this.value = null;
        } else if (originalValue.startsWith("{")) {
            this.value = stringToExpression(originalValue);
            this.cellType = CellType.EXPRESSION;
        } else {
            numOrString(originalValue);
        }
    }

    private Expression stringToExpression(String value) {
        char firstChar = value.charAt(0);
        if (firstChar != '{') {
            throw new IllegalArgumentException("Invalid expression format: " + value);
        }

        int countOpenBraces = 0;
        List<String> result = new ArrayList<>();
        StringBuilder currentExpression = new StringBuilder();

        for (int i = 1; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == '{') {
                countOpenBraces++;
            } else if (c == '}') {
                countOpenBraces--;
                if (countOpenBraces == 0) {
                    result.add(currentExpression.toString());
                    currentExpression.setLength(0); // Clear currentExpression for next part
                    break;
                }
            }
            currentExpression.append(c);
        }

        if (countOpenBraces != 0) {
            throw new IllegalArgumentException("Mismatched braces in input: " + value);
        }

        return createExpressionFromResult(result);
    }


    private Expression createExpressionFromResult(List<String> result) {
        if (result.isEmpty()) {
            throw new IllegalArgumentException("Empty expression result.");
        }

        String operator = result.get(0);
        List<Expression> args = parseArguments(result);

        return createExpressionFromOperator(operator, args);
    }

    private Expression createExpressionFromOperator(String operator, List<Expression> args) {
        return switch (operator) {
            case "PLUS" -> new Plus(args.get(0), args.get(1));
            case "MINUS" -> new Minus(args.get(0), args.get(1));
            case "TIMES" -> new Times(args.get(0), args.get(1));
            case "DIVIDE" -> new Divide(args.get(0), args.get(1));
            case "MOD" -> new Mod(args.get(0), args.get(1));
            case "POW" -> new Pow(args.get(0), args.get(1));
            case "ABS" -> new Abs(args.get(0));
            case "CONCAT" -> new Concat(args.get(0), args.get(1));
            case "SUB" -> new Sub(args.get(0), args.get(1), args.get(2));
            case "REF" -> new Ref(args.get(0));
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };
    }

    private List<Expression> parseArguments(List<String> result) {
        List<Expression> args = new ArrayList<>();
        for (int i = 1; i < result.size(); i++) {
            args.add(stringToExpression(result.get(i)));
        }
        return args;
    }

    private void numOrString(String value) {
        try {
            this.value = Double.parseDouble(value);
            this.cellType = CellType.NUMERIC;
        } catch (NumberFormatException e) {
            this.value = value;
            this.cellType = CellType.STRING;
        }
    }
}