package shticell.engine.sheet.api;

import shticell.engine.sheet.cell.api.Cell;
import shticell.engine.sheet.coordinate.Coordinate;
import shticell.engine.sheet.impl.Edge;

import java.util.List;
import java.util.Map;

public interface SheetReadActions {

    int getVersion();

    String getSheetName();

    Cell getCell(Coordinate coordinate);

    int getRowSize();

    int getColSize();

    int getColumnWidthUnits();

    int getRowsHeightUnits();

    Map<Coordinate, Cell> getCells();

    List<Edge> getEdges();

    int getCounterChangedCells();


}