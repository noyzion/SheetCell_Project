package shticell.engine.menu;

import shticell.engine.DTO.CoordinateDTO;
import shticell.engine.sheet.coordinate.Coordinate;

public interface Menu {

  public void getXmlFile();

  public void displaySpreadsheet();

  public void displaySingleCell();

  public void updateSingleCell(CoordinateDTO coordinateDTO, String newOriginalValue);

  //public void showSheetVersions();
  //public void exit();
}
