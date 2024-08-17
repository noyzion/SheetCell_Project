package shticell.engine.sheet.cell.impl;

import shticell.engine.sheet.api.Sheet;
import shticell.engine.sheet.cell.api.Cell;
import shticell.engine.sheet.cell.api.EffectiveValue;
import shticell.engine.sheet.coordinate.Coordinate;
import shticell.engine.sheet.coordinate.CoordinateImpl;

import java.util.ArrayList;
import java.util.List;

public class CellImpl implements Cell {
    private Sheet mySheet;
    private EffectiveValue effectiveValue;
    private String originalValue;
    private final Coordinate coordinate;
    private List<Cell> relatedCells = new ArrayList<Cell>();
    private List<Cell> affectedCells = new ArrayList<>();
    private int lastVersionUpdate;

    public CellImpl(int row, int column, String originalValue, int version,Sheet sheet) {
        this.coordinate = new CoordinateImpl(row, column);
        this.originalValue = originalValue;
        this.effectiveValue = new EffectiveValueImp();
        this.effectiveValue.calculateValue(sheet,originalValue);
        this.lastVersionUpdate = version;
        this.mySheet = sheet;

    }

    @Override
    public Coordinate getCoordinate() {
        return coordinate;
    }

    @Override
    public void setOriginalValue(String originalValue) {
        this.originalValue = originalValue;
        effectiveValue.calculateValue(mySheet,originalValue);
    }

    @Override
    public String getOriginalValue() {
        return originalValue;
    }

    @Override
    public EffectiveValue getEffectiveValue() {
        return effectiveValue;
    }

    @Override
    public void addCellToRelatedCells(Cell cell) {
        relatedCells.add(cell);
    }

    @Override
    public void addCellToAffectedCells(Cell cell) {
        affectedCells.add(cell);
    }

    @Override
    public List<Cell> getRelatedCells() {
        return relatedCells;
    }

    @Override
    public int getVersion() {
        return lastVersionUpdate;
    }

    @Override
    public void setLastVersionUpdate(int lastVersionUpdate) {
        this.lastVersionUpdate = lastVersionUpdate;
    }

    @Override
    public List<Cell> getAffectedCells() {
        return affectedCells;
    }


    //TODO!
    @Override
    public String toString() {
        return String.format(
                "Cell coordinates are: %s%n" +
                        "Effective Value: %s%n" +
                        "Original Value: %s%n" +
                        "Last Version Update: %d%n" +
                        "Related Cells: %s%n" +
                        "Affected Cells: %s",
                coordinate != null ? coordinate.toString() : "N/A",
                effectiveValue != null ? effectiveValue.getValue() : "N/A",
                originalValue != null ? originalValue : "N/A",
                lastVersionUpdate,
                relatedCells != null && !relatedCells.isEmpty() ? relatedCells.toString() : "[]",
                affectedCells != null && !affectedCells.isEmpty() ? affectedCells.toString() : "[]"
        );
    }


    public static int[] convertCellIdentifierToCoordinates(Sheet sheet, String cellIdentifier) {
        int rowIndex = 0;
        int columnIndex = 0;

        int i = 0;
        while (i < cellIdentifier.length() && Character.isLetter(cellIdentifier.charAt(i))) {
            columnIndex = columnIndex * 26 + (cellIdentifier.charAt(i) - 'A' + 1);
            i++;
        }

        rowIndex = Integer.parseInt(cellIdentifier.substring(i));

        if (rowIndex < 0 || rowIndex > sheet.getRowSize() ||
                columnIndex < 0 || columnIndex > sheet.getColSize()) {
            throw new IndexOutOfBoundsException();
        }
        return new int[]{columnIndex, rowIndex};
    }
}