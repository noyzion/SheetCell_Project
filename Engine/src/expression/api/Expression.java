package expression.api;

import sheet.cell.api.CellType;

public interface Expression {

    Object evaluate();

    String getOperationName();

    CellType getCellType();
}
