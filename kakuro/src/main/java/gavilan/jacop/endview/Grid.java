package gavilan.jacop.endview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;
import org.jacop.core.IntVar;
import org.jacop.core.Store;

public class Grid {

private final IntVar[][] grid;
private final int rowCount;

public Grid(int rowCount) {
  this.rowCount = rowCount;
  this.grid = new IntVar[rowCount][rowCount];
}

public List<IntVar> init(Store store) {
  List<IntVar> vars = new ArrayList<>();
  for (int i = 0; i < rowCount; ++i) {
    for (int j = 0; j < rowCount; ++j) {
      IntVar v = new IntVar(store, 1, rowCount);
      vars.add(v);
      grid[i][j] = v;
    }
  }
  return vars;
}

public ArrayList<IntVar> getRow(int n) {
  return new ArrayList<>(Arrays.asList(grid[n]));
}

public ArrayList<IntVar> getColumn(int n) {
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

public void drawGrid() {
  for (int i = 0; i < rowCount; ++i) {
    for (int j = 0; j < rowCount; ++j) {
      Arrays.stream(grid[i][j].dom().toIntArray())
              .mapToObj(this::display)
              .forEach(System.out::print);
    }
    System.out.println();
  }
}

}
