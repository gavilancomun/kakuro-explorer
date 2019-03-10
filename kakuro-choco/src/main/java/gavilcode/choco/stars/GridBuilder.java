package gavilcode.choco.stars;

import java.util.ArrayList;
import java.util.List;

public class GridBuilder {

Cell[][] cells;

private void initCells(int numberOfRows, int numberOfColumns) {
  cells = new Cell[numberOfRows][numberOfColumns];
  for (int i = 0; i < numberOfRows; ++i) {
    for (int j = 0; j < numberOfColumns; ++j) {
      cells[i][j] = new Cell();
    }
  }
}

public Cell[][] gridFromString(List<String> gridStrings) {
  var headerless = new ArrayList<String>(gridStrings);
  var removeHeader = headerless.remove(0);
  var numberOfRows = headerless.size() / 2;
  var numberOfColumns = (headerless.get(0).length() - 1) / 4;
  System.out.println("r " + numberOfRows + " c " + numberOfColumns);
  initCells(numberOfRows, numberOfColumns);
  for (int pos = 0; pos < headerless.size(); pos += 2) {
    var rowNumber = pos / 2;
    var cellRow = headerless.get(pos);
    cellRow = cellRow.substring(1); // remove left wall
    for (int col = 0; col < numberOfColumns; ++col) {
//      System.out.println(rowNumber + " " + col + " [" + cellRow + "]");
      var cellString = cellRow.substring(col * 4, col * 4 + 4);
//      System.out.println("[" + cellString + "]");
      var cell = cells[rowNumber][col];
      if (cellString.substring(3, 4).equals(" ")) {
        cell.right = cells[rowNumber][col + 1];
        cells[rowNumber][col + 1].left = cell;
      }
    }
    var underRow = headerless.get(pos + 1).substring(1);
    for (int col = 0; col < numberOfColumns; ++col) {
      var cellUnderString = underRow.substring(col * 4, col * 4 + 4);
      System.out.println(rowNumber + " " + col + " " + cellUnderString);
      var cell = cells[rowNumber][col];
      if (cellUnderString.substring(0, 3).equals("   ")) {
        cell.bottom = cells[rowNumber + 1][col];
        cells[rowNumber + 1][col].top = cell;
      }
    }
  }
  return cells;
}

public void draw() {
  for (int row = 0; row < cells.length; ++row) {
    System.out.print("|");
    for (int col = 0; col < cells[0].length; ++col) {
      System.out.print("   ");
      if (cells[row][col].right == null) {
        System.out.print("|");
      }
      else {
        System.out.print(" ");
      }
    }
    System.out.println();
    System.out.print("|");
    for (int col = 0; col < cells[0].length; ++col) {
      if (cells[row][col].bottom == null) {
        System.out.print("---.");
      }
      else {
        System.out.print("   .");
      }
    }
    System.out.println();
  }
}
}
