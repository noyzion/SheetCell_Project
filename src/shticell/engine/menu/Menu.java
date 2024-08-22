package shticell.engine.menu;

import shticell.engine.sheet.coordinate.Coordinate;

public interface Menu {

  //  public void display();
    public void getXmlFile();   public void displaySpreadsheet();
    public void displaySingleCell();
    public void updateSingleCell( Coordinate coordinate, String newOriginalValue);
    //public void showSheetVersions();
    //public void exit();
}
