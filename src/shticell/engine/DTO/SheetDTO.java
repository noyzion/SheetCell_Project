package shticell.engine.DTO;

import shticell.engine.sheet.api.Sheet;
import shticell.engine.sheet.cell.api.Cell;
import shticell.engine.sheet.coordinate.Coordinate;
import shticell.engine.sheet.impl.Edge;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SheetDTO {
    private final String sheetName;
    private final int version;
    private final int rowSize;
    private final int columnSize;
    private final int columnWidthUnits;
    private final int rowsHeightUnits;
    private final Map<Coordinate, Cell> cells;
    private final List<Edge> edges;

    public SheetDTO(String sheetName, int version, int rowSize, int colSize, int columnWidthUnits, int rowsHeightUnits, Map<CoordinateDTO, CellDTO> cellDTOs, List<Edge> edges) {
        this.sheetName = sheetName;
        this.version = version;
        this.rowSize = rowSize;
        this.columnSize = colSize;
        this.columnWidthUnits = columnWidthUnits;
        this.rowsHeightUnits = rowsHeightUnits;
        this.cells = new HashMap<Coordinate, Cell>();
        this.edges = edges;
    }

    public String getSheetName() {
        return sheetName;
    }

    public int getVersion() {
        return version;
    }

    public int getRowSize() {
        return rowSize;
    }

    public int getColumnSize() {
        return columnSize;
    }

    public int getColumnWidthUnits() {
        return columnWidthUnits;
    }

    public int getRowsHeightUnits() {
        return rowsHeightUnits;
    }

    public Map<Coordinate, Cell> getCells() {
        return cells;
    }

    public List<Edge> getEdges() {
        return edges;
    }
}