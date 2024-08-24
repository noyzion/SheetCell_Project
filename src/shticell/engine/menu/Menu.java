package shticell.engine.menu;

import shticell.engine.DTO.CoordinateDTO;
import shticell.engine.sheet.coordinate.Coordinate;
import shticell.engine.sheet.coordinate.ParseException;

public interface Menu {

  public void getXmlFile();

  public void displaySpreadsheet() throws ParseException;

  public void displaySingleCell() throws ParseException;

  public void updateSingleCell(String cord, String newOriginalValue);

  //public void showSheetVersions();
  //public void exit();
}
