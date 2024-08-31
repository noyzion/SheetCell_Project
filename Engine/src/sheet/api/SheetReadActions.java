package sheet.api;

import sheet.cell.api.Cell;
import sheet.coordinate.Coordinate;
import sheet.impl.Edge;

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