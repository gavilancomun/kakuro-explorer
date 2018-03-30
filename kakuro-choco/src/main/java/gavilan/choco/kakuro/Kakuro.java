package gavilan.choco.kakuro;

import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

public class Kakuro {

static Model model;

static {
  initStore();
}

public static void initStore() {
  model = new Model();
}

public static ValueCell v(Collection<Integer> values) {
  IntVar v = model.intVar(values.stream().mapToInt(i -> i).toArray());
  ValueCell valueCell = new ValueCell(v);
  return valueCell;
}

public static ValueCell v(Integer... values) {
  return v(asList(values));
}

public static ValueCell v() {
  ValueCell valueCell = new ValueCell(model.intVar(IntStream.rangeClosed(1, 9).toArray()));
  return valueCell;
}

public static EmptyCell e() {
  return new EmptyCell();
}

public static DownCell d(int d) {
  return new DownCell(d);
}

public static AcrossCell a(int a) {
  return new AcrossCell(a);
}

public static DownAcrossCell da(int d, int a) {
  return new DownAcrossCell(d, a);
}

public static String drawRow(List<Cell> row) {
  return row.stream()
          .map(Cell::draw)
          .collect(joining()) + "\n";
}

public static String drawGrid(List<List<Cell>> grid) {
  return grid.stream()
          .map(Kakuro::drawRow)
          .collect(joining());
}

public static <T> List<T> concatLists(List<? extends T> a, List<? extends T> b) {
  return Stream.concat(a.stream(), b.stream()).collect(toList());
}

public static <T> List<List<T>> transpose(List<List<T>> m) {
  if (m.isEmpty()) {
    return Collections.emptyList();
  }
  else {
    return IntStream.range(0, m.get(0).size())
            .mapToObj(i -> m.stream().map(col -> col.get(i)).collect(toList()))
            .collect(toList());
  }
}

public static <T> List<T> takeWhile(Predicate<T> f, List<T> coll) {
  List<T> result = new ArrayList<>();
  for (T item : coll) {
    if (!f.test(item)) {
      return result;
    }
    result.add(item);
  }
  return result;
}

public static <T> List<T> drop(int n, List<T> coll) {
  return coll.stream().skip(n).collect(toList());
}

public static <T> List<T> take(int n, List<T> coll) {
  return coll.stream().limit(n).collect(toList());
}

public static <T> List<List<T>> partitionBy(Predicate<T> f, List<T> coll) {
  if (coll.isEmpty()) {
    return Collections.emptyList();
  }
  else {
    T head = coll.get(0);
    boolean fx = f.test(head);
    List<T> group = takeWhile(y -> fx == f.test(y), coll);
    return concatLists(asList(group), partitionBy(f, drop(group.size(), coll)));
  }
}

public static <T> List<List<T>> partitionAll(int n, int step, List<T> coll) {
  if (coll.isEmpty()) {
    return Collections.emptyList();
  }
  else {
    return concatLists(asList(take(n, coll)), partitionAll(n, step, drop(step, coll)));
  }
}

public static <T> List<List<T>> partitionN(int n, List<T> coll) {
  return partitionAll(n, n, coll);
}

public static Cell last(List<Cell> coll) {
  return coll.get(coll.size() - 1);
}

public static void constrainStep(List<ValueCell> cells, int total) {
  IntVar[] logicVars = new IntVar[cells.size()];
  for (int i = 0; i < cells.size(); ++i) {
    logicVars[i] = cells.get(i).logicVar;
  }
  model.allDifferent(logicVars, "DEFAULT").post();
  model.sum(logicVars, "=", total).post();
}

public static void constrainPair(Function<Cell, Integer> getTotal, SimplePair<List<Cell>> pair) {
  List<Cell> notValueCells = pair.left;
  if (!pair.right.isEmpty()) {
    List<ValueCell> valueCells = pair.right.stream()
            .map(cell -> (ValueCell) cell)
            .collect(toList());
    constrainStep(valueCells, getTotal.apply(last(notValueCells)));
  }
}

// returns (non-values, values)*
public static List<List<Cell>> gatherValues(List<Cell> line) {
  return partitionBy(v -> (v instanceof ValueCell), line);
}

public static List<SimplePair<List<Cell>>> pairTargetsWithValues(List<Cell> line) {
  List<List<Cell>> gathered = gatherValues(line);
  List<SimplePair<List<Cell>>> results = new ArrayList<>();
  for (int i = 0; i < gathered.size(); i += 2) {
    if (i + 1 < gathered.size()) {
      results.add(new SimplePair<>(gathered.get(i), gathered.get(i + 1)));
    }
    else {
      results.add(new SimplePair<>(gathered.get(i), Collections.emptyList()));
    }
  }
  return results;
}

public static void constrainLine(List<Cell> line, Function<Cell, Integer> f) {
  pairTargetsWithValues(line).forEach(pair -> constrainPair(f, pair));
}

public static void constrainRow(List<Cell> row) {
  constrainLine(row, x -> ((Across) x).getAcross());
}

public static void constrainColumn(List<Cell> column) {
  constrainLine(column, x -> ((Down) x).getDown());
}

public static void constrainGrid(List<List<Cell>> grid) {
  grid.forEach(Kakuro::constrainRow);
  transpose(grid).forEach(Kakuro::constrainColumn);
}

public static List<List<Cell>> solver(List<List<Cell>> grid) {
  System.out.println(drawGrid(grid));
  constrainGrid(grid);
  Solver solver = model.getSolver();
  boolean ok = solver.solve();
  if (ok) {
    System.out.println(drawGrid(grid));
  }
  return grid;
}

}
