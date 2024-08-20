package shticell.engine.xmlParser;

import shticell.engine.xmlParser.jaxb.STLCell;
import shticell.engine.xmlParser.jaxb.STLLayout;
import shticell.engine.xmlParser.jaxb.STLSheet;


public class XmlSheetValidator {

    public static void validateXmlPath(String filePath) {
        if (!filePath.endsWith(".xml")) {
            throw new IllegalArgumentException("The file does not exist or is not an XML file.");
        }

        if (!isOnlyValidChars(filePath)) {
            throw new IllegalArgumentException("The file contains invalid characters.");
        }

        if (filePath.contains(" ")) {
            filePath = handleSpacesInPath(filePath);
        }
    }

    public static boolean isOnlyValidChars(String path) {
        return path.matches("^[ a-zA-Z0-9_\\\\:\\-\\.]+$");
    }

    private static String handleSpacesInPath(String path) {
        return "\"" + path + "\"";
    }

    public static boolean isValidSheetSize(STLSheet sheet) {
        STLLayout layout = sheet.getSTLLayout();
        int rows = layout.getRows();
        int columns = layout.getColumns();

        return rows >= 1 && rows <= 50 && columns >= 1 && columns <= 20;
    }

    private static boolean areCellsWithinBounds(STLSheet sheet) {
        STLLayout layout = sheet.getSTLLayout();
        int maxRows = layout.getRows();
        int maxColumns = layout.getColumns();

        for (STLCell cell : sheet.getSTLCells().getSTLCell()) {
            int row = cell.getRow();
            int column = columnToIndex(cell.getColumn());

            if (row < 1 || row > maxRows || column < 1 || column > maxColumns) {
                return false;
            }
        }
        return true;
    }

    private static int columnToIndex(String column) {
        return column.charAt(0) - 'A' + 1;
    }
}