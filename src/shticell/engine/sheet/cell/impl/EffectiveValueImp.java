package shticell.engine.sheet.cell.impl;

import shticell.engine.expression.api.Expression;
import shticell.engine.expression.impl.NumberExpression;
import shticell.engine.expression.impl.Operations.*;
import shticell.engine.expression.impl.StringExpression;
import shticell.engine.sheet.api.Sheet;
import shticell.engine.sheet.cell.api.CellType;
import shticell.engine.sheet.cell.api.EffectiveValue;
import shticell.engine.sheet.coordinate.Coordinate;

import java.util.ArrayList;
import java.util.List;


public class EffectiveValueImp implements EffectiveValue {
    private CellType cellType;
    private Object value;
    private String expressionName;
    private Coordinate coordinate;

    public EffectiveValueImp(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    @Override
    public CellType getCellType() {
        return cellType;
    }

    @Override
    public Object getValue() {
        return value;
    }

@Override
public String getExpressionName()
{
    return expressionName;
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
    public void calculateValue(Sheet sheet,String originalValue) {
        if (originalValue.isEmpty()) {
            this.value = null;
        } else if (originalValue.startsWith("{")) {
            stringToExpression(sheet,originalValue);
            this.cellType = CellType.EXPRESSION;
        } else {
            numOrString(originalValue);
        }
    }

    private String[] stringTrimer(Sheet sheet,String input) {
        if (input.startsWith("{") && input.endsWith("}")) {
            input = input.substring(1, input.length() - 1).trim();
        }

        List<String> result = new ArrayList<>();
        StringBuilder currentElement = new StringBuilder();
        boolean insideBraces = false;
        int openBrackets = 0;

        for (char c : input.toCharArray()) {
            if (c == '{') {
                insideBraces = true;
                openBrackets++;
            } else if (c == '}') {
                openBrackets--;
                if (openBrackets == 0) {
                    insideBraces = false;
                }
            }

            if (c == ',' && !insideBraces) {
                result.add(currentElement.toString().trim());
                currentElement.setLength(0);
            } else {
                currentElement.append(c);
            }
        }
        result.add(currentElement.toString().trim());

        String operator = result.remove(0);

        // Convert to array
        String[] operatorAndArgs = new String[result.size() + 1];
        operatorAndArgs[0] = operator;
        for (int i = 0; i < result.size(); i++) {
            operatorAndArgs[i + 1] = result.get(i);
        }

        return operatorAndArgs;
    }

    private Expression createExpression(Sheet sheet,String[] expression) {
        String operator = expression[0];
        List<Expression> args = new ArrayList<>();

        for (int i = 1; i < expression.length; i++) {
            args.add(stringToExpression(sheet, expression[i]));
        }
        Expression res;

        switch (operator) {
            case "PLUS" -> res = new Plus(args.get(0), args.get(1));
            case "MINUS" -> res = new Minus(args.get(0), args.get(1));
            case "TIMES" -> res = new Times(args.get(0), args.get(1));
            case "DIVIDE" -> res = new Divide(args.get(0), args.get(1));
            case "MOD" -> res = new Mod(args.get(0), args.get(1));
            case "POW" -> res = new Pow(args.get(0), args.get(1));
            case "ABS" -> res = new Abs(args.get(0));
            case "CONCAT" -> res = new Concat(args.get(0), args.get(1));
            case "SUB" -> res = new Sub(args.get(0), args.get(1), args.get(2));
            case "REF" -> res = new Ref(args.get(0), sheet);
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        }
        ;


        this.expressionName = res.getOperationName();
        this.value = res.evaluate();

        if (res instanceof Ref) {
            Coordinate ref = ((Ref) res).getRefCoordinate();
            sheet.getCell(coordinate).addCellToRelatedCells(ref);
            sheet.getCell(ref).addCellToAffectedCells(coordinate);
        }

        return res;
    }

    private Expression stringToExpression(Sheet sheet,String input) {

        if (input.startsWith("{") && input.endsWith("}")) {
            return createExpression(sheet,stringTrimer(sheet,input));
        } else {
            try {
                return new NumberExpression(Double.parseDouble(input));
            } catch (NumberFormatException e) {
                return new StringExpression(input);
            }
        }
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