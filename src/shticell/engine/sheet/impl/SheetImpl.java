package shticell.engine.sheet.impl;

import shticell.engine.sheet.api.Sheet;
import shticell.engine.sheet.cell.Cell;
import shticell.engine.sheet.coordinate.Coordinate;
import shticell.engine.sheet.coordinate.CoordinateFactory;

import java.util.HashMap;
import java.util.Map;

public class SheetImpl implements Sheet {
    private Map<Coordinate, Cell> cells = new HashMap<Coordinate, Cell>();
    private final String sheetName;
    private int version;

    public SheetImpl(String sheetName) {
        this.sheetName = sheetName;
        this.version = 1;
        this.cells = new HashMap<Coordinate, Cell>();
    }

    @Override
    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public String getSheetName() {
        return sheetName;
    }

    @Override
    public void addCell(Cell newCell) {
        Coordinate cellCord = newCell.getCoordinate();
        Coordinate coordinate = CoordinateFactory.createCoordinate(cellCord.getRow(), cellCord.getColumn());
        cells.put(coordinate, newCell);
    }

    public Cell getCell(Coordinate coordinate) {
        return cells.get(coordinate);
    }
}