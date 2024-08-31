package shticell.engine.expression.api;

import shticell.engine.expression.api.Expression;
import shticell.engine.sheet.api.Sheet;
import shticell.engine.sheet.cell.api.CellType;
import shticell.engine.sheet.coordinate.Coordinate;

import java.util.List;

public interface ExpressionFactory {

    Expression createExpression(Sheet sheet, String operator, List<Expression> args);

    CellType validateArguments(String operator, List<Expression> args, Coordinate coordinate);
}
