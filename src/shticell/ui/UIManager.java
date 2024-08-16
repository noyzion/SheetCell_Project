package shticell.ui;

import shticell.engine.sheet.api.Sheet;
import shticell.engine.sheet.cell.impl.CellImpl;
import shticell.engine.sheet.impl.SheetImpl;

import java.util.Scanner;

public class UIManager {

    private final int mainMenuRange = 6;
    private final Sheet sheet = new SheetImpl("FirstSheet");

    public void printMenu() {
        System.out.println("(1) Read File");
        System.out.println("(2) Display Spreadsheet");
        System.out.println("(3) Display Single Cell");
        System.out.println("(4) Update Single Cell");
        System.out.println("(5) Display Versions");
        System.out.println("(6) Exit");
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

    public void start() {

        Sheet sheetspread = new SheetImpl("First Sheet");
        sheetspread.addCell();

    }
}
