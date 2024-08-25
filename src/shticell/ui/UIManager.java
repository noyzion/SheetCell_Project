package shticell.ui;

import shticell.engine.DTO.CellDTO;
import shticell.engine.DTO.CoordinateDTO;
import shticell.engine.DTO.SheetDTO;
import shticell.engine.logic.impl.LogicImpl;
import shticell.engine.menu.Menu;
import shticell.engine.sheet.coordinate.Coordinate;
import shticell.engine.sheet.coordinate.CoordinateFactory;
import shticell.engine.sheet.coordinate.CoordinateParser;
import shticell.engine.sheet.coordinate.ParseException;
import shticell.engine.xmlParser.XmlSheetLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    public void displaySpreadsheet()  {
        SheetDTO sheetDTO = logic.getSheet();
        if (sheetDTO != null) {
            System.out.println(sheetDTO.toString());
        } else {
            System.out.println("No sheet loaded.");
        }
    }

    @Override
    public void displaySingleCell() {
        String coordinate = getCellCoordinate();
        if (coordinate != null) {
            try {
                CellDTO cellDTO = logic.getSheet().getCell(coordinate);
                if (cellDTO != null) {
                    System.out.println(cellDTO.toString());
                } else {
                    System.out.println("Cell " + coordinate +" is empty");
                }
            }
            catch (ParseException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public int getUserChoice(int range) {
        Scanner scanner = new Scanner(System.in);
        int userChoice = -1;

        while (true) {
            try {
                System.out.println("Please enter a number between 1 and " + range + ":");
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
    public void updateSingleCell(String cellID) {
        try {
            if (logic.getSheet().getCell(cellID) == null)
                System.out.println("Cell at: " + cellID + " is empty");
            else {
                System.out.println("Cell at: " + cellID);
                System.out.println("Original value is: " + logic.getSheet().getCell(cellID).getOriginalValue());
                System.out.println("Effective value is: " + logic.getSheet().getCell(cellID).getEffectiveValue().getValue());
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        boolean validCalc = false;
        while (!validCalc) {
            try {
                String newOriginalValue = getNewValueForCell(logic.getSheet().getCell(cellID));
                if (newOriginalValue.isEmpty()) {

                } else {
                    logic.setCellValue(cellID, newOriginalValue);
                    System.out.println("Cell updated successfully.");
                }
                validCalc = true;
            } catch (Exception e) {
                System.out.println("Error updating cell: " + e.getMessage());
            }
        }
        displaySpreadsheet();
    }

    public void start()  {
        boolean exit = false;

        while (!exit) {
            printMenu();
            int choice = getUserChoice(6);

            switch (choice) {
                case 1 -> getXmlFile();
                case 2 -> displaySpreadsheet();
                case 3 -> displaySingleCell();
                case 4 -> updateSingleCell(getCellCoordinate());
                case 5 -> displayVersions();
                case 6 -> exit = true;
                default -> System.out.println("Invalid choice. Please try again.");
            }

            if (!exit) {
                System.out.println("Returning to menu...\n");
            }
        }

        System.out.println("Exiting program. Goodbye!");
    }

    private String getCellCoordinate() {
        Scanner scanner = new Scanner(System.in);
        String coordinate = null;

        while (true) {
            System.out.print("Please enter the cell coordinate (e.g., A5): ");
            String input = scanner.nextLine().trim();

            try {
                CoordinateFactory.isValidCoordinateFormat(input);
                if (CoordinateFactory.isCoordinateWithinBounds(
                        logic.getSheet().getRowSize(),
                        logic.getSheet().getColumnSize(),
                        input)) {
                    coordinate = input;
                    break;
                } else {
                    System.out.println("Coordinate is out of bounds. Please enter a valid coordinate.");
                }
            } catch (IllegalArgumentException | ParseException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        return coordinate;
    }



    private String getNewValueForCell(CellDTO cellDTO) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the new value: ");
        return scanner.nextLine();
    }

    @Override
    public void getXmlFile() {
        Scanner scanner = new Scanner(System.in);
        boolean validInput = false;

        while (!validInput) {
            System.out.println("Please enter the full path to the XML file: ");
            String filePath = scanner.nextLine();

            try {
                logic.addSheet(XmlSheetLoader.fromXmlFileToObject(filePath));
                System.out.println("Sheet loaded successfully.");
                validInput = true;
            } catch (Exception e) {
                System.out.print("Error loading file: " + e.getMessage() + " Please try again.");
                System.out.println();
            }
        }
    }

    private void displayVersions() {

    }
}
