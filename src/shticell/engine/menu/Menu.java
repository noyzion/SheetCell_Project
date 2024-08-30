package shticell.engine.menu;

import shticell.engine.DTO.CoordinateDTO;
import shticell.engine.sheet.coordinate.Coordinate;
import shticell.engine.sheet.coordinate.ParseException;

public interface Menu {

   void getXmlFile();

   void displaySpreadsheet();

   void displaySingleCell();

   void updateSingleCell();

  void displayVersions();

  void loadSystemState();

  void saveSystemState();
}
