package shticell.engine.sheet.impl;

import shticell.engine.sheet.api.Sheet;
import shticell.engine.sheet.cell.api.Cell;
import shticell.engine.sheet.cell.impl.EffectiveValueImp;
import shticell.engine.sheet.coordinate.Coordinate;
import shticell.engine.sheet.coordinate.CoordinateFactory;
import shticell.engine.sheet.coordinate.CoordinateImpl;

import java.util.HashMap;
import java.util.Map;

public class SheetImpl implements Sheet {
    private final Map<Coordinate, Cell> cells;
    private final String sheetName;
    private int version;
    private final int rowSize;
    private final int columnSize;
    private final int columnWidthUnits;
    private final int rowsHeightUnits;


    public SheetImpl(String sheetName, int rowSize, int columnSize, int columnWidthUnits, int rowsHeightUnits) {
        this.sheetName = sheetName;
        this.version = 1;
        this.cells = new HashMap<>();
        this.rowSize = rowSize;
        this.columnSize = columnSize;
        this.columnWidthUnits = columnWidthUnits;
        this.rowsHeightUnits = rowsHeightUnits;
    }

    @Override
    public int getColumnWidthUnits() {
        return columnWidthUnits;
    }

    @Override
    public int getRowsHeightUnits() {
        return rowsHeightUnits;
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
        StringBuilder outputString = new StringBuilder();

        // Column headers
        outputString.append("   | ");
        for (int col = 0; col < columnSize; col++) {
            String colHeader = String.format("%-" + columnWidthUnits + "s", (char) ('A' + col));
            outputString.append(colHeader).append("| ");
        }
        outputString.append("\n");

        // Separator line
        outputString.append("   | ");
        outputString.append("-".repeat(columnSize * (columnWidthUnits + 1) + 1)); // Adjusted for column width
        outputString.append("\n");

        // Rows
        for (int row = 0; row < rowSize; row++) {
            // Row header
            String rowHeader = String.format("%02d | ", row + 1);
            outputString.append(rowHeader);

            // Cell values
            for (int col = 0; col < columnSize; col++) {
                Coordinate cellCoordinate = CoordinateFactory.createCoordinate(this,row, col);
                String cellValue = cells.get(cellCoordinate) != null
                        ? cells.get(cellCoordinate).getEffectiveValue().getValue().toString()
                        : " ";

                String formattedCellValue = String.format("%-" + columnWidthUnits + "s", cellValue);
                outputString.append(formattedCellValue).append("| ");
            }

            // Add additional rows height spacing for visual clarity
            for (int i = 1; i < rowsHeightUnits; i++) {
                outputString.append("\n");
                outputString.append("   | ");
                for (int col = 0; col < columnSize; col++) {
                    outputString.append(String.format("%-" + columnWidthUnits + "s", "")).append("| ");
                }
            }

            outputString.append("\n");
        }

        return outputString.toString();
    }

    public static String centerText(String text, int width) {
        if (text == null || width <= text.length()) {
            return text;
        }

        int padding = (width - text.length()) / 2;
        String format = "%" + padding + "s%s%" + (width - padding - text.length()) + "s";
        return String.format(format, "", text, "");
    }

    @Override
    public void onCellUpdated(String originalValue, Coordinate coordinate) {

        Cell cell = cells.get(coordinate);
        cell.setOriginalValue(originalValue);

        if (cell.getEffectiveValue() == null) {
            cell.setEffectiveValue( new EffectiveValueImp(coordinate));
        }
        cell.getEffectiveValue().calculateValue(this, originalValue);

        if (!cell.isInBounds()) {
            throw new IndexOutOfBoundsException("The content of cell at coordinate " + coordinate + " exceeds the allowed cell size.");
        }

        for (Coordinate cord : cell.getAffectedCells()) {
            Cell affectedCell = getCell(cord);
            if (affectedCell != null) {
                affectedCell.getEffectiveValue().calculateValue(this, affectedCell.getOriginalValue());
                if (!affectedCell.isInBounds()) {
                    throw new IndexOutOfBoundsException("The content of cell at coordinate " + coordinate + " exceeds the allowed cell size.");
                }
                affectedCell.updateVersion();
            }
        }

    }
}