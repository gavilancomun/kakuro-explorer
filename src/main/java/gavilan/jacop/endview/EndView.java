package gavilan.jacop.endview;

import java.util.ArrayList;
import static java.util.Arrays.asList;
import static java.util.Collections.reverse;
import java.util.List;
import org.jacop.constraints.Alldifferent;
import org.jacop.constraints.And;
import org.jacop.constraints.In;
import org.jacop.constraints.Or;
import org.jacop.constraints.PrimitiveConstraint;
import org.jacop.constraints.XeqC;
import org.jacop.constraints.XeqY;
import org.jacop.core.IntDomain;
import org.jacop.core.IntVar;
import org.jacop.core.IntervalDomain;
import org.jacop.core.Store;
import org.jacop.search.DepthFirstSearch;
import org.jacop.search.IndomainMin;
import org.jacop.search.InputOrderSelect;
import org.jacop.search.Search;
import org.jacop.search.SelectChoicePoint;

public class EndView {

Store store = new Store();

public static Or or(PrimitiveConstraint... pcs) {
  return new Or(pcs);
}

public static And and(PrimitiveConstraint... pcs) {
  return new And(pcs);
}

private void constrain(int constraint, List<IntVar> vars) {
  if (constraint > 0) {
    int kind = vars.size();
    IntDomain blank = new IntervalDomain(1, vars.size() - 4);
    IntVar target = new IntVar(store, constraint, constraint);
    if (5 == kind) {
      store.impose(or(
              new XeqC(vars.get(0), constraint),
              and(new In(vars.get(0), blank),
                      new XeqC(vars.get(1), constraint))
      ));
    }
    else if (6 == kind) {
      store.impose(or(
              new XeqY(target, vars.get(0)),
              and(new In(vars.get(0), blank),
                      new XeqC(vars.get(1), constraint)),
              and(new In(vars.get(0), blank),
                      new In(vars.get(1), blank),
                      new XeqC(vars.get(2), constraint)
              )
      ));
    }
  }

}

int toDom(int rowCount, char c) {
  return c - 'A' + rowCount - 3;
}

private void parseConstraints(Grid grid, List<String> gridPic) {
  int rowCount = gridPic.size() - 2;
  String topRow = gridPic.get(0);
  String bottomRow = gridPic.get(gridPic.size() - 1);
  for (int i = 0; i < rowCount; ++i) {
    ArrayList<IntVar> row = grid.getRow(i);
    ArrayList<IntVar> column = grid.getColumn(i);
    store.impose(new Alldifferent(row));
    store.impose(new Alldifferent(column));
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
  List<IntVar> vars = grid.init(store);
  parseConstraints(grid, gridPic);
  boolean ok = store.consistency();
  if (ok) {
    Search<IntVar> search = new DepthFirstSearch<>();
    SelectChoicePoint<IntVar> select = new InputOrderSelect<>(store, vars.toArray(new IntVar[1]), new IndomainMin<>());
    boolean result = search.labeling(store, select);
    grid.drawGrid();
  }

}

public static void main(String[] args) {
  List<String> gridPic = asList(
          "  DCA  ",
          " ..... ",
          "B.....D",
          " ..... ",
          " .....D",
          " .....A",
          "  A    "
  );
  new EndView().solve(gridPic);
  List<String> gridPic2 = asList(
          "  AD D  ",
          "D...... ",
          " ...... ",
          " ......C",
          " ...... ",
          " ......B",
          " ...... ",
          " C CC   "
  );
  new EndView().solve(gridPic2);
  List<String> gridPic3 = asList(
          "  C     ",
          " ......C",
          " ......A",
          "B...... ",
          " ......A",
          " ......A",
          " ...... ",
          " B   A  "
  );
  new EndView().solve(gridPic3);
}

}
