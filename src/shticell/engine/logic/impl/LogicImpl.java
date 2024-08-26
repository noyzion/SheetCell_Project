package shticell.engine.logic.impl;

import java.util.*;
import java.util.stream.Collectors;
import shticell.engine.DTO.CellDTO;
import shticell.engine.DTO.CoordinateDTO;
import shticell.engine.DTO.SheetDTO;
import shticell.engine.sheet.api.Sheet;
import shticell.engine.sheet.api.SheetReadActions;
import shticell.engine.sheet.cell.api.Cell;
import shticell.engine.sheet.cell.impl.CellImpl;
import shticell.engine.sheet.coordinate.Coordinate;
import shticell.engine.sheet.coordinate.CoordinateParser;
import shticell.engine.sheet.coordinate.ParseException;
import shticell.engine.sheet.impl.Edge;
import shticell.engine.sheet.impl.SheetImpl;

public class LogicImpl {

    private final List<Sheet> mainSheet = new ArrayList<>();


    public static CoordinateDTO toCoordinateDTO(Coordinate coordinate) {
        if (coordinate == null) {
            throw new IllegalArgumentException("Coordinate cannot be null.");
        }
        return new CoordinateDTO(coordinate.getRow(), coordinate.getColumn(), coordinate.getStringCord());
    }


    public static CellDTO toCellDTO(Cell cell) {
        if (cell == null) {
            throw new IllegalArgumentException("Cell cannot be null.");
        }
        return new CellDTO(
                toCoordinateDTO(cell.getCoordinate()),
                cell.getOriginalValue(),
                cell.getEffectiveValue() != null ? cell.getEffectiveValue() : null, // Handle null effective value
                cell.getVersion(),
                cell.getRelatedCells(),
                cell.getRelatedCells()
        );
    }

    public static SheetDTO toSheetDTO(SheetReadActions sheet) {
        Map<CoordinateDTO, CellDTO> cellDTOs = sheet.getCells().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> toCoordinateDTO(entry.getKey()),
                        entry -> toCellDTO(entry.getValue())));
        return new SheetDTO(
                sheet.getSheetName(),
                sheet.getVersion(),
                sheet.getRowSize(),
                sheet.getColSize(),
                sheet.getColumnWidthUnits(),
                sheet.getRowsHeightUnits(),
                cellDTOs,
                sheet.getEdges()
        );
    }

    public void setCellValue(String cellId, String value) throws ParseException {
        Coordinate coordinate = CoordinateParser.parse(cellId);
        if (mainSheet.isEmpty()) {
            throw new IllegalStateException("No sheets available to update.");
        }
        Sheet currentSheet = mainSheet.getLast();
        Sheet newSheet = createNewSheetFrom(currentSheet);
        newSheet.updateVersion();

        newSheet.onCellUpdated(value, coordinate);
        newSheet.getCell(coordinate).setVersion(newSheet.getVersion());
        for(Coordinate cord : newSheet.getCell(coordinate).getAffectedCells())
            newSheet.getCell(cord).setVersion(newSheet.getVersion());
        mainSheet.add(newSheet);
    }

    private Sheet createNewSheetFrom(Sheet oldSheet) {
        SheetImpl newSheet = new SheetImpl(
                oldSheet.getSheetName(),
                oldSheet.getRowSize(),
                oldSheet.getColSize(),
                oldSheet.getColumnWidthUnits(),
                oldSheet.getRowsHeightUnits(),
                oldSheet.getVersion()
        );

        for (Map.Entry<Coordinate, Cell> entry : oldSheet.getCells().entrySet()) {
            Cell oldCell = entry.getValue();

            Cell newCell = new CellImpl(oldCell);
            newSheet.addCell(newCell);
        }

        for (Edge edge : oldSheet.getEdges()) {
            Edge newEdge = new Edge(edge.getFrom(), edge.getTo()); // Adjust if needed
            newSheet.addEdge(newEdge);
        }
        return newSheet;
    }

    public void removeCell(String cellID) throws ParseException {
        Coordinate coordinate = CoordinateParser.parse(cellID);
        mainSheet.getLast().removeCell(coordinate);
    }

    public void addSheet(Sheet newSheet) {
        if (newSheet == null) {
            throw new IllegalArgumentException("New sheet cannot be null.");
        }
        mainSheet.add(newSheet);
    }

    public SheetDTO getSheetByVersion(int version) {
        Optional<Sheet> sheet = mainSheet.stream()
                .filter(s -> s.getVersion() == version)
                .findFirst();

        if (sheet.isEmpty()) {
            throw new IllegalArgumentException("No sheet found with version: " + version);
        }

        return toSheetDTO(sheet.get());
    }

    public SheetDTO getSheet() {
        Sheet sheet = mainSheet.getLast();
        String sheetName = sheet.getSheetName();
        int version = sheet.getVersion();
        int rowSize = sheet.getRowSize();
        int columnSize = sheet.getColSize();
        int columnWidthUnits = sheet.getColumnWidthUnits();
        int rowsHeightUnits = sheet.getRowsHeightUnits();

        Map<CoordinateDTO, CellDTO> cellDTOs = getCoordinateDTOCellDTOMap(sheet);

        List<Edge> edges = sheet.getEdges();

        return new SheetDTO(sheetName, version, rowSize, columnSize, columnWidthUnits, rowsHeightUnits, cellDTOs, edges);
    }

    private static Map<CoordinateDTO, CellDTO> getCoordinateDTOCellDTOMap(Sheet sheet) {
        Map<CoordinateDTO, CellDTO> cellDTOs = new HashMap<>();
        for (Map.Entry<Coordinate, Cell> entry : sheet.getCells().entrySet()) {
            Coordinate coordinate = entry.getKey();
            Cell cell = entry.getValue();
            CoordinateDTO coordinateDTO = new CoordinateDTO(coordinate.getRow(), coordinate.getColumn(), coordinate.getStringCord());
            CellDTO cellDTO = new CellDTO(coordinateDTO, cell.getOriginalValue(), cell.getEffectiveValue(), cell.getVersion(),
                    cell.getRelatedCells(), cell.getAffectedCells());
            cellDTOs.put(coordinateDTO, cellDTO);
        }
        return cellDTOs;
    }


}