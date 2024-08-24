package shticell.engine.sheet.coordinate;

import shticell.engine.sheet.api.Sheet;

import java.util.HashMap;
import java.util.Map;

public class CoordinateFactory {

    private static final Map<String, Coordinate> cachedCoordinates = new HashMap<>();

    public static String getKey(Coordinate coordinate) {
        return coordinate.getRow() + ":" + coordinate.getColumn();
    }

    public static Coordinate createCoordinate(Sheet sheet, int row, int column) throws IndexOutOfBoundsException {
        if (row < 0 || row >= sheet.getRowSize() || column < 0 || column >= sheet.getColSize()) {
            throw new IndexOutOfBoundsException("Please enter a valid row/column number, between 1 and " +
                    (sheet.getRowSize()) + " for rows and " +
                    convertIndexToColumnLetter(sheet.getColSize()) + " for columns.");
        }

        String key = row + ":" + column;

        if (cachedCoordinates.containsKey(key)) {
            return cachedCoordinates.get(key);
        }

        CoordinateImpl coordinate = new CoordinateImpl(row, column);
        cachedCoordinates.put(key, coordinate);

        return coordinate;
    }

    public static boolean isValidCoordinateFormat(String input) throws IllegalArgumentException {
        if (!input.matches("[A-Z]+\\d+")) {
            throw new IllegalArgumentException("Invalid format. Coordinate should be in the format (e.g., A5).");
        }
        return true;
    }

    public static int convertColumnLetterToIndex(String columnLetter) throws IllegalArgumentException {
        if (columnLetter == null || columnLetter.isEmpty()) {
            throw new IllegalArgumentException("Column letter cannot be null or empty.");
        }

        int columnIndex = 0;
        for (char letter : columnLetter.toCharArray()) {
            if (letter < 'A' || letter > 'Z') {
                throw new IllegalArgumentException("Column letter should only contain uppercase letters.");
            }
            columnIndex = columnIndex * 26 + (letter - 'A');
        }
        return columnIndex;
    }

    public static String convertIndexToColumnLetter(int columnIndex) throws IllegalArgumentException {
        if (columnIndex < 0) {
            throw new IllegalArgumentException("Column index cannot be negative.");
        }

        StringBuilder columnLetter = new StringBuilder();
        while (columnIndex >= 0) {
            columnLetter.insert(0, (char) ('A' + (columnIndex % 26)));
            columnIndex = (columnIndex / 26) - 1;
        }
        return columnLetter.toString();
    }

    public static boolean isCoordinateWithinBounds(int rowSize, int colSize, String coordinate) throws ParseException, IllegalArgumentException {
        Coordinate cord = CoordinateParser.parse(coordinate);

        if (cord.getRow() < 0 || cord.getRow() >= rowSize) {
            throw new IllegalArgumentException("Row index out of bounds. Valid row indices are from 1 to " + (rowSize) + ".");
        }
        if (cord.getColumn() < 0 || cord.getColumn() >= colSize) {
            throw new IllegalArgumentException("Column index out of bounds. Valid column indices are from 1 to " + (colSize) + ".");
        }
        return true;
    }

}
