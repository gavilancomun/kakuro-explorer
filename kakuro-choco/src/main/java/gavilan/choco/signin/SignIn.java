package gavilan.choco.signin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

public class SignIn {

Model model = new Model();

private Constraint equal(IntVar var1, int var2) {
  return model.arithm(var1, "=", var2);
}

private Constraint in(IntVar var1, int[] var2) {
  return model.member(var1, var2);
}

private void constrain(int target, List<IntVar> vars) {
  if (target > 0) {
    int kind = vars.size();
    int[] blank = IntStream.rangeClosed(1, vars.size() - 4).toArray();
    if (5 == kind) {
      model.or(
              equal(vars.get(0), target),
              model.and(in(vars.get(0), blank),
                      equal(vars.get(1), target))
      ).post();
    }
    else if (6 == kind) {
      model.or(
              equal(vars.get(0), target),
              model.and(in(vars.get(0), blank),
                      equal(vars.get(1), target)),
              model.and(in(vars.get(0), blank),
                      in(vars.get(1), blank),
                      equal(vars.get(2), target)
              )
      ).post();
    }
  }
}

int toDom(int rowCount, char c) {
  return c - 'A' + rowCount - 3;
}

private void parseConstraints(Grid grid, List<String> gridPic) {
  for (int pos = 0; pos < gridPic.size(); pos += 2) {
    int i = pos / 2;
    IntVar[] row = grid.getRow(i);
    IntVar[] column = grid.getColumn(i);
    model.allDifferent(row, "DEFAULT").post();
    model.allDifferent(column, "DEFAULT").post();
    String[] parts = gridPic.get(pos).split("");
    for (int partPos = 1; partPos < parts.length; partPos += 2) {
      String sign = parts[partPos];
      int leftPos = partPos / 2;
      System.out.println("row " + i + " leftPos " + leftPos + " sign " + sign);
      switch (sign) {
      case "+":
        model.arithm(row[leftPos + 1], "-", row[leftPos], "=", 1).post();
        break;
      case "-":
        model.arithm(row[leftPos], "-", row[leftPos + 1], "=", 1).post();
        break;
      default:
        model.distance(row[leftPos], row[leftPos + 1], "!=", 1).post();
      }
    }
  }
}

public void solve(List<String> gridPic) {
  int rowCount = (gridPic.size() + 1) / 2;
  Grid grid = new Grid(rowCount);
  grid.init(model);
  parseConstraints(grid, gridPic);
  boolean ok = model.getSolver().solve();
  if (ok) {
    System.out.println("");
    grid.drawGrid();
  }
}

}
