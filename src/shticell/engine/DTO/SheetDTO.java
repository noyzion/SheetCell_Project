package shticell.engine.DTO;

import shticell.engine.sheet.api.Sheet;
import shticell.engine.sheet.cell.api.Cell;
import shticell.engine.sheet.coordinate.Coordinate;
import shticell.engine.sheet.impl.Edge;

import java.util.List;
import java.util.Map;

public class SheetDTO {

    private final String sheetName;
    private final int version;
    private final int rowSize;
    private final int columnSize;
    private final int columnWidthUnits;
    private final int rowsHeightUnits;
    private final Map<CoordinateDTO, CellDTO> cells;
    private final List<Edge> edges;

    public SheetDTO(String sheetName, int version, int rowSize, int columnSize,
                    int columnWidthUnits, int rowsHeightUnits,
                    Map<CoordinateDTO, CellDTO> cells, List<Edge> edges) {
        this.sheetName = sheetName;
        this.version = version;
        this.rowSize = rowSize;
        this.columnSize = columnSize;
        this.columnWidthUnits = columnWidthUnits;
        this.rowsHeightUnits = rowsHeightUnits;
        this.cells = cells;
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

    public Map<CoordinateDTO, CellDTO> getCells() {
        return cells;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    @Override
    public String toString() {
        StringBuilder outputString = new StringBuilder();
        outputString.append("   | ");

        for (int col = 0; col < columnSize; col++) {
            String colHeader = String.format("%-" + columnWidthUnits + "s", (char) ('A' + col));
            outputString.append(colHeader).append("| ");
        }
        outputString.append("\n");
        outputString.append(" ".repeat(3));
        outputString.append("-".repeat(columnWidthUnits * (columnSize + 1) - 4));
        outputString.append("\n");

        for (int row = 0; row < rowSize; row++) {
            String rowHeader = String.format("%02d | ", row + 1);
            outputString.append(rowHeader);

            for (int col = 0; col < columnSize; col++) {
                CoordinateDTO cellCoordinate = new CoordinateDTO(row, col + 1);
                String cellValue = cells.containsKey(cellCoordinate) ?
                        String.valueOf(cells.get(cellCoordinate).getEffectiveValue().getValue()) : " ";

                // Truncate cellValue if it exceeds the column width
                if (cellValue.length() > columnWidthUnits) {
                    cellValue = cellValue.substring(0, columnWidthUnits);
                }
                String formattedCellValue = centerText(cellValue, columnWidthUnits);
                outputString.append(formattedCellValue).append("| ");
            }

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

    private static String centerText(String text, int width) {
        if (text == null || width <= text.length()) {
            return text;
        }

        int padding = (width - text.length()) / 2;
        String format = "%" + padding + "s%s%" + (width - padding - text.length()) + "s";
        return String.format(format, "", text, "");
    }
}
