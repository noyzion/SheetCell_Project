package shticell.engine.sheet.coordinate;

public class CoordinateParser {

    public static Coordinate parse(String coordinateString) throws IllegalArgumentException {
        if (coordinateString == null || coordinateString.isEmpty()) {
            throw new IllegalArgumentException("Coordinate string cannot be null or empty.");
        }

        coordinateString = coordinateString.toUpperCase().trim();
        int row = 0;
        int col = 0;

        // Extract the column (letters) and row (digits) from the string
        int i = 0;
        while (i < coordinateString.length() && Character.isLetter(coordinateString.charAt(i))) {
            col = col * 26 + (coordinateString.charAt(i) - 'A' + 1);
            i++;
        }

        while (i < coordinateString.length() && Character.isDigit(coordinateString.charAt(i))) {
            row = row * 10 + (coordinateString.charAt(i) - '0');
            i++;
        }

        if (row <= 0 || col <= 0) {
            throw new IllegalArgumentException("Invalid coordinate string: " + coordinateString);
        }

        // Convert 1-based row and col to 0-based indices (if your implementation uses 0-based indices)
        return new CoordinateImpl(row - 1, col - 1);
    }
}
