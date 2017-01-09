package com.flowlikeariver.kakuro.jacop;

import java.util.ArrayList;
import java.util.Arrays;
import static java.util.Arrays.asList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.jacop.constraints.Alldifferent;
import org.jacop.constraints.Constraint;
import org.jacop.constraints.SumInt;
import org.jacop.core.IntVar;
import org.jacop.core.Store;
import org.jacop.search.DepthFirstSearch;
import org.jacop.search.IndomainMin;
import org.jacop.search.InputOrderSelect;
import org.jacop.search.Search;
import org.jacop.search.SelectChoicePoint;

public class Kakuro {

static Store store;
static List<IntVar> vars = new ArrayList<>();

static {
  initStore();
}

public static void initStore() {
  store = new Store();
  vars.clear();
}

public static ValueCell v(Collection<Integer> values) {
  ValueCell valueCell = new ValueCell(store, values);
  vars.add(valueCell.logicVar);
  return valueCell;
}

public static ValueCell v() {
  return v(1, 2, 3, 4, 5, 6, 7, 8, 9);
}

public static ValueCell v(Integer... values) {
  return v(asList(values));
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

public static <T> boolean allDifferent(Collection<T> nums) {
  return nums.size() == new HashSet<>(nums).size();
}

public static <T> List<T> conj(List<T> items, T item) {
  List<T> result = new ArrayList<>(items);
  result.add(item);
  return result;
}

public static <T> List<T> concatLists(List<? extends T> a, List<? extends T> b) {
  return Stream.concat(a.stream(), b.stream()).collect(toList());
}

public static <T> List<List<T>> product(List<Set<T>> colls) {
  switch (colls.size()) {
    case 0:
      return Collections.EMPTY_LIST;
    case 1:
      return colls.get(0).stream()
              .map(Arrays::asList)
              .collect(toList());
    default:
      Collection<T> head = colls.get(0);
      List<Set<T>> tail = colls.stream().skip(1).collect(toList());
      List<List<T>> tailProd = product(tail);
      return head.stream()
              .flatMap(x -> tailProd.stream()
              .map(ys -> concatLists(asList(x), ys)))
              .collect(toList());
  }
}

public static List<List<Integer>> permuteAll(List<ValueCell> vs, int target) {
  List<Set<Integer>> values = vs.stream()
          .map(ValueCell::getValues)
          .collect(toList());
  return product(values).stream()
          .filter(x -> target == x.stream().mapToInt(i -> i).sum())
          .collect(toList());
}

public static boolean isPossible(ValueCell v, int n) {
  return v.contains(n);
}

public static <T> List<List<T>> transpose(List<List<T>> m) {
  if (m.isEmpty()) {
    return Collections.EMPTY_LIST;
  }
  else {
    return IntStream.range(0, m.get(0).size())
            .mapToObj(i -> m.stream()
            .map(col -> col.get(i))
            .collect(toList()))
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
    return Collections.EMPTY_LIST;
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
    return Collections.EMPTY_LIST;
  }
  else {
    return concatLists(asList(take(n, coll)), partitionAll(n, step, drop(step, coll)));
  }
}

public static <T> List<List<T>> partitionN(int n, List<T> coll) {
  return partitionAll(n, n, coll);
}

public static <T> T last(List<T> coll) {
  return coll.get(coll.size() - 1);
}

public static void constrainStep(List<ValueCell> cells, int total) {
  ArrayList<IntVar> logicVars = new ArrayList<>();
  cells.stream().map(c -> c.logicVar).forEach(logicVars::add);
  Constraint ctr = new Alldifferent(logicVars);
  store.impose(ctr);
  IntVar sum = new IntVar(store, total, total);
  Constraint sumConstr = new SumInt(store, logicVars, "==", sum);
  store.impose(sumConstr);
}

public static void constrainPair(Function<Cell, Integer> f, SimplePair<List<Cell>> pair) {
  List<Cell> notValueCells = pair.left;
  if (!pair.right.isEmpty()) {
    List<ValueCell> valueCells = pair.right.stream()
            .map(cell -> (ValueCell) cell)
            .collect(toList());
    constrainStep(valueCells, f.apply(last(notValueCells)));
  }
}

// returns (non-vals, vals)*
public static List<List<Cell>> gatherValues(List<Cell> line) {
  return partitionBy(v -> (v instanceof ValueCell), line);
}

public static List<SimplePair<List<Cell>>> pairTargetsWithValues(List<Cell> line) {
  return partitionN(2, gatherValues(line)).stream()
          .map(part -> new SimplePair<List<Cell>>(part.get(0), (1 == part.size()) ? Collections.EMPTY_LIST : part.get(1)))
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

public static boolean gridEquals(List<List<Cell>> g1, List<List<Cell>> g2) {
  if (g1.size() == g2.size()) {
    return IntStream.range(0, g1.size()).allMatch(i -> g1.get(i).equals(g2.get(i)));
  }
  else {
    return false;
  }
}

public static List<List<Cell>> solver(List<List<Cell>> grid) {
  System.out.println(drawGrid(grid));
  constrainGrid(grid);
  boolean ok = store.consistency();
  if (ok) {
    Search<IntVar> search = new DepthFirstSearch<>();
    SelectChoicePoint<IntVar> select = new InputOrderSelect<>(store, vars.toArray(new IntVar[1]), new IndomainMin<>());
    boolean result = search.labeling(store, select);
    System.out.println(drawGrid(grid));
  }
  return grid;
}

public static <T> Set<T> asSet(T... items) {
  return new HashSet<>(asList(items));
}

}
