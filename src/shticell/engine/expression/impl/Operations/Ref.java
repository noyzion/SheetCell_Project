package shticell.engine.expression.impl.Operations;

import shticell.engine.expression.api.Expression;
import shticell.engine.expression.impl.UnaryExpression;
import shticell.engine.sheet.api.Sheet;
import shticell.engine.sheet.cell.api.Cell;
import shticell.engine.sheet.cell.api.CellType;
import shticell.engine.sheet.cell.impl.CellImpl;
import shticell.engine.sheet.coordinate.Coordinate;
import shticell.engine.sheet.coordinate.CoordinateParser;
import shticell.engine.sheet.coordinate.ParseException;

import java.util.Objects;


public class Ref extends UnaryExpression {

    Sheet sheet;
    Coordinate refCoordinate;

    public Ref(Expression expression1, Sheet sheet) {
        super(expression1);
        this.sheet = sheet;
    }

    @Override
    public CellType getCellType() {
        return CellType.EXPRESSION;
    }

    @Override
    protected Object evaluate(Object object) throws NumberFormatException {
        if (!(object instanceof String coordinateStr)) {
            throw new IllegalArgumentException("The argument must be a coordinate. Provided: " + object.getClass().getSimpleName());
        }

        try {
            this.refCoordinate = CoordinateParser.parse(coordinateStr);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid coordinate format: " + coordinateStr, e);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing coordinate: " + coordinateStr, e);
        }


        Cell cell = sheet.getCell(refCoordinate);

        if (cell == null) {
            cell = new CellImpl(refCoordinate, sheet.getRowsHeightUnits(), sheet.getRowsHeightUnits());
            sheet.addCell(cell);
            sheet.onCellUpdated(null, refCoordinate);
            //throw new IllegalArgumentException("Cell at coordinate: " + coordinateStr + " is empty, you cant use it");
        } else if (Objects.equals(cell.getOriginalValue(), " ")) {
            return Double.NaN;
        }


        return cell.getEffectiveValue().getValue();
    }

    public Coordinate getRefCoordinate() {
        return refCoordinate;
    }

    @Override
    public String getOperationName() {
        return "REF";
    }
}