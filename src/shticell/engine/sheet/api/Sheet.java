package shticell.engine.sheet.api;

import shticell.engine.sheet.cell.api.Cell;
import shticell.engine.sheet.coordinate.Coordinate;

public interface Sheet {

    public void updateVersion();

    public int getVersion();

    public String getSheetName();

    public void addCell(Cell newCell);

    public Cell getCell(Coordinate coordinate);

    public int getRowSize();

    public int getColSize();

    public int getColumnWidthUnits();

    public int getRowsHeightUnits();
}