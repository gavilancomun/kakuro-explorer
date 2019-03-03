package gavilan.choco.endview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

public class Grid {

private final IntVar[][] grid;
private final int rowCount;

public Grid(int rowCount) {
  this.rowCount = rowCount;
  this.grid = new IntVar[rowCount][rowCount];
}

public List<IntVar> init(Model model) {
  var vars = new ArrayList<IntVar>();
  for (int i = 0; i < rowCount; ++i) {
    for (int j = 0; j < rowCount; ++j) {
      var v = model.intVar(range());
      vars.add(v);
      grid[i][j] = v;
    }
  }
  return vars;
}

  private int[] range() {
    return IntStream.rangeClosed(1, rowCount).toArray();
  }

public List<IntVar> getRow(int n) {
  return new ArrayList<>(Arrays.asList(grid[n]));
}

public List<IntVar> getColumn(int n) {
  return new ArrayList<>(IntStream.range(0, grid.length)
          .mapToObj(i -> grid[i][n])
          .collect(toList()));
}

private String display(int n) {
  if (n <= (rowCount - 4)) {
    return "X";
  }
  else {
    return "" + (char) ('A' + (n - (rowCount - 3)));
  }
}

private List<Integer> elements(IntVar iv) {
  var results = new ArrayList<Integer>();
  var vit = iv.getValueIterator(true);
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
