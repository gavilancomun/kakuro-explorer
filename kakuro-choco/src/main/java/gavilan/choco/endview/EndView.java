package gavilan.choco.endview;

import static java.util.Collections.reverse;
import java.util.List;
import java.util.stream.IntStream;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

public class EndView {

Model model = new Model();

private Constraint equal(IntVar var1, int var2) {
  return model.arithm(var1, "=", var2);
}

private Constraint and(Constraint... cs) {
  return model.and(cs);
}

private Constraint or(Constraint... cs) {
  return model.or(cs);
}

private Constraint in(IntVar var1, int[] var2) {
  return model.member(var1, var2);
}

private int[] range(IntVar[] vars) {
  return IntStream.rangeClosed(1, vars.length - 4).toArray();
}

private void constrain(int target, IntVar[] vars) {
  if (target > 0) {
    int kind = vars.length;
    var blank = range(vars);
    if (5 == kind) {
      or(
              equal(vars[0], target),
              and(
                      in(vars[0], blank),
                      equal(vars[1], target))
      ).post();
    }
    else if (6 == kind) {
      or(
              equal(vars[0], target),
              and(
                      in(vars[0], blank),
                      equal(vars[1], target)),
              and(
                      in(vars[0], blank),
                      in(vars[1], blank),
                      equal(vars[2], target)
              )
      ).post();
    }
  }
}

private void constrain(int target, List<IntVar> vars) {
  constrain(target, vars.toArray(new IntVar[0]));
}

int toDom(int rowCount, char c) {
  return c - 'A' + rowCount - 3;
}

private void parseConstraints(Grid grid, List<String> gridPic) {
  var intVars = new IntVar[0];
  int rowCount = gridPic.size() - 2;
  var topRow = gridPic.get(0);
  var bottomRow = gridPic.get(gridPic.size() - 1);
  for (int i = 0; i < rowCount; ++i) {
    var row = grid.getRow(i);
    var column = grid.getColumn(i);
    model.allDifferent(row.toArray(intVars)).post();
    model.allDifferent(column.toArray(intVars)).post();
    int topConstraint = toDom(rowCount, topRow.charAt(i + 1));
    constrain(topConstraint, column);
    reverse(column);
    int bottomConstraint = toDom(rowCount, bottomRow.charAt(i + 1));
    constrain(bottomConstraint, column);
    int leftConstraint = toDom(rowCount, gridPic.get(i + 1).charAt(0));
    constrain(leftConstraint, row);
    int rightContraint = toDom(rowCount, gridPic.get(i + 1).charAt(gridPic.get(i + 1).length() - 1));
    reverse(row);
    constrain(rightContraint, row);
  }
}

public void solve(List<String> gridPic) {
  int rowCount = gridPic.size() - 2;
  var grid = new Grid(rowCount);
  grid.init(model);
  parseConstraints(grid, gridPic);
  var ok = model.getSolver().solve();
  if (ok) {
    System.out.println("");
    grid.drawGrid();
  }
  else {
    System.err.println("No solution found for " + gridPic);
  }
}

}
