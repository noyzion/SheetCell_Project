package shticell.engine.xmlParser;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import shticell.engine.sheet.api.Sheet;
import shticell.engine.sheet.cell.api.Cell;
import shticell.engine.sheet.cell.impl.CellImpl;
import shticell.engine.sheet.coordinate.Coordinate;
import shticell.engine.sheet.coordinate.CoordinateParser;
import shticell.engine.sheet.coordinate.ParseException;
import shticell.engine.sheet.impl.SheetImpl;
import shticell.engine.xmlParser.jaxb.STLCell;
import shticell.engine.xmlParser.jaxb.STLCells;
import shticell.engine.xmlParser.jaxb.STLSheet;

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
        Sheet sheet = new SheetImpl(name,rowSize,columnSize,colWidthUnits,rowHeightUnits);

        for (STLCell stlCell : stlCells.getSTLCell()) {
            String stringCord = stlCell.getColumn() + stlCell.getRow();
            Coordinate coordinate = CoordinateParser.parse(stringCord);
            Cell newCell = new CellImpl(coordinate, sheet.getColumnWidthUnits(), sheet.getRowsHeightUnits());
            sheet.addCell(newCell);
            String originalValue = stlCell.getSTLOriginalValue();
            sheet.onCellUpdated(originalValue, coordinate);
        }
        return sheet ;
    }
}
