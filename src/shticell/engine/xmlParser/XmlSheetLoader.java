package shticell.engine.xmlParser;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import shticell.engine.sheet.api.Sheet;
import shticell.engine.sheet.cell.api.Cell;
import shticell.engine.sheet.cell.impl.CellImpl;
import shticell.engine.sheet.coordinate.Coordinate;
import shticell.engine.sheet.coordinate.CoordinateImpl;
import shticell.engine.sheet.coordinate.CoordinateParser;
import shticell.engine.sheet.impl.SheetImpl;
import shticell.engine.xmlParser.jaxb.STLCell;
import shticell.engine.xmlParser.jaxb.STLCells;
import shticell.engine.xmlParser.jaxb.STLSheet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XmlSheetLoader {

    public static Sheet fromXmlFileToObject(String filePath) {
        System.out.println("\nFrom File to Object");

        Sheet sheet = null;
        try {
            XmlSheetValidator.validateXmlPath(filePath);
            File file = new File(filePath);
            if (file.exists()) {
                JAXBContext jaxbContext = JAXBContext.newInstance(STLSheet.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                STLSheet stlSheet = (STLSheet) jaxbUnmarshaller.unmarshal(file);
                sheet = convert(stlSheet); // Convert STLSheet to Sheet
            } else {
                System.out.println("File does not exist");
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return sheet;
    }

    public static Sheet convert(STLSheet stlSheet) {
        if (stlSheet == null) {
            return null;
        }

        // Convert STLSheet to Sheet
        String name = stlSheet.getName();
        int rowSize = stlSheet.getSTLLayout().getRows();
        int columnSize = stlSheet.getSTLLayout().getColumns();
        STLCells stlCells = stlSheet.getSTLCells();
        Sheet sheet = new SheetImpl(name,rowSize,columnSize);

        for (STLCell stlCell : stlCells.getSTLCell()) {
            String stringCoord = stlCell.getColumn() + String.valueOf(stlCell.getRow());
            Coordinate cord = CoordinateParser.parse(stringCoord);
            Cell newCell = new CellImpl(cord,sheet);
            sheet.addCell(newCell);
            String originalValue = stlCell.getSTLOriginalValue();
            newCell.setOriginalValue(originalValue);
        }
        // Create and return the new Sheet object
        return sheet ;
    }
}
