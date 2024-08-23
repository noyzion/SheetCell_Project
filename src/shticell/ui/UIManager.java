package shticell.ui;

import shticell.engine.DTO.CellDTO;
import shticell.engine.DTO.CoordinateDTO;
import shticell.engine.logic.impl.LogicImpl;
import shticell.engine.menu.Menu;
import shticell.engine.sheet.cell.api.Cell;
import shticell.engine.sheet.coordinate.*;
import shticell.engine.xmlParser.XmlSheetLoader;

import java.util.Scanner;

public class UIManager implements Menu {

    private final LogicImpl logic = new LogicImpl();

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
        if (logic.getSheet() != null) {
            System.out.println(logic.getSheet().toString());
        } else {
            System.out.println("No sheet loaded.");
        }
    }

    @Override
    public void displaySingleCell() {
        CoordinateDTO coordinate = getCellCoordinate();
        if (coordinate != null) {
            CellDTO cell = logic.getSheet().getCells().get(coordinate);
            if (cell != null) {
                System.out.println(cell.toString());
            } else {
                System.out.println("Cell not found.");
            }
        }
    }

    public int getUserChoice(int range) {
        Scanner scanner = new Scanner(System.in);
        int userChoice = -1;

        while (true) {
            System.out.println("Please enter a number between 1 and " + range + ":");

            try {
                String userInput = scanner.nextLine();
                userChoice = Integer.parseInt(userInput);

                if (userChoice >= 1 && userChoice <= range) {
                    break;
                } else {
                    System.out.println("Input out of range. Please try again.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }

        return userChoice;
    }

    @Override
    public void updateSingleCell(CoordinateDTO coordinateDTO, String newOriginalValue) {
        if (logic.getSheet() != null && coordinateDTO != null) {
            try {
                Coordinate coordinate = toCoordinate(coordinateDTO);
                logic.setCellValue(coordinate, newOriginalValue);
                System.out.println("Cell updated successfully.");
            } catch (Exception e) {
                System.out.println("Error updating cell: " + e.getMessage());
            }
        } else {
            System.out.println("Sheet or coordinate is not valid.");
        }
    }

    // Utility method to convert CoordinateDTO to Coordinate
    private Coordinate toCoordinate(CoordinateDTO coordinateDTO) {
        if (coordinateDTO == null) {
            throw new IllegalArgumentException("CoordinateDTO cannot be null.");
        }
        // Assuming CoordinateImpl has a constructor that matches the parameters
        return new CoordinateImpl(coordinateDTO.getRow(), coordinateDTO.getColumn());
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
                case 4 -> updateSingleCell(getCellCoordinate(), getNewValueForCell());
                case 5 -> displayVersions(); // Implement if needed
                case 6 -> exit = true;
                default -> System.out.println("Invalid choice. Please try again.");
            }

            if (!exit) {
                System.out.println("Returning to menu...\n");
            }
        }

        System.out.println("Exiting program. Goodbye!");
    }

    private CoordinateDTO getCellCoordinate() {
        Scanner scanner = new Scanner(System.in);
        CoordinateDTO coordinate = null;

        while (true) {
            System.out.print("Please enter the cell coordinate (e.g., A5): ");
            String input = scanner.nextLine().trim();

            if (!isValidCoordinateFormat(input)) {
                System.out.println("Invalid format. Please enter the coordinate in the format (e.g., A5).");
                continue;
            }

            try {
                coordinate = (CoordinateDTO) CoordinateParser.parse(input);
                if (coordinate.getRow() < 0 || coordinate.getRow() >= logic.getSheet().getRowSize() ||
                        coordinate.getColumn() < 0 || coordinate.getColumn() >= logic.getSheet().getColumnSize()) {
                    System.out.println("Coordinate " + input + " is out of bounds.");
                } else {
                    break;
                }

            } catch (IllegalArgumentException | IndexOutOfBoundsException | ParseException e) {
                System.out.println("Invalid coordinate: " + e.getMessage());
            }
        }

        return coordinate;
    }

    private boolean isValidCoordinateFormat(String input) {
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
        String filePath = scanner.nextLine();

        try {
            logic.addSheet(XmlSheetLoader.fromXmlFileToObject(filePath));
            System.out.println("Sheet loaded successfully.");
        } catch (Exception e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    // Placeholder for displayVersions method
    private void displayVersions() {
        // Implement version display logic if needed
    }
}