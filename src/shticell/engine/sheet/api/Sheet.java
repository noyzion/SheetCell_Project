package shticell.engine.sheet.api;

import shticell.engine.sheet.cell.api.Cell;
import shticell.engine.sheet.coordinate.Coordinate;

public interface Sheet {

    void updateVersion();

    int getVersion();

    String getSheetName();

    void addCell(Cell newCell);

    Cell getCell(Coordinate coordinate);

    int getRowSize();

    int getColSize();

    int getColumnWidthUnits();

    int getRowsHeightUnits();

    void onCellUpdated(String originalValue, Coordinate coordinate);

}