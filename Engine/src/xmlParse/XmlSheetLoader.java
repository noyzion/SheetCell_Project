package xmlParse;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import sheet.api.Sheet;
import sheet.cell.api.Cell;
import sheet.cell.impl.CellImpl;
import sheet.coordinate.Coordinate;
import sheet.coordinate.CoordinateParser;
import sheet.coordinate.ParseException;
import sheet.impl.SheetImpl;
import xmlParse.jaxb.STLCell;
import xmlParse.jaxb.STLCells;
import xmlParse.jaxb.STLSheet;

import java.io.File;


public class XmlSheetLoader {


    public static Sheet fromXmlFileToObject(String filePath) {

        try {
            XmlSheetValidator.validateXmlPath(filePath);
            File file = new File(filePath);
            XmlSheetValidator.isXmlFileExists(file);
            JAXBContext jaxbContext = JAXBContext.newInstance(STLSheet.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            STLSheet sheet = (STLSheet) jaxbUnmarshaller.unmarshal(file);

            XmlSheetValidator.validateSheetSize(sheet);
            XmlSheetValidator.validateCellsWithinBounds(sheet);
            return convert(sheet);

        } catch (JAXBException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Sheet convert(STLSheet stlSheet) throws ParseException {
        if (stlSheet == null) {
            return null;
        }

        int colWidthUnits = stlSheet.getSTLLayout().getSTLSize().getColumnWidthUnits();
        int rowHeightUnits = stlSheet.getSTLLayout().getSTLSize().getRowsHeightUnits();
        String name = stlSheet.getName();
        int rowSize = stlSheet.getSTLLayout().getRows();
        int columnSize = stlSheet.getSTLLayout().getColumns();
        STLCells stlCells = stlSheet.getSTLCells();
        Sheet sheet = new SheetImpl(name, rowSize, columnSize, colWidthUnits, rowHeightUnits, 1);

        for (STLCell stlCell : stlCells.getSTLCell()) {
            String stringCord = stlCell.getColumn() + stlCell.getRow();
            Coordinate coordinate = CoordinateParser.parse(stringCord);
            Cell newCell = new CellImpl(coordinate, sheet.getColumnWidthUnits(), sheet.getRowsHeightUnits());
            sheet.addCell(newCell);
            String originalValue = stlCell.getSTLOriginalValue();
            sheet.onCellUpdated(originalValue, coordinate);
        }
        sheet.setCounter(sheet.getCells().size());
        return sheet;
    }
}
