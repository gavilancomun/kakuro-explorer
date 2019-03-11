package gavilcode.choco.stars;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import static java.util.Objects.isNull;
import java.util.stream.Stream;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

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
  println("r " + numberOfRows + " c " + numberOfColumns);
  initCells(numberOfRows, numberOfColumns);
  for (int pos = 0; pos < headerless.size(); pos += 2) {
    var rowNumber = pos / 2;
    var cellRow = headerless.get(pos);
    cellRow = cellRow.substring(1); // remove left wall
    for (int col = 0; col < numberOfColumns; ++col) {
//      println(rowNumber + " " + col + " [" + cellRow + "]");
      var cellString = cellRow.substring(col * 4, col * 4 + 4);
//      println("[" + cellString + "]");
      var cell = cells[rowNumber][col];
      if (cellString.substring(3, 4).equals(" ")) {
        cell.right = cells[rowNumber][col + 1];
        cells[rowNumber][col + 1].left = cell;
      }
    }
    var underRow = headerless.get(pos + 1).substring(1);
    for (int col = 0; col < numberOfColumns; ++col) {
      var cellUnderString = underRow.substring(col * 4, col * 4 + 4);
//      println(rowNumber + " " + col + " " + cellUnderString);
      var cell = cells[rowNumber][col];
      if (cellUnderString.substring(0, 3).equals("   ")) {
        cell.bottom = cells[rowNumber + 1][col];
        cells[rowNumber + 1][col].top = cell;
      }
    }
  }
  return cells;
}

void print(String s) {
  System.out.print(s);
}

void println(String s) {
  System.out.println(s);
}

void println() {
  System.out.println();
}

public void draw() {
  for (var row : cells) {
    print("|");
    for (var cell : row) {
      print(" ");
      if (isNull(cell.intVar)) {
        print(" ");
      }
      else {
        print(cell.intVar.getValue() == 1 ? "*" : " ");
      }
      print(" ");
      print(isNull(cell.right) ? "|" : " ");
    }
    println();
    print("|");
    for (var cell : row) {
      print(isNull(cell.bottom) ? "---" : "   ");
      print(".");
    }
    println();
  }
}

void arithm(Model model, IntVar[] vars, String operator, String relOp, int target) {
  var accumulation = vars[0];
  for (int i = 1; i < vars.length; ++i) {
    var newSum = model.intVar(0, vars.length + 1);
    model.arithm(accumulation, operator, vars[i], "=", newSum).post();
    accumulation = newSum;
  }
  model.arithm(accumulation, relOp, target).post();
}

void arithm(Model model, IntVar[] vars, String operator, int target) {
    arithm(model, vars, operator, "=", target);
}

void constrainRows(Model model) {
  for (var row : cells) {
    var vars = getIntVars(row);
    arithm(model, vars, "+", 2);
  }
}

void constrainColumns(Model model) {
  for (int col = 0; col < cells[0].length; ++col) {
    var vars = new IntVar[cells.length];
    for (int row = 0; row < cells.length; ++row) {
      vars[row] = cells[row][col].intVar;
    }
    arithm(model, vars, "+", 2);
  }
}

void growGroup(Collection<Cell> group, Cell... cells) {
  for (var cell : cells) {
    if (!isNull(cell) && !cell.inGroup) {
      cell.inGroup = true;
      group.add(cell);
      growGroup(group, cell.left, cell.right, cell.top, cell.bottom);
    }
  }
}

IntVar[] getIntVars(Stream<Cell> cells) {
  return cells.map(c -> c.intVar).toArray(IntVar[]::new);
}

IntVar[] getIntVars(Cell[] coll) {
  return getIntVars(Arrays.stream(coll));
}

IntVar[] getIntVars(Collection<Cell> coll) {
  return getIntVars(coll.stream());
}

void constrainGroups(Model model) {
  var groups = new ArrayList<Collection<Cell>>();
  for (var row : cells) {
    for (var cell : row) {
      if (!cell.inGroup) {
        var group = new HashSet<Cell>();
        groups.add(group);
        growGroup(group, cell);
      }
    }
  }
  groups.forEach(g -> arithm(model, getIntVars(g), "+", 2));
}

private void constrainNeighbour(Model model, Cell cell, int x, int y) {
  if ((x >= 0) && (x < cells.length)
          && (y >= 0) && (y < cells[x].length)) {
    var neighbour = cells[x][y];
    model.arithm(cell.intVar, "+", neighbour.intVar, "<", 2).post();
  }
}
// Only need to do:
//
// XX.
// Xo.
// ...
//
// for every cell to cover the whole grid
//

void constrainNeighbours(Model model) {
  for (int row = 0; row < cells.length; ++row) {
    for (int col = 0; col < cells[0].length; ++col) {
        var cell = cells[row][col];
        constrainNeighbour(model, cell, row - 1, col - 1);
        constrainNeighbour(model, cell, row - 1, col);
        constrainNeighbour(model, cell, row, col - 1);
    }
  }
}

private void solveModel(Model model) {
  var solver = model.getSolver();
  solver.showStatistics();
  solver.showDashboard();
  solver.limitTime(10 * 60 * 1000);
  var solution = solver.findSolution();
  if (solver.isStopCriterionMet()) {
    println("stop criterion met");
  }
  if (solution != null) {
    draw();
  }
}

private void initCellLogicVars(Model model) {
  for (var row : cells) {
    for (var cell : row) {
      cell.intVar = model.intVar(0, 1); // 0 empty, 1 star
    }
  }
}

public void solve() {
  var model = new Model();
  initCellLogicVars(model);
  constrainRows(model);
  constrainColumns(model);
  constrainGroups(model);
  constrainNeighbours(model);
  solveModel(model);
}

}
