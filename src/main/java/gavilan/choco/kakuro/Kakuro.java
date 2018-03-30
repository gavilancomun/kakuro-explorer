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
static List<IntVar> vars = new ArrayList<>();

static {
  initStore();
}

public static void initStore() {
  model = new Model();
  vars.clear();
}

public static ValueCell v(Collection<Integer> values) {
  IntVar v = model.intVar(values.stream().mapToInt(i -> i).toArray());
  ValueCell valueCell = new ValueCell(v);
  vars.add(v);
  return valueCell;
}

public static ValueCell v(Integer... values) {
  return v(asList(values));
}

public static ValueCell v() {
  ValueCell valueCell = new ValueCell(model.intVar(IntStream.rangeClosed(1, 9).toArray()));
  vars.add(valueCell.logicVar);
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

public static <T> List<T> concatLists(List<T> a, List<T> b) {
  return Stream.concat(a.stream(), b.stream()).collect(toList());
}

public static List<List<Cell>> transpose(List<List<Cell>> m) {
  if (m.isEmpty()) {
    return Collections.emptyList();
  }
  else {
    return IntStream.range(0, m.get(0).size())
            .mapToObj(i -> m.stream().map(col -> col.get(i)).collect(toList()))
            .collect(toList());
  }
}

public static List<Cell> takeWhile(Predicate<Cell> f, List<Cell> coll) {
  List<Cell> result = new ArrayList<>();
  for (Cell item : coll) {
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

public static List<List<Cell>> partitionBy(Predicate<Cell> f, List<Cell> coll) {
  if (coll.isEmpty()) {
    return Collections.emptyList();
  }
  else {
    Cell head = coll.get(0);
    boolean fx = f.test(head);
    List<Cell> group = takeWhile(y -> fx == f.test(y), coll);
    return concatLists(asList(group), partitionBy(f, drop(group.size(), coll)));
  }
}

public static <T> List<List<T>> partitionAll(int n, int step, List<T> coll) {
  if (coll.isEmpty()) {
    return Collections.emptyList();
  }
  else {
    List<T> tailN = new ArrayList<>();
    coll.stream()
            .skip(step)
            .forEach(c -> tailN.add(c));
    List<T> takeN = take(n, coll);
    List<List<T>> partitioned = partitionAll(n, step, tailN);
    List<List<T>> result = new ArrayList<>();
    result.add(takeN);
    result.addAll(partitioned);
    return result;
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
          .toArray(() -> new IntVar[cells.size()]);
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

// returns (non-vals, vals)*
public static List<List<Cell>> gatherValues(List<Cell> line) {
  return partitionBy(v -> (v instanceof ValueCell), line);
}

public static List<SimplePair<List<Cell>>> pairTargetsWithValues(List<Cell> line) {
  return partitionN(2, gatherValues(line)).stream()
          .map(part -> new SimplePair<List<Cell>>(part.get(0), (1 == part.size()) ? Collections.emptyList() : part.get(1)))
          .collect(toList());
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
