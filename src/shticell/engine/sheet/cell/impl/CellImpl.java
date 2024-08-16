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

    public CellImpl(int row, int column, String originalValue, EffectiveValue effectiveValue, int version, List<Cell> relatedOn, List<Cell> affectedOn) {
        this.coordinate = new CoordinateImpl(row, column);
        this.originalValue = originalValue;
        this.effectiveValue = effectiveValue;
        this.lastVersionUpdate = version;
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
                "[, effectiveValue=%s, originalValue=%s, lastVersionUpdate=%d, relatedCells=%s, affectedCells=%s]",
                effectiveValue,
                originalValue,
                lastVersionUpdate,
                relatedCells != null ? relatedCells.toString() : "[]",
                affectedCells != null ? affectedCells.toString() : "[]"
        );
    }
}
