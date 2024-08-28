package shticell.engine.expression.api;

import shticell.engine.sheet.cell.api.CellType;

public interface Expression {

    Object evaluate();

    String getOperationName();

    CellType getCellType();
}
