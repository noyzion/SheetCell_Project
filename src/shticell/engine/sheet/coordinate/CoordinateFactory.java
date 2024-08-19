package shticell.engine.sheet.coordinate;

import shticell.engine.sheet.api.Sheet;

import java.util.HashMap;
import java.util.Map;

public class CoordinateFactory {

    private static final Map<String, Coordinate> cachedCoordinates = new HashMap<>();

    public static String getKey(Coordinate coordinate) {
        // Generate the key in the same format as used in createCoordinate
        return coordinate.getRow() + ":" + coordinate.getColumn();
    }

    public static Coordinate createCoordinate(Sheet sheet,int row, int column) throws IndexOutOfBoundsException {

        if(row< 0 || row > sheet.getRowSize()
        || column < 0 || column > sheet.getColSize()) {
            throw new IndexOutOfBoundsException();
        }
        String key = row + ":" + column;
        if (cachedCoordinates.containsKey(key)) {
            return cachedCoordinates.get(key);
        }

        CoordinateImpl coordinate = new CoordinateImpl(row, column);
        cachedCoordinates.put(key, coordinate);

        return coordinate;
    }
}
