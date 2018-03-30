package gavilan.choco.kakuro;

import static java.util.Arrays.asList;
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
  init();
}

public static void init() {
  model = new Model();
}

public static ValueCell v(int... values) {
  IntVar v = model.intVar(values);
  return new ValueCell(v);
}

public static ValueCell v() {
  return v(IntStream.rangeClosed(1, 9).toArray());
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

public static <T> List<T> concat(List<? extends T> a, List<? extends T> b) {
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
  return coll.stream().takeWhile(f).collect(toList());
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
    return concat(asList(group), partitionBy(f, drop(group.size(), coll)));
  }
}

public static <T> List<List<T>> partitionAll(int n, int step, List<T> coll) {
  if (coll.isEmpty()) {
    return Collections.emptyList();
  }
  else {
    return concat(asList(take(n, coll)), partitionAll(n, step, drop(step, coll)));
  }
}

public static <T> List<List<T>> partitionN(int n, List<T> coll) {
  return partitionAll(n, n, coll);
}

public static <T> T last(List<T> coll) {
  return coll.get(coll.size() - 1);
}

public static void constrainStep(List<ValueCell> cells, int total) {
  IntVar[] logicVars = cells.stream()
          .map(c -> c.logicVar)
          .toArray(IntVar[]::new);
  model.allDifferent(logicVars, "DEFAULT").post();
  model.sum(logicVars, "=", total).post();
}

// returns (non-values, values)*
public static List<List<Cell>> gatherValues(List<Cell> line) {
  return partitionBy(v -> (v instanceof ValueCell), line);
}

public static void constrainLine(List<Cell> line, Function<Cell, Integer> f) {
  List<List<Cell>> gathered = gatherValues(line);
  for (int i = 0; i < gathered.size(); i += 2) {
    if (i + 1 < gathered.size()) {
      List<Cell> notValueCells = gathered.get(i);
      if (!gathered.get(i + 1).isEmpty()) {
        List<ValueCell> valueCells = gathered.get(i + 1).stream()
                .map(cell -> (ValueCell) cell)
                .collect(toList());
        constrainStep(valueCells, f.apply(last(notValueCells)));
      }
    }
  }
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
