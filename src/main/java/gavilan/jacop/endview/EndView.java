package gavilan.jacop.endview;

import java.util.ArrayList;
import java.util.Arrays;
import static java.util.Arrays.asList;
import static java.util.Collections.reverse;
import java.util.List;
import org.jacop.constraints.Alldifferent;
import org.jacop.constraints.And;
import org.jacop.constraints.Or;
import org.jacop.constraints.XeqY;
import org.jacop.core.IntVar;
import org.jacop.core.Store;
import org.jacop.search.DepthFirstSearch;
import org.jacop.search.IndomainMin;
import org.jacop.search.InputOrderSelect;
import org.jacop.search.Search;
import org.jacop.search.SelectChoicePoint;

public class EndView {

Store store = new Store();
IntVar blank = new IntVar(store, 1, 1);

ArrayList<IntVar> getRow(IntVar[][] grid, int n) {
  return new ArrayList(Arrays.asList(grid[n]));
}

ArrayList<IntVar> getColumn(IntVar[][] grid, int n) {
  ArrayList<IntVar> results = new ArrayList<>();
  for (int i = 0; i < grid.length; ++i) {
    results.add(grid[i][n]);
  }
  return results;
}

String display(int n) {
  if (1 == n) {
    return "X";
  }
  else {
    return "" + (char) ('A' + (n - 2));
  }
}

public void drawGrid(IntVar[][] grid) {
  int rowCount = grid.length;
  for (int i = 0; i < rowCount; ++i) {
    for (int j = 0; j < rowCount; ++j) {
      Arrays.stream(grid[i][j].dom().toIntArray())
              .mapToObj(this::display)
              .forEach(System.out::print);
    }
    System.out.println();
  }
}

private void constrain(int constraint, ArrayList<IntVar> vars) {
  if (constraint > 0) {
    IntVar target = new IntVar(store, constraint, constraint);
    store.impose(new Or(new XeqY(target, vars.get(0)),
            new And(new XeqY(blank, vars.get(0)),
                    new XeqY(target, vars.get(1)))
    ));
  }
}

int toDom(char c) {
  return c - 'A' + 2;
}

public void solve() {
  List<IntVar> vars = new ArrayList<>();
  List<String> gridPic = asList(
          "  DCA  ",
          " ..... ",
          "B.....D",
          " ..... ",
          " .....D",
          " .....A",
          "  A    "
  );
  int rowCount = gridPic.size() - 2;
  IntVar[][] grid = new IntVar[rowCount][rowCount];
  for (int i = 0; i < rowCount; ++i) {
    for (int j = 0; j < rowCount; ++j) {
      IntVar v = new IntVar(store, 1, rowCount);
      vars.add(v);
      grid[i][j] = v;
    }
  }
  String topRow = gridPic.get(0);
  String bottomRow = gridPic.get(gridPic.size() - 1);
  for (int i = 0; i < rowCount; ++i) {
    ArrayList<IntVar> row = getRow(grid, i);
    ArrayList<IntVar> column = getColumn(grid, i);
    store.impose(new Alldifferent(row));
    store.impose(new Alldifferent(column));
    int topConstraint = toDom(topRow.charAt(i + 1));
    constrain(topConstraint, column);
    reverse(column);
    int bottomConstraint = toDom(bottomRow.charAt(i + 1));
    constrain(bottomConstraint, column);
    int leftConstraint = toDom(gridPic.get(i + 1).charAt(0));
    constrain(leftConstraint, row);
    int rightContraint = toDom(gridPic.get(i + 1).charAt(gridPic.get(i + 1).length() - 1));
    reverse(row);
    constrain(rightContraint, row);
  }
  boolean ok = store.consistency();
  if (ok) {
    Search<IntVar> search = new DepthFirstSearch<>();
    SelectChoicePoint<IntVar> select = new InputOrderSelect<>(store, vars.toArray(new IntVar[1]), new IndomainMin<>());
    boolean result = search.labeling(store, select);
    drawGrid(grid);
  }

}

public static void main(String[] args) {
  new EndView().solve();
}

}
