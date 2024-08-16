package shticell.engine.sheet.cell.api;

public interface EffectiveValue {
    CellType getCellType();

    Object getValue();

    void calculateValue(String originalValue);



    <T> T extractValueWithExpectation(Class<T> type);
}