package expression.api;

import sheet.api.Sheet;
import sheet.cell.api.CellType;
import sheet.coordinate.Coordinate;

import java.util.List;

public interface ExpressionFactory {

    Expression createExpression(Sheet sheet, String operator, List<Expression> args);

    CellType validateArguments(String operator, List<Expression> args, Coordinate coordinate);
}
