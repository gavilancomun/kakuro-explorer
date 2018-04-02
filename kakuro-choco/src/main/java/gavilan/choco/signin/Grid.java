package gavilan.choco.signin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

public class Grid {

public IntVar[][] grid;
private final int rowCount;
private final Model model;

public Grid(Model model, int rowCount) {
  this.rowCount = rowCount;
  this.grid = new IntVar[rowCount][rowCount];
  this.model = model;
}

public List<IntVar> init(Model model) {
  List<IntVar> vars = new ArrayList<>();
  for (int i = 0; i < rowCount; ++i) {
    for (int j = 0; j < rowCount; ++j) {
      IntVar v = model.intVar(IntStream.rangeClosed(1, rowCount).toArray());
      vars.add(v);
      grid[i][j] = v;
    }
  }
  return vars;
}

public IntVar[] getRow(int n) {
  return grid[n];
}

public IntVar[] getColumn(int n) {
  return IntStream.range(0, grid.length)
          .mapToObj(i -> grid[i][n])
          .toArray(IntVar[]::new);
}

private String display(int n) {
  return "" + n;
}

private int[] elements(IntVar iv) {
  return model.getDomainUnion(iv);
}

public void drawGrid() {
  for (int i = 0; i < rowCount; ++i) {
    for (int j = 0; j < rowCount; ++j) {
      Arrays.stream(elements(grid[i][j]))
              .mapToObj(this::display)
              .forEach(System.out::print);
    }
    System.out.println();
  }
}

}
