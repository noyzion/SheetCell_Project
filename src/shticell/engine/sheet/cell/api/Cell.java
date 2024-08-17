package shticell.engine.sheet.cell.api;

import shticell.engine.sheet.coordinate.Coordinate;

import java.util.List;

public interface Cell {

    public Coordinate getCoordinate();

    public String getOriginalValue();

    public void setOriginalValue(String value);

    public EffectiveValue getEffectiveValue();

    public int getVersion();

    public List<Coordinate> getRelatedCells();

    public List<Coordinate> getAffectedCells();

    public void addCellToAffectedCells(Coordinate coordinate);

    public void addCellToRelatedCells(Coordinate coordinate);

    public void setLastVersionUpdate(int lastVersionUpdate);


    }
