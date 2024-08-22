package shticell.ui;

import shticell.engine.menu.Menu;
import shticell.engine.sheet.api.Sheet;
import shticell.engine.sheet.cell.api.Cell;
import shticell.engine.sheet.cell.impl.CellImpl;
import shticell.engine.sheet.coordinate.Coordinate;
import shticell.engine.sheet.coordinate.CoordinateParser;
import shticell.engine.sheet.coordinate.ParseException;
import shticell.engine.xmlParser.XmlSheetLoader;

import java.util.Optional;
import java.util.Scanner;

public class UIManager implements Menu {

    Sheet sheet;

    private void printMenu() {
        System.out.println("(1) Read File");
        System.out.println("(2) Display Spreadsheet");
        System.out.println("(3) Display Single Cell");
        System.out.println("(4) Update Single Cell");
        System.out.println("(5) Display Versions");
        System.out.println("(6) Exit");
    }

    @Override
    public void displaySpreadsheet() {
        System.out.println(sheet.toString());
    }

    @Override
    public void displaySingleCell() {

    }

    public int getUserChoice(int range) {
        Scanner scanner = new Scanner(System.in);
        boolean validInput = false;
        int userChoice = -1;

        while (!validInput) {
            System.out.println("Please enter a number between 1 and " + range + ":");

            try {
                String userInput = scanner.nextLine();
                userChoice = Integer.parseInt(userInput);

                if (userChoice >= 1 && userChoice <= range) {
                    validInput = true;
                } else {
                    throw new IllegalArgumentException("Input out of range. Please try again.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        return userChoice;
    }

    @Override
    public void updateSingleCell(Coordinate coordinate, String newOriginalValue) {

        Optional<Cell> optionalCell = Optional.ofNullable(sheet.getCell(coordinate));
        Cell cell;
        if (optionalCell.isPresent()) {
            cell = optionalCell.get();
            System.out.println("Cell at: " + coordinate);
            System.out.println("Original value: " + cell.getOriginalValue());
            System.out.println("Effective value: " + cell.getEffectiveValue());
            sheet.onCellUpdated(newOriginalValue, cell.getCoordinate());

        } else {
            System.out.println("Cell at: " + coordinate.toString() + " is empty");
            cell = new CellImpl(coordinate, sheet.getColumnWidthUnits(), sheet.getRowsHeightUnits());
            sheet.addCell(cell);
            sheet.onCellUpdated(newOriginalValue, coordinate);
        }
        cell.updateVersion();
        sheet.updateVersion();
        System.out.println("Cell at: " + coordinate);
        System.out.println("Original value: " + cell.getOriginalValue());
        System.out.println("Effective value: " + cell.getEffectiveValue().getValue());
        System.out.println(sheet);

    }

    public void start() {

        boolean exit = false;

        while (!exit) {
            printMenu();
            int choice = getUserChoice(6);

            switch (choice) {
                case 1 -> getXmlFile();
                case 2 -> displaySpreadsheet();
                case 3 -> displaySingleCell();
                case 4 -> updateSingleCell(getCellCoordinateToChange(sheet), getNewValueForCell());
                //case 5 -> displayVersions();
                case 6 -> exit = true;
                default -> System.out.println("Invalid choice. Please try again.");
            }

            if (!exit) {
                System.out.println("Returning to menu...\n");
            }
        }

        System.out.println("Exiting program. Goodbye!");
    }

    private Coordinate getCellCoordinateToChange(Sheet sheet) {
        Scanner scanner = new Scanner(System.in);
        Coordinate coordinate = null;
        boolean validInput = false;

        while (!validInput) {
            System.out.print("Please enter the cell coordinate (e.g., A5): ");
            String input = scanner.nextLine().trim();

            // Validate the input format
            if (!isValidCoordinateFormat(input)) {
                System.out.println("Invalid format. Please enter the coordinate in the format (e.g., A5).");
                continue;
            }

            try {
                coordinate = CoordinateParser.parse(input);
                if (coordinate.getRow() < 0 || coordinate.getRow() >= sheet.getRowSize() ||
                        coordinate.getColumn() < 0 || coordinate.getColumn() >= sheet.getColSize()) {
                    throw new IndexOutOfBoundsException("Coordinate " + input + " is out of bounds.");
                }

                validInput = true;
            } catch (IllegalArgumentException | IndexOutOfBoundsException | ParseException e) {
                System.out.println("Invalid coordinate: " + e.getMessage());
            }
        }

        return coordinate;
    }

    private boolean isValidCoordinateFormat(String input) {
        // Regular expression to match format: one or more uppercase letters followed by one or more digits
        return input.matches("[A-Z]+\\d+");
    }

    private String getNewValueForCell() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the new value: ");
        return scanner.nextLine();
    }

    @Override
    public void getXmlFile() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the full path to the XML file: ");
        System.out.println("(Example for Windows: C:\\path\\to\\your\\file.xml)\n");
        String filePath = scanner.nextLine();

        this.sheet = XmlSheetLoader.fromXmlFileToObject(filePath);

    }
}