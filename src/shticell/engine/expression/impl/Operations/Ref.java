package shticell.engine.expression.impl.Operations;

import shticell.engine.expression.api.Expression;
import shticell.engine.expression.impl.UnaryExpression;
import shticell.engine.sheet.api.Sheet;
import shticell.engine.sheet.coordinate.Coordinate;
import shticell.engine.sheet.coordinate.CoordinateParser;


public class Ref extends UnaryExpression {

    Sheet sheet;
    Coordinate refCoordinate;

    public Ref(Expression expression1, Sheet sheet) {
        super(expression1);
        this.sheet = sheet;
    }

    //TODO!
    @Override
    protected Object evaluate(Object object) throws NumberFormatException {
        if (!(object instanceof String)) {
            throw new IllegalArgumentException("argument must be of type String.");
        } else {
            this.refCoordinate = CoordinateParser.parse((String) object);
            return sheet.getCell(refCoordinate).getEffectiveValue().getValue();
        }
    }

    public Coordinate getRefCoordinate() {
        return refCoordinate;
    }

        @Override
        public String getOperationName() {
            return "REF";
        }
    }
