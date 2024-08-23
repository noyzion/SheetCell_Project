package shticell.engine.DTO;

import shticell.engine.sheet.cell.api.EffectiveValue;
import shticell.engine.sheet.coordinate.Coordinate;

import java.util.List;

public class CellDTO {
    private final CoordinateDTO coordinate;
    private final String originalValue;
    private final EffectiveValue effectiveValue;
    private final int lastVersionUpdate;
    private final List<Coordinate> relatedCells;
    private final List<Coordinate> affectedCells;

    public CellDTO(CoordinateDTO coordinate, String originalValue, EffectiveValue effectiveValue, int lastVersionUpdate, List<Coordinate> relatedCells, List<Coordinate> affectedCells) {
        this.coordinate = coordinate;
        this.originalValue = originalValue;
        this.effectiveValue = effectiveValue;
        this.lastVersionUpdate = lastVersionUpdate;
        this.relatedCells = relatedCells;
        this.affectedCells = affectedCells;
    }

    public CoordinateDTO getCoordinateDTO() {
        return coordinate;
    }

    public String getOriginalValue() {
        return originalValue;
    }

    public EffectiveValue getEffectiveValue() {
        return effectiveValue;
    }

    public int getLastVersionUpdate() {
        return lastVersionUpdate;
    }

    public List<Coordinate> getRelatedCells() {
        return relatedCells;
    }

    public List<Coordinate> getAffectedCells() {
        return affectedCells;
    }
}