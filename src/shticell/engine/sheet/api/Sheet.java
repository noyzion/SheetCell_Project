package shticell.engine.sheet.api;

import shticell.engine.sheet.cell.api.Cell;
import shticell.engine.sheet.coordinate.Coordinate;

public interface Sheet {

    public void setVersion(int version);
    public int getVersion();
    public String getSheetName();
    public void addCell(Cell newCell);
    public Cell getCell(Coordinate coordinate);

}
