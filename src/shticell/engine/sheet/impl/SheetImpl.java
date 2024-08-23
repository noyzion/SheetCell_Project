package shticell.engine.sheet.impl;

import shticell.engine.sheet.api.Sheet;
import shticell.engine.sheet.api.SheetReadActions;
import shticell.engine.sheet.api.SheetUpdateActions;
import shticell.engine.sheet.cell.api.Cell;
import shticell.engine.sheet.cell.impl.EffectiveValueImp;
import shticell.engine.sheet.coordinate.Coordinate;
import shticell.engine.sheet.coordinate.CoordinateFactory;

import java.util.*;

public class SheetImpl implements Sheet {
    private final Map<Coordinate, Cell> cells;
    private final List<Edge> edges = new ArrayList<>(); // רשימת קשתות (רק לתאים עם REF)
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

        outputString.append("   | ");
        for (int col = 0; col < columnSize; col++) {
            String colHeader = String.format("%-" + columnWidthUnits + "s", (char) ('A' + col));
            outputString.append(colHeader).append("| ");
        }
        outputString.append("\n");

        outputString.append("   | ");
        outputString.append("-".repeat(columnSize * (columnWidthUnits + 1) + 1)); // Adjusted for column width
        outputString.append("\n");

        for (int row = 0; row < rowSize; row++) {
            String rowHeader = String.format("%02d | ", row + 1);
            outputString.append(rowHeader);

            for (int col = 0; col < columnSize; col++) {
                Coordinate cellCoordinate = CoordinateFactory.createCoordinate(this, row, col);
                String cellValue = cells.get(cellCoordinate) != null
                        ? cells.get(cellCoordinate).getEffectiveValue().getValue().toString()
                        : " ";

                // Truncate cellValue if it exceeds the column width
                if (cellValue.length() > columnWidthUnits) {
                    cellValue = cellValue.substring(0, columnWidthUnits);
                }

                String formattedCellValue = String.format("%-" + columnWidthUnits + "s", cellValue);
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
            cell.setEffectiveValue(new EffectiveValueImp(coordinate));
        }
        cell.getEffectiveValue().calculateValue(this, originalValue);

        updateCells();
    }


    @Override
    public void updateCells() {
        List<Cell> sortedCells = orderCellsForCalculation();
        for (Cell cell : sortedCells) {
            cell.getEffectiveValue().calculateValue(this, cell.getOriginalValue());
        }
    }

    private List<Cell> orderCellsForCalculation() {
        Map<Cell, List<Cell>> graph = new HashMap<>();
        Map<Cell, Integer> inDegree = new HashMap<>();
        List<Cell> orderedCells = new ArrayList<>();

        for (Cell cell : cells.values()) {
            inDegree.put(cell, 0);
        }

        for (Edge edge : edges) {
            Cell fromCell = cells.get(edge.getFrom());
            Cell toCell = cells.get(edge.getTo());

            if (fromCell != null && toCell != null) {
                graph.computeIfAbsent(fromCell, k -> new ArrayList<>()).add(toCell);
                inDegree.put(toCell, inDegree.get(toCell) + 1);
            }
        }

        Queue<Cell> queue = new LinkedList<>();

        for (Map.Entry<Cell, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        while (!queue.isEmpty()) {
            Cell current = queue.poll();
            orderedCells.add(current);

            List<Cell> neighbors = graph.getOrDefault(current, new ArrayList<>());
            for (Cell neighbor : neighbors) {
                int newInDegree = inDegree.get(neighbor) - 1;
                inDegree.put(neighbor, newInDegree);

                if (newInDegree == 0) {
                    queue.add(neighbor);
                }
            }
        }

        if (orderedCells.size() != inDegree.size()) {
            throw new IllegalStateException("Circular dependency detected");
        }

        return orderedCells;
    }


    @Override
    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    @Override
    public Map<Coordinate, Cell> getCells()
    {
        return cells;
    }

    @Override
    public List<Edge> getEdges()
    {
        return edges;
    }

}