package gavilcode.choco.stars;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import static org.chocosolver.util.tools.ArrayUtils.toArray;

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
//      System.out.println(rowNumber + " " + col + " " + cellUnderString);
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
      var cell = cells[row][col];
      System.out.print(" ");
      if (cell.intVar == null) {
        System.out.print(" ");
      }
      else {
        System.out.print(cell.intVar.getValue() == 1 ? "*" : " ");
      }
      System.out.print(" ");
      if (cell.right == null) {
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

void constrain(Model model, IntVar[] vars, String operator, String relOp, int target) {
  var accumulation = vars[0];
  for (int i = 1; i < vars.length; ++i) {
    var newSum = model.intVar(0, vars.length + 1);
    model.arithm(accumulation, operator, vars[i], "=", newSum).post();
    accumulation = newSum;
  }
  model.arithm(accumulation, relOp, target).post();
}

void constrain(Model model, IntVar[] vars, String operator, int target) {
  constrain(model, vars, operator, "=", target);
}

void constrainRows(Model model) {
  for (int row = 0; row < cells.length; ++row) {
    var vars = new IntVar[cells[row].length];
    for (int col = 0; col < cells[0].length; ++col) {
      vars[col] = cells[row][col].intVar;
    }
    constrain(model, vars, "+", 2);
  }
}

void constrainColumns(Model model) {
  for (int col = 0; col < cells[0].length; ++col) {
    var vars = new IntVar[cells.length];
    for (int row = 0; row < cells.length; ++row) {
      vars[row] = cells[row][col].intVar;
    }
    constrain(model, vars, "+", 2);
  }
}

void growGroup(Cell cell, Set<Cell> group) {
  if ((cell == null) || (cell.group != null)) {
    return;
  }
  group.add(cell);
  cell.group = group;
  if (cell.left != null) {
    growGroup(cell.left, group);
  }
  if (cell.right != null) {
    growGroup(cell.right, group);
  }
  if (cell.top != null) {
    growGroup(cell.top, group);
  }
  if (cell.bottom != null) {
    growGroup(cell.bottom, group);
  }
}

void constrainGroups(Model model) {
  var groups = new ArrayList<Set<Cell>>();
  for (int row = 0; row < cells.length; ++row) {
    for (int col = 0; col < cells[0].length; ++col) {
      var cell = cells[row][col];
      if (cell.group == null) {
        var group = new HashSet<Cell>();
        groups.add(group);
        growGroup(cell, group);
      }
    }
  }
  System.out.println("groups " + groups.size());
  groups.forEach(g -> constrain(model, g.stream().map(c -> c.intVar).toArray(IntVar[]::new), "+", 2));
}

void constrainNeighboursCell(Model model, int row, int col) {
//  System.out.println(" n " + row + " " + col);
  var cell = cells[row][col];
  for (int dx = -1; dx < 1; ++dx) {
    for (int dy = -1; dy < 1; ++dy) {
      var x = row + dx;
      var y = col + dy;
      if ((x >= 0) && (x < cells.length)
              && (y >= 0) && (y < cells[0].length)) {
        if ((x != row) || (y != col)) {
          model.arithm(cell.intVar, "+", cells[x][y].intVar, "<", 2).post();
        }
//        System.out.print(" [" + x + ", " + y + "]");
      }
    }
  }
//  System.out.println();
}

void constrainNeighbours(Model model) {
  for (int row = 0; row < cells.length; ++row) {
    for (int col = 0; col < cells[0].length; ++col) {
      constrainNeighboursCell(model, row, col);
    }
  }
}

public void solve() {
  var model = new Model();
  for (int row = 0; row < cells.length; ++row) {
    for (int col = 0; col < cells[0].length; ++col) {
      cells[row][col].intVar = model.intVar(0, 1); // 0 empty, 1 star
    }
  }
  constrainRows(model);
  constrainColumns(model);
  constrainGroups(model);
  constrainNeighbours(model);
  var solver = model.getSolver();
  solver.showStatistics();
  solver.showDashboard();
  solver.limitTime(10 * 60 * 1000);
  var solution = solver.findSolution();
  if (solver.isStopCriterionMet()) {
    System.out.println("stop criterion met");
  }
  if (solution != null) {
    draw();
  }
}
}
