package shticell.engine.sheet.impl;

import shticell.engine.sheet.api.Sheet;
import shticell.engine.sheet.cell.api.Cell;
import shticell.engine.sheet.cell.api.EffectiveValue;
import shticell.engine.sheet.cell.impl.CellImpl;
import shticell.engine.sheet.cell.impl.EffectiveValueImp;
import shticell.engine.sheet.coordinate.Coordinate;
import shticell.engine.sheet.coordinate.CoordinateFactory;

import java.io.Serializable;
import java.util.*;

public class SheetImpl implements Sheet, Serializable {
    private final Map<Coordinate, Cell> cells;
    private List<Edge> edges = new ArrayList<>();
    private final String sheetName;
    private int version;
    private final int rowSize;
    private final int columnSize;
    private final int columnWidthUnits;
    private final int rowsHeightUnits;
    private int counterChangedCells = 0;

    public SheetImpl(String sheetName, int rowSize, int columnSize, int columnWidthUnits, int rowsHeightUnits, int version) {
        this.sheetName = sheetName;
        this.cells = new HashMap<>();
        this.rowSize = rowSize;
        this.columnSize = columnSize;
        this.columnWidthUnits = columnWidthUnits;
        this.rowsHeightUnits = rowsHeightUnits;
        this.version = version;
    }

    @Override
    public int getCounterChangedCells() {
        return counterChangedCells;
    }

    @Override
    public void setCounter(int counter) {
        this.counterChangedCells = counter;
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
        Coordinate coordinate = createCoordinate(newCell);
        cells.put(coordinate, newCell);
    }

    @Override
    public Cell getCell(Coordinate coordinate) {
        return cells.get(coordinate);
    }

    @Override
    public void onCellUpdated(String originalValue, Coordinate coordinate) {
        Cell cell = cells.getOrDefault(coordinate, createNewCell(coordinate));
        String previousOriginalValue = cell.getOriginalValue();
        EffectiveValue previousEffectiveValue = cell.getEffectiveValue() != null ? cell.getEffectiveValue().copy() : null;

        try {
            cell.setOriginalValue(originalValue);
            removeDependence(cell);
            updateEffectiveValue(cell, originalValue);
            checkForChanges(cell, previousEffectiveValue);
            updateCells(coordinate);
            cell.setOriginalValue(originalValue);
        } catch (Exception e) {
            revertCellChanges(cell, previousOriginalValue, previousEffectiveValue);
            updateCells(coordinate);
            throw new IllegalArgumentException("Failed to update cell at " + coordinate.getStringCord() + " because of " + e.getMessage());
        }
    }

    @Override
    public void updateCells(Coordinate coordinate) {
        List<Cell> sortedCells = orderCellsForCalculation();
        for (Cell cell : sortedCells) {
            updateCellValue(cell);
        }
    }

    @Override
    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    @Override
    public List<Edge> getEdges() {
        return edges;
    }

    @Override
    public Map<Coordinate, Cell> getCells() {
        return cells;
    }

    public void removeEdge(Edge edge) {
        edges.remove(edge);
    }

    private Coordinate createCoordinate(Cell cell) {
        Coordinate cellCord = cell.getCoordinate();
        return CoordinateFactory.createCoordinate(this, cellCord.getRow(), cellCord.getColumn(), cellCord.getStringCord());
    }

    private Cell createNewCell(Coordinate coordinate) {
        Cell newCell = new CellImpl(coordinate, rowsHeightUnits, columnWidthUnits);
        addCell(newCell);
        return newCell;
    }

    private void updateEffectiveValue(Cell cell, String originalValue) {
        if (cell.getEffectiveValue() == null) {
            cell.setEffectiveValue(new EffectiveValueImp(cell.getCoordinate()));
        }
        cell.getEffectiveValue().calculateValue(this, originalValue);
    }

    private void checkForChanges(Cell cell, EffectiveValue previousEffectiveValue) {
        if (!cell.getEffectiveValue().equals(previousEffectiveValue)) {
            counterChangedCells++;
        }
    }

    private void revertCellChanges(Cell cell, String previousOriginalValue, EffectiveValue previousEffectiveValue) {
        cell.setOriginalValue(previousOriginalValue);
        cell.setEffectiveValue(previousEffectiveValue);
    }

    private void updateCellValue(Cell cell) {
        EffectiveValue previousEffectiveValue = cell.getEffectiveValue() != null ? cell.getEffectiveValue().copy() : null;
        try {
            if (cell.getEffectiveValue() != null) {
                cell.getEffectiveValue().calculateValue(this, cell.getOriginalValue());
                if (!cell.getEffectiveValue().equals(previousEffectiveValue)) {
                    removeDependence(cell);
                    counterChangedCells++;
                }
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Error while updating cell at " + cell.getCoordinate().getStringCord() + " because " + e.getMessage());
        }
    }

    private List<Cell> orderCellsForCalculation() {
        Map<Cell, List<Cell>> graph = buildGraph();
        Map<Cell, Integer> inDegree = calculateInDegrees(graph);
        List<Cell> orderedCells = performTopologicalSort(graph, inDegree);
        validateOrder(orderedCells, inDegree);
        return orderedCells;
    }

    private Map<Cell, List<Cell>> buildGraph() {
        Map<Cell, List<Cell>> graph = new HashMap<>();
        for (Edge edge : edges) {
            Cell fromCell = cells.get(edge.getFrom());
            Cell toCell = cells.get(edge.getTo());
            if (fromCell != null && toCell != null) {
                graph.computeIfAbsent(fromCell, k -> new ArrayList<>()).add(toCell);
            }
        }
        return graph;
    }

    private Map<Cell, Integer> calculateInDegrees(Map<Cell, List<Cell>> graph) {
        Map<Cell, Integer> inDegree = new HashMap<>();
        for (Cell cell : cells.values()) {
            inDegree.put(cell, 0);
        }
        for (List<Cell> neighbors : graph.values()) {
            for (Cell neighbor : neighbors) {
                inDegree.put(neighbor, inDegree.get(neighbor) + 1);
            }
        }
        return inDegree;
    }

    private List<Cell> performTopologicalSort(Map<Cell, List<Cell>> graph, Map<Cell, Integer> inDegree) {
        List<Cell> orderedCells = new ArrayList<>();
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
        return orderedCells;
    }

    private void validateOrder(List<Cell> orderedCells, Map<Cell, Integer> inDegree) {
        if (orderedCells.size() != inDegree.size()) {
            throw new IllegalStateException("Circular dependency detected");
        }
    }

    private void removeDependence(Cell cell) {
        for (Coordinate cord : cell.getRelatedCells()) {
            Cell dependentCell = this.getCell(cord);
            if (dependentCell != null) {
                dependentCell.getAffectedCells().remove(cell.getCoordinate());
                this.removeEdge(new Edge(cord, cell.getCoordinate()));
            }
        }
        cell.getRelatedCells().clear();
    }
}
