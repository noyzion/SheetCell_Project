package shticell.ui;

import shticell.engine.DTO.CellDTO;
import shticell.engine.DTO.SheetDTO;
import shticell.engine.logic.impl.LogicImpl;
import shticell.engine.menu.Menu;
import shticell.engine.sheet.coordinate.CoordinateFactory;
import shticell.engine.sheet.coordinate.ParseException;
import shticell.engine.xmlParser.XmlSheetLoader;

import java.io.*;
import java.util.Scanner;

public class UIManager implements Menu {

    private LogicImpl logic = new LogicImpl();

    private void printMenu() {
        for (MenuOption option : MenuOption.values()) {
            System.out.println("(" + option.getValue() + ") " + option.getDescription());
        }
    }

    @Override
    public void displaySpreadsheet() {
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
                    System.out.println(cellDTO);
                } else {
                    System.out.println("Cell " + coordinate + " is empty");
                }
            } catch (ParseException e) {
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
                if (newOriginalValue.isBlank()) {
                    logic.setCellValue(cellID, null);

                } else {
                    logic.setCellValue(cellID, newOriginalValue);
                    System.out.println("Cell updated successfully.");
                    int counter = 1 + logic.getSheet().getCell(cellID).getAffectedCells().size();
                }
                validCalc = true;
            } catch (Exception e) {
                System.out.println("Error updating cell: " + e.getMessage());
            }
        }
        displaySpreadsheet();
    }

    public void start() {
        boolean exit = false;

        while (!exit) {
            printMenu();
            int choice = getUserChoice(MenuOption.values().length);

            try {
                MenuOption option = MenuOption.fromValue(choice);

                switch (option) {
                    case READ_FILE -> getXmlFile();
                    case DISPLAY_SPREADSHEET -> displaySpreadsheet();
                    case DISPLAY_SINGLE_CELL -> displaySingleCell();
                    case UPDATE_SINGLE_CELL -> updateSingleCell(getCellCoordinate());
                    case DISPLAY_VERSIONS -> displayVersions();
                    case SAVE_SYSTEM_STATE -> saveSystemState();
                    case LOAD_SYSTEM_STATE -> loadSystemState();
                    case EXIT -> exit = true;
                }

                if (!exit) {
                    System.out.println("Returning to menu...\n");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid choice. Please try again.");
            }
        }

        System.out.println("Exiting program. Goodbye!");
    }

    private String getCellCoordinate() {
        Scanner scanner = new Scanner(System.in);
        String coordinate = null;

        while (true) {
            System.out.print("Please enter the cell coordinate (e.g., A5): ");
            String input = scanner.nextLine().trim().toUpperCase();

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

    private void displayVersionsTable(int versions) {
        System.out.println("Version Number | Number of Changes");
        System.out.println("-------------------------------");
        for (int i = 0; i < versions; i++) {
            int versionNumber = i + 1;
            int changes = logic.getSheetByVersion(i+1).getCounterChangedCells();
            System.out.printf("%-15d | %-17d%n", versionNumber, changes);
        }
    }

    private void displayVersions() {
        displayVersionsTable(logic.getSheet().getVersion());
        int version = getVersionNumber(logic.getSheet().getVersion());
        System.out.println("Displaying the state of the spreadsheet for version " + version + ":");
        System.out.println(logic.getSheetByVersion(version).toString());
    }

    public int getVersionNumber(int numVersions) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Please enter the version number to view: ");
            try {
                int versionNumber = Integer.parseInt(scanner.nextLine());
                if (versionNumber >= 1 && versionNumber <= numVersions) {
                    return versionNumber;
                } else {
                    System.out.printf("Invalid version number. Please enter a number between 1 and %d.%n", numVersions);
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    public void saveSystemState() {

        String filePath = getFilePath() + ".dat";
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(logic);
            System.out.println("System state saved successfully to " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving system state: " + e.getMessage());
        }
    }

    public void loadSystemState() {

        String filePath = getFilePath() + ".dat";
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
            logic = (LogicImpl) in.readObject();
            System.out.println("System state loaded successfully from " + filePath);
            System.out.println(" " + filePath);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading system state: " + e.getMessage());
        }
    }

    private String getFilePath() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the full file path to load the system state (without extension):");
        return scanner.nextLine();
    }
}
