package gavilan.choco.signin;

import java.util.List;
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

private void parseConstraints(Grid grid, List<String> gridPic) {
  for (int pos = 0; pos < gridPic.size(); pos += 2) {
    int i = pos / 2;
    var row = grid.getRow(i);
    var column = grid.getColumn(i);
    model.allDifferent(row).post();
    model.allDifferent(column).post();
    var rowParts = gridPic.get(pos).split("");
    for (int partPos = 1; partPos < rowParts.length; partPos += 2) {
      var sign = rowParts[partPos];
      int leftPos = partPos / 2;
      var left = row[leftPos];
      var right = row[leftPos + 1];
//      System.out.println("row " + i + " leftPos " + leftPos + " sign " + sign);
      switch (sign) {
      case "+":
        model.arithm(right, "-", left, "=", 1).post();
        break;
      case "-":
        model.arithm(left, "-", right, "=", 1).post();
        break;
      default:
        model.distance(left, right, "!=", 1).post();
      }
    }
    if (pos + 1 < gridPic.size()) {
      var columnParts = gridPic.get(pos + 1).split("");
      for (int partPos = 0; partPos < columnParts.length; partPos += 2) {
        var sign = columnParts[partPos];
        int leftPos = partPos / 2;
        var left = row[leftPos];
        var right = grid.getRow(i + 1)[leftPos];
//        System.out.println("row " + i + " top " + leftPos + " sign " + sign);
        switch (sign) {
        case "+":
          model.arithm(right, "-", left, "=", 1).post();
          break;
        case "-":
          model.arithm(left, "-", right, "=", 1).post();
          break;
        default:
          model.distance(left, right, "!=", 1).post();
        }
      }
    }
  }
}

public void solve(List<String> gridPic) {
  int rowCount = (gridPic.size() + 1) / 2;
  var grid = new Grid(model, rowCount);
  grid.init(model);
  parseConstraints(grid, gridPic);
  var ok = model.getSolver().solve();
  if (ok) {
    System.out.println("");
    grid.drawGrid();
  }
}

}
