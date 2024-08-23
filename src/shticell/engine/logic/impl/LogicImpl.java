package shticell.engine.logic.impl;

import java.util.Map;
import java.util.stream.Collectors;
import shticell.engine.DTO.CellDTO;
import shticell.engine.DTO.CoordinateDTO;
import shticell.engine.DTO.SheetDTO;
import shticell.engine.sheet.api.Sheet;
import shticell.engine.sheet.cell.api.Cell;
import shticell.engine.sheet.coordinate.Coordinate;

public class LogicImpl {

    public static CoordinateDTO toCoordinateDTO(Coordinate coordinate) {
        if (coordinate == null) {
            throw new IllegalArgumentException("Coordinate cannot be null.");
        }
        return new CoordinateDTO(coordinate.getRow(), coordinate.getColumn());
    }


    public static CellDTO toCellDTO(Cell cell) {
        if (cell == null) {
            throw new IllegalArgumentException("Cell cannot be null.");
        }
        return new CellDTO(
                toCoordinateDTO(cell.getCoordinate()), // Convert to CoordinateDTO
                cell.getOriginalValue(),
                cell.getEffectiveValue() != null ? cell.getEffectiveValue().getValue() : null, // Handle null effective value
                cell.getVersion(),
                cell.getRelatedCells(),
                cell.getRelatedCells()
        );
    }

    public static SheetDTO toSheetDTO(Sheet sheet) {
        if (sheet == null) {
            throw new IllegalArgumentException("Sheet cannot be null.");
        }

        Map<CoordinateDTO, CellDTO> cellDTOs = sheet.getCells().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> toCoordinateDTO(entry.getKey()), // Convert keys to CoordinateDTO
                        entry -> toCellDTO(entry.getValue())      // Convert values to CellDTO
                ));

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
}