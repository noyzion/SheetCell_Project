package shticell.engine.sheet.cell.impl;

import shticell.engine.sheet.cell.api.Cell;
import shticell.engine.sheet.cell.api.EffectiveValue;
import shticell.engine.sheet.coordinate.Coordinate;
import shticell.engine.sheet.coordinate.CoordinateImpl;

import java.util.ArrayList;
import java.util.List;

public class CellImpl implements Cell {
    private EffectiveValue effectiveValue;
    private String originalValue;
    private final Coordinate coordinate;
    private List<Cell> relatedCells = new ArrayList<Cell>();
    private List<Cell> affectedCells = new ArrayList<>();
    private int lastVersionUpdate;

    public CellImpl(int row, int column, String originalValue, int version, List<Cell> relatedOn, List<Cell> affectedOn) {
        this.coordinate = new CoordinateImpl(row, column);
        this.originalValue = originalValue;
        this.effectiveValue = new EffectiveValueImp();
        this.effectiveValue.calculateValue(originalValue);
        this.lastVersionUpdate = 1;
        this.relatedCells = relatedOn;
        this.affectedCells = affectedOn;
    }

    @Override
    public Coordinate getCoordinate() {
        return coordinate;
    }

    @Override
    public void setOriginalValue(String originalValue) {
        this.originalValue = originalValue;
        effectiveValue.calculateValue(originalValue);
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
}