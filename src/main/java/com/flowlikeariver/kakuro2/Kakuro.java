package com.flowlikeariver.kakuro2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Kakuro {

public static ValueCell v() {
  return new ValueCell(IntStream.range(1, 10).mapToObj(i -> (Integer) i).collect(toSet()));
}

public static ValueCell v(Collection<Integer> values) {
  return new ValueCell(values);
}

public static ValueCell v(Integer... values) {
  return new ValueCell(Arrays.asList(values));
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
    .map(v -> v.draw())
    .collect(joining()) + "\n";
}

public static String drawGrid(List<List<Cell>> grid) {
  return grid.stream()
    .map(row -> drawRow(row))
    .collect(joining());
}

public static boolean allDifferent(List<Integer> nums) {
  return nums.size() == new HashSet<>(nums).size();
}

public static <T> List<T> conj(List<T> items, T item) {
  List<T> result = new ArrayList<>(items);
  result.add(item);
  return result;
}

public static List<List<Integer>> permute(List<ValueCell> vs, int target, List<Integer> soFar) {
  if (target >= 1) {
    if (soFar.size() == (vs.size() - 1)) {
      List<List<Integer>> result = new ArrayList<>();
      result.add(conj(soFar, target));
      return result;
    }
    else {
      return vs.get(soFar.size()).values.stream()
        .map(n -> permute(vs, (target - n), conj(soFar, n)))
        .flatMap(perm -> perm.stream())
        .collect(toList());
    }
  }
  else {
    return Collections.EMPTY_LIST;
  }
}

public static List<List<Integer>> permuteAll(List<ValueCell> vs, int target) {
  return permute(vs, target, new ArrayList<>());
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
      .mapToObj(i -> IntStream.range(0, m.size())
        .mapToObj(j -> m.get(j).get(i))
        .collect(toList()))
      .collect(toList());
  }
}

public static <T> List<T> takeWhile(Predicate<T> f, List<T> coll) {
  List<T> result = new ArrayList();
  for (T item : coll) {
    if (!f.test(item)) {
      break;
    }
    result.add(item);
  }
  return result;
}

public static <T> List<T> concatLists(List<? extends T> a, List<? extends T> b) {
  return Stream.concat(a.stream(), b.stream()).collect(toList());
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
    return concatLists(Arrays.asList(group), partitionBy(f, drop(group.size(), coll)));
  }
}

public static <T> List<List<T>> partitionAll(int n, int step, List<T> coll) {
  if (coll.isEmpty()) {
    return Collections.EMPTY_LIST;
  }
  else {
    return concatLists(Arrays.asList(take(n, coll)), partitionAll(n, step, drop(step, coll)));
  }
}

public static <T> List<List<T>> partitionN(int n, List<T> coll) {
  return partitionAll(n, n, coll);
}

public static <T> T last(List<T> coll) {
  return coll.get(coll.size() - 1);
}

public static List<ValueCell> solveStep(List<ValueCell> cells, int total) {
  int finalIndex = cells.size() - 1;
  List<List<Integer>> perms = permuteAll(cells, total).stream()
    .filter(v -> isPossible(last(cells), v.get(finalIndex)))
    .filter(Kakuro::allDifferent)
    .collect(toList());
  return transpose(perms).stream()
    .map(Kakuro::v)
    .collect(toList());
}

public static List<Cell> solvePair(Function<Cell, Integer> f, SimplePair<List<Cell>> pair) {
  List<Cell> notValueCells = pair.left;
  if (pair.right.isEmpty()) {
    return notValueCells;
  }
  else {
    List<ValueCell> valueCells = pair.right.stream()
      .map(cell -> (ValueCell) cell)
      .collect(toList());
    List<ValueCell> newValueCells = solveStep(valueCells, f.apply(last(notValueCells)));
    return concatLists(notValueCells, newValueCells);
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

public static List<Cell> solveLine(List<Cell> line, Function<SimplePair<List<Cell>>, List<Cell>> pairSolver) {
  return pairTargetsWithValues(line).stream()
    .map(pair -> pairSolver.apply(pair))
    .flatMap(solved -> solved.stream())
    .collect(toList());
}

public static List<Cell> solveRow(List<Cell> row) {
  return solveLine(row, v -> solvePair(x -> ((Across) x).getAcross(), v));
}

public static List<Cell> solveColumn(List<Cell> column) {
  return solveLine(column, v -> solvePair(x -> ((Down) x).getDown(), v));
}

public static List<List<Cell>> solveGrid(List<List<Cell>> grid) {
  List<List<Cell>> rowsDone = grid.stream()
    .map(Kakuro::solveRow).collect(toList());
  List<List<Cell>> colsDone = transpose(rowsDone).stream()
    .map(Kakuro::solveColumn)
    .collect(toList());
  return transpose(colsDone);
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
  List<List<Cell>> g = solveGrid(grid);
  if (gridEquals(g, grid)) {
    return g;
  }
  else {
    return solver(g);
  }
}

}
