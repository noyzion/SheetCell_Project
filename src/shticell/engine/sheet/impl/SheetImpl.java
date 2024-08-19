package shticell.engine.sheet.impl;

import shticell.engine.sheet.api.Sheet;
import shticell.engine.sheet.cell.api.Cell;
import shticell.engine.sheet.coordinate.Coordinate;
import shticell.engine.sheet.coordinate.CoordinateFactory;

import java.util.HashMap;
import java.util.Map;

public class SheetImpl implements Sheet {
    private Map<Coordinate, Cell> cells = new HashMap<Coordinate, Cell>();
    private final String sheetName;
    private int version;
    private final int rowSize;
    private final int columnSize;

    public SheetImpl(String sheetName, int rowSize, int columnSize) {
        this.sheetName = sheetName;
        this.version = 1;
        this.cells = new HashMap<Coordinate, Cell>();
        this.rowSize = rowSize;
        this.columnSize = columnSize;
    }

    @Override
    public int getRowSize() {
        return rowSize;
    }

    @Override
    public int getColSize() {
        return columnSize;
    }

    @Override
    public void updateVersion() {
        this.version++;
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public String getSheetName() {
        return sheetName;
    }

    @Override
    public void addCell(Cell newCell) {
        Coordinate cellCord = newCell.getCoordinate();
        Coordinate coordinate = CoordinateFactory.createCoordinate(this, cellCord.getRow(), cellCord.getColumn());
        cells.put(coordinate, newCell);
    }

    public Cell getCell(Coordinate coordinate) {
        return cells.get(coordinate);
    }

    public Cell removeCell(Coordinate coordinate) {
        return cells.remove(coordinate);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Sheet name: ").append(sheetName).append("\n")
                .append("sheet version is ").append(version).append("\n");
        sb.append("   ");
        for (int col = 0; col < columnSize; col++) {
            sb.append(String.format("%-5s", (char) ('A' + col))); // Column headers
            if (col < columnSize - 1) {
                sb.append("|");
            }
        }
        sb.append("\n");

        for (int row = 0; row < rowSize; row++) {
            sb.append(String.format("%02d ", row + 1));
            for (int col = 0; col < columnSize; col++) {
                Coordinate coordinate = CoordinateFactory.createCoordinate(this, row, col);
                Cell cell = getCell(coordinate);
                String cellValue = (cell != null && cell.getEffectiveValue().getValue() != null)
                        ? cell.getEffectiveValue().getValue().toString()
                        : "";
                sb.append(String.format("%-5s", cellValue));
                if (col < columnSize - 1) {
                    sb.append("|");
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }

}