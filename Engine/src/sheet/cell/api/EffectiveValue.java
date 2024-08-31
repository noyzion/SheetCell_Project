package sheet.cell.api;

import sheet.api.Sheet;

public interface EffectiveValue {
    CellType getCellType();

    Object getValue();

    void calculateValue(Sheet sheet, String originalValue);

    EffectiveValue copy();
}