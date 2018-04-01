package gavilan.choco.signin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.iterators.DisposableValueIterator;

public class Grid {

public IntVar[][] grid;
private final int rowCount;

public Grid(int rowCount) {
  this.rowCount = rowCount;
  this.grid = new IntVar[rowCount][rowCount];
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

private List<Integer> elements(IntVar iv) {
  List<Integer> results = new ArrayList<>();
  DisposableValueIterator vit = iv.getValueIterator(true);
  while (vit.hasNext()) {
    int v = vit.next();
    results.add(v);
  }
  vit.dispose();
  return results;
}

public void drawGrid() {
  for (int i = 0; i < rowCount; ++i) {
    for (int j = 0; j < rowCount; ++j) {
      elements(grid[i][j]).stream()
              .map(this::display)
              .forEach(System.out::print);
    }
    System.out.println();
  }
}

}
