package shticell.engine.menu;

import shticell.engine.DTO.CoordinateDTO;
import shticell.engine.sheet.coordinate.Coordinate;
import shticell.engine.sheet.coordinate.ParseException;

public interface Menu {

  public void getXmlFile();

  public void displaySpreadsheet();

  public void displaySingleCell();

  public void updateSingleCell(String cord);

  //public void showSheetVersions();
  //public void exit();
}
