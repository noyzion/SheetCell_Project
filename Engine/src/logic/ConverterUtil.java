package logic;

import DTO.CellDTO;
import DTO.CoordinateDTO;
import DTO.SheetDTO;
import sheet.api.SheetReadActions;
import sheet.cell.api.Cell;
import sheet.coordinate.Coordinate;
import java.util.Map;
import java.util.stream.Collectors;

public class ConverterUtil {

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
                cell.getEffectiveValue() != null ? cell.getEffectiveValue() : null,
                cell.getVersion(),
                cell.getRelatedCells(),
                cell.getAffectedCells()
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
                sheet.getEdges(),
                sheet.getCounterChangedCells());
    }

}
