package shticell.engine.sheet.api;

import shticell.engine.sheet.cell.api.Cell;
import shticell.engine.sheet.coordinate.Coordinate;
import shticell.engine.sheet.impl.Edge;

public interface SheetReadActions {

    int getVersion();
    String getSheetName();
    Cell getCell(Coordinate coordinate);
    int getRowSize();
    int getColSize();
    int getColumnWidthUnits();
    int getRowsHeightUnits();



}
