package shticell.engine.sheet.cell.api;

import shticell.engine.sheet.coordinate.Coordinate;

import java.util.List;

public interface Cell {

    public Coordinate getCoordinate();

    public String getOriginalValue();

    public void setOriginalValue(String value);

    public EffectiveValue getEffectiveValue();

    public int getVersion();

    public List<Cell> getRelatedCells();

    public List<Cell> getAffectedCells();

    public void addCellToAffectedCells(Cell cell);

    public void addCellToRelatedCells(Cell cell);

    public void setLastVersionUpdate(int lastVersionUpdate);


    }
