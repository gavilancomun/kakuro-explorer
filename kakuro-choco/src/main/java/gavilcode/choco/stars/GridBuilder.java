package gavilcode.choco.stars;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
      if (cell.intVar == null) {
        print(" ");
      }
      else {
        print(cell.intVar.getValue() == 1 ? "*" : " ");
      }
      print(" ");
      if (cell.right == null) {
        print("|");
      }
      else {
        print(" ");
      }
    }
    println();
    print("|");
    for (var cell : row) {
      if (cell.bottom == null) {
        print("---");
      }
      else {
        print("   ");
      }
      print(".");
    }
    println();
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

void growGroup(Set<Cell> group, Cell... cells) {
  for (var cell : cells) {
    if ((cell != null) && !cell.inGroup) {
      cell.inGroup = true;
      group.add(cell);
      growGroup(group, cell.left, cell.right, cell.top, cell.bottom);
    }
  }
}

IntVar[] getIntVars(Collection<Cell> coll) {
  return coll.stream().map(c -> c.intVar).toArray(IntVar[]::new);
}

void constrainGroups(Model model) {
  var groups = new ArrayList<Set<Cell>>();
  for (var row : cells) {
    for (var cell : row) {
      if (!cell.inGroup) {
        var group = new HashSet<Cell>();
        groups.add(group);
        growGroup(group, cell);
      }
    }
  }
  println("groups " + groups.size());
  groups.forEach(g -> constrain(model, getIntVars(g), "+", 2));
}

void constrainNeighboursCell(Model model, int row, int col) {
//  println(" n " + row + " " + col);
  var cell = cells[row][col];
  for (int dx = -1; dx < 1; ++dx) {
    for (int dy = -1; dy < 1; ++dy) {
      var x = row + dx;
      var y = col + dy;
      if ((x >= 0) && (x < cells.length)
              && (y >= 0) && (y < cells[0].length)) {
        if ((dx != 0) || (dy != 0)) {
          model.arithm(cell.intVar, "+", cells[x][y].intVar, "<", 2).post();
        }
//        print(" [" + x + ", " + y + "]");
      }
    }
  }
//  println();
}

void constrainNeighbours(Model model) {
  for (int row = 0; row < cells.length; ++row) {
    for (int col = 0; col < cells[0].length; ++col) {
      constrainNeighboursCell(model, row, col);
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
