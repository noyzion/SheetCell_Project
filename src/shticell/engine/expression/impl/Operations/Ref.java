package shticell.engine.expression.impl.Operations;

import shticell.engine.expression.api.Expression;
import shticell.engine.expression.impl.UnaryExpression;
import shticell.engine.sheet.api.Sheet;
import shticell.engine.sheet.cell.api.Cell;
import shticell.engine.sheet.cell.impl.CellImpl;
import shticell.engine.sheet.coordinate.Coordinate;
import shticell.engine.sheet.coordinate.CoordinateImpl;

import javax.print.DocFlavor;

public class Ref extends UnaryExpression {

    Sheet sheet;
    public Ref(Expression expression1, Sheet sheet) {
        super(expression1);
        this.sheet = sheet;
    }

    //TODO!
    @Override
    protected Object evaluate(Object object) throws NumberFormatException {
        if (!(object instanceof String)) {
            throw new IllegalArgumentException("argument must be of type String.");
        }
        else {
           int[] coordinates = CellImpl.convertCellIdentifierToCoordinates(sheet,(String) object);
            Coordinate getRefOf = new CoordinateImpl(coordinates[1],coordinates[0]);
            return sheet.getCell(getRefOf).getEffectiveValue().getValue();
        }


    }


        @Override
        public String getOperationName() {
            return "REF";
        }
    }
