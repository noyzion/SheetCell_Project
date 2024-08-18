package shticell.ui;

import shticell.engine.sheet.api.Sheet;
import shticell.engine.sheet.cell.api.Cell;
import shticell.engine.sheet.cell.impl.CellImpl;
import shticell.engine.sheet.coordinate.Coordinate;
import shticell.engine.sheet.coordinate.CoordinateImpl;
import shticell.engine.sheet.impl.SheetImpl;

import java.util.Scanner;

public class UIManager {


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


        String value1 = "500";
        String value2 = "{PLUS,{REF,B1},4}"; //504
        String value3 = "{MINUS, {REF,B2},{REF,B1}}";

        Coordinate coord1 = new CoordinateImpl(1, 2);
        Coordinate coord2 = new CoordinateImpl(2, 2);
        Coordinate coord3 = new CoordinateImpl(3, 2);


        Sheet sheetSpread = new SheetImpl("FirstSheet", 5, 5);
        sheetSpread.addCell(new CellImpl(1, 2, sheetSpread));
        sheetSpread.getCell(coord1).setOriginalValue(value1);
        sheetSpread.addCell(new CellImpl(2, 2, sheetSpread));
        sheetSpread.getCell(coord2).setOriginalValue(value2);
        sheetSpread.addCell(new CellImpl(3, 2, sheetSpread));
        sheetSpread.getCell(coord3).setOriginalValue(value3);
        String value8 = "{CONCAT, 'Hello ', {REF,B1}}"; // Expected to concatenate "Hello " with the result of value1

        String value7 = "{TIMES, {REF,B2}, {PLUS, 2, {REF,B1}}}"; // Expected to multiply the result of value2 by (2 + value1)

        Coordinate coord7 = new CoordinateImpl(4, 2);
        sheetSpread.addCell(new CellImpl(4, 2, sheetSpread));
        sheetSpread.getCell(coord7).setOriginalValue(value7);

        System.out.println(sheetSpread.getCell(coord7));

        Coordinate trying = new CoordinateImpl(2, 2);
        Cell cell = sheetSpread.getCell(trying);
        System.out.println(cell);
        System.out.println("\n");
        System.out.println(sheetSpread.toString());

        cell.setOriginalValue("10");

        System.out.println("\n");
        System.out.println(sheetSpread.toString());

    }
}