package gavilan.choco.endview;

import java.util.ArrayList;
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
  IntVar[] intVars = new IntVar[0];
  int rowCount = gridPic.size() - 2;
  String topRow = gridPic.get(0);
  String bottomRow = gridPic.get(gridPic.size() - 1);
  for (int i = 0; i < rowCount; ++i) {
    ArrayList<IntVar> row = grid.getRow(i);
    ArrayList<IntVar> column = grid.getColumn(i);
    model.allDifferent(row.toArray(intVars), "DEFAULT").post();
    model.allDifferent(column.toArray(intVars), "DEFAULT").post();
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
