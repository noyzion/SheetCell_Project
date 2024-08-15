package shticell.engine.sheet.cell;

import shticell.engine.sheet.coordinate.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class CellImpl implements Cell
{
    private Object effectiveValue;
    private String originalValue;
    private Coordinate coordinate;
    private List<Cell> relatedCells = new ArrayList<Cell>();
    private List<Cell> affectedCells = new ArrayList<>();
    private int lastVersionUpdate;

    @Override
    public Coordinate getCoordinate()
    {
        return coordinate;
    }

    @Override
    public void setOriginalValue(String originalValue) {
        this.originalValue = originalValue;
        this.effectiveValue = originalValue;
    }

    @Override
    public String getOriginalValue() {
        return originalValue;
    }

    @Override
    public Object getEffectiveValue() {
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
