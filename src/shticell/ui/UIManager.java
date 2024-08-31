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
    private boolean isSheetLoaded = false;

    private void printMenu() {
        System.out.println(" ".repeat(MenuOption.getMaxDescriptionLength() / 2) + "Menu:\n" + "=".repeat(MenuOption.getMaxDescriptionLength() + 6));
        for (MenuOption option : MenuOption.values()) {
            System.out.println("(" + option.getValue() + ") " + option.getDescription());
        }
    }

    @Override
    public void displaySpreadsheet() {
        checkSheetLoaded();
        SheetDTO sheetDTO = logic.getSheet();
        System.out.println(sheetDTO.toString());
    }

    @Override
    public void displaySingleCell() {
        checkSheetLoaded();
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
    public void updateSingleCell() {
        checkSheetLoaded();
       String cellID = getCellCoordinate();
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
                String newOriginalValue = getNewValueForCell();
                if (newOriginalValue.isBlank()) {
                    logic.setCellValue(cellID, null);

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



    private void checkSheetLoaded() {
        if (!isSheetLoaded) {
            throw new IllegalStateException("No sheet loaded. Please load a sheet first.");
        }
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
                    case UPDATE_SINGLE_CELL -> updateSingleCell();
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
           catch  (IllegalStateException e)
            {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("Exiting program. Goodbye!");
    }

    private String getCellCoordinate() {
        Scanner scanner = new Scanner(System.in);
        String coordinate;

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

    private String getNewValueForCell() {
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
                isSheetLoaded = true;
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

    @Override
    public void displayVersions() {
        checkSheetLoaded();
        displayVersionsTable(logic.getSheet().getVersion());
        int version = getVersionNumber(logic.getSheet().getVersion());
        System.out.println("Displaying the state of the spreadsheet for version " + version + ":");
        System.out.println(logic.getSheetByVersion(version).toString());
    }

    private int getVersionNumber(int numVersions) {
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

    @Override
    public void saveSystemState() {
        Scanner scanner = new Scanner(System.in);
        checkSheetLoaded();
        System.out.print("Please enter the full file path to save the system state (without extension): ");
        String filePath = scanner.nextLine();
        System.out.print("Please enter the desired file name to save the system state: ");
        String fileName = scanner.nextLine();

        while (!isValidFileName(fileName)) {
            System.out.println("Invalid file name. Please avoid using special characters like \\ / : * ? \" < > | and try again.");
            System.out.print("Please try again: ");
             fileName = scanner.nextLine();
        }

        String fullPath = filePath + File.separator + fileName + ".dat";

        if (!saveSystemStateHelper(fullPath)) {
            System.out.println("Please try again: ");
            saveSystemState();
        }
        System.out.println("System state saved successfully to " + fullPath);
    }

    @Override
    public void loadSystemState() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter the full file path to load the system state (without extension): ");
        String filePath = scanner.nextLine();
        String fullPath = filePath + ".dat";

        if (!loadSystemStateHelper(fullPath)) {
            System.out.println("Please try again: ");
            loadSystemState();
        }
        System.out.println("System state loaded successfully from " + fullPath);

    }

    private boolean saveSystemStateHelper(String filePath) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(logic);
            return true;
        } catch (IOException e) {
            System.out.println("Error saving system state: " + e.getMessage());
            return false;
        }
    }

    private boolean loadSystemStateHelper(String filePath) {

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
            logic = (LogicImpl) in.readObject();
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading system state: " + e.getMessage());
            return false;
        }
    }

    private boolean isValidFileName(String fileName) {
        String invalidChars = "[\\\\/:*?\"<>|]";
        return !fileName.matches(".*" + invalidChars + ".*");
    }
}