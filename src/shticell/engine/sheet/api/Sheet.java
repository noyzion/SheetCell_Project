package shticell.engine.sheet.api;

import shticell.engine.sheet.coordinate.Coordinate;

public interface Sheet extends SheetReadActions,SheetUpdateActions{
    void updateCells(Coordinate coordinate);
}
