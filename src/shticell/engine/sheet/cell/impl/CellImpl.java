package shticell.engine.sheet.cell.impl;

import shticell.engine.sheet.api.Sheet;
import shticell.engine.sheet.cell.api.Cell;
import shticell.engine.sheet.cell.api.CellType;
import shticell.engine.sheet.cell.api.EffectiveValue;
import shticell.engine.sheet.coordinate.Coordinate;
import shticell.engine.sheet.coordinate.CoordinateImpl;

import java.util.ArrayList;
import java.util.List;

public class CellImpl implements Cell {
    private final Sheet mySheet;
    private EffectiveValue effectiveValue;
    private String originalValue;
    private final Coordinate coordinate;
    private final List<Coordinate> relatedCells = new ArrayList<>();
    private final List<Coordinate> affectedCells = new ArrayList<>();
    private int lastVersionUpdate;

    public CellImpl(Coordinate coordinate, Sheet sheet) {
        this.mySheet = sheet;
        this.coordinate = coordinate;
    }

    @Override
    public Coordinate getCoordinate() {
        return coordinate;
    }

    @Override
    public void setOriginalValue(String originalValue) {
        this.originalValue = originalValue;
        if(effectiveValue == null) {
            effectiveValue= new EffectiveValueImp(this.coordinate);
        }
        effectiveValue.calculateValue(mySheet, originalValue);
        for (Coordinate cord : affectedCells) {
            Cell cell = mySheet.getCell(cord);
            cell.getEffectiveValue().calculateValue(mySheet, cell.getOriginalValue());
            cell.updateVersion();
        }
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
    public void addCellToRelatedCells(Coordinate coordinate) {
        if (!relatedCells.contains(coordinate)) {
            relatedCells.add(coordinate);
        }
    }

    @Override
    public void addCellToAffectedCells(Coordinate coordinate) {
        if (!affectedCells.contains(coordinate)) {
            affectedCells.add(coordinate);
        }
    }


    @Override
    public List<Coordinate> getRelatedCells() {
        return relatedCells;
    }

    @Override
    public int getVersion() {
        return lastVersionUpdate;
    }

    @Override
    public void updateVersion() {
        this.lastVersionUpdate++;
    }

    @Override
    public List<Coordinate> getAffectedCells() {
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