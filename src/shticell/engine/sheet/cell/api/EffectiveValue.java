package shticell.engine.sheet.cell.api;

import shticell.engine.sheet.api.Sheet;

public interface EffectiveValue {
    CellType getCellType();

    Object getValue();

    void calculateValue(Sheet sheet,String originalValue);



    <T> T extractValueWithExpectation(Class<T> type);
}