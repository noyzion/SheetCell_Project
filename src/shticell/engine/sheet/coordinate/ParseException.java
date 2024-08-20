package shticell.engine.sheet.coordinate;

public class ParseException extends Exception {
        private int errorOffset;

        public ParseException(String message, int errorOffset) {
            super(message);
            this.errorOffset = errorOffset;
        }

        public int getErrorOffset() {
            return errorOffset;
        }

        @Override
        public String toString() {
            return super.toString() + " (at offset " + errorOffset + ")";
        }
    }
