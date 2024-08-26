package shticell.engine.sheet.api;

import shticell.engine.sheet.cell.api.Cell;
import shticell.engine.sheet.coordinate.Coordinate;
import shticell.engine.sheet.impl.Edge;

public interface SheetUpdateActions {
    void addCell(Cell newCell);

    void onCellUpdated(String originalValue, Coordinate coordinate);

    void addEdge(Edge edge);

    void updateCells(Coordinate coordinate);

    void updateVersion();
}