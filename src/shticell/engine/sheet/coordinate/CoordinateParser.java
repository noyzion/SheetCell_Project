package shticell.engine.sheet.coordinate;

public class CoordinateParser {

        public static Coordinate parse(String coordinateString) throws ParseException {
            if (coordinateString == null || coordinateString.isEmpty()) {
                throw new ParseException("Coordinate input cannot be null or empty.", 0);
            }

            coordinateString = coordinateString.toUpperCase().trim();
            int row = 0;
            int col = 0;

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
                throw new ParseException("Invalid coordinate input: " + coordinateString, i);
            }

            return new CoordinateImpl(row-1 , col-1 ,coordinateString);
        }
    }


