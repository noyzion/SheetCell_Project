package sheet.coordinate;

public class ParseException extends Exception {
        private final int errorOffset;

        public ParseException(String message, int errorOffset) {
            super(message);
            this.errorOffset = errorOffset;
        }

        @Override
        public String toString() {
            return super.toString() + " (at offset " + errorOffset + ")";
        }
    }
