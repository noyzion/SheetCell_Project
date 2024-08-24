package shticell.engine.sheet.impl;

import com.sun.tools.xjc.reader.gbind.Graph;
import shticell.engine.sheet.api.Sheet;
import shticell.engine.sheet.api.SheetReadActions;
import shticell.engine.sheet.api.SheetUpdateActions;
import shticell.engine.sheet.cell.api.Cell;
import shticell.engine.sheet.cell.impl.CellImpl;
import shticell.engine.sheet.cell.impl.EffectiveValueImp;
import shticell.engine.sheet.coordinate.Coordinate;
import shticell.engine.sheet.coordinate.CoordinateFactory;
import shticell.engine.sheet.coordinate.CoordinateParser;
import shticell.engine.sheet.coordinate.ParseException;

import java.util.*;

public class SheetImpl implements Sheet {
    private final Map<Coordinate, Cell> cells;
    private List<Edge> edges = new ArrayList<>(); // רשימת קשתות (רק לתאים עם REF)
    private final String sheetName;
    private int version = 1;
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

    public SheetImpl(SheetImpl other) {
        this.sheetName = other.sheetName;
        this.columnWidthUnits = other.columnWidthUnits;
        this.rowsHeightUnits = other.rowsHeightUnits;
        this.rowSize = other.rowSize;
        this.columnSize = other.columnSize;

        // Deep copy of cells
        this.cells = new HashMap<>();
        for (Map.Entry<Coordinate, Cell> entry : other.cells.entrySet()) {
            this.cells.put(entry.getKey(), new CellImpl((CellImpl) entry.getValue()));
        }

        // Deep copy of edges
        this.edges = new ArrayList<>(other.edges); // Assuming Edge has a proper copy mechanism
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
        Coordinate coordinate = CoordinateFactory.createCoordinate(this, cellCord.getRow(), cellCord.getColumn(),cellCord.getStringCord());
        cells.put(coordinate, newCell);
    }

    public Cell getCell(Coordinate coordinate) {
        return cells.get(coordinate);
    }

    public Cell removeCell(Coordinate coordinate) {
        return cells.remove(coordinate);
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