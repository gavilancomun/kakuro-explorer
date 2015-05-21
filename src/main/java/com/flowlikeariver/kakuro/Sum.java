package com.flowlikeariver.kakuro;

import com.flowlikeariver.kakuro.cell.ValueCell;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Sum {

private final int total;
private final List<ValueCell> cells;

public Sum(int total, List<ValueCell> valueCells) {
  this.total = total;
  this.cells = valueCells;
}

// All different is part of the definition of a kakuro puzzle
private static boolean areAllDifferent(List<Integer> candidates) {
  return new HashSet<>(candidates).size() == candidates.size();
}

private static List<Integer> copyAdd(List<Integer> vs, int v) {
  return Stream.concat(vs.stream(), Stream.of(v)).collect(toList());
}

private static Stream<List<Integer>> permute(List<ValueCell> cells, int pos, int target, List<Integer> soFar) {
  if (target >= 1) {
    if (pos == (cells.size() - 1)) {
      return Stream.of(copyAdd(soFar, target));
    }
    else {
      return cells.get(pos).getValues().stream()
              .flatMap(v -> permute(cells, pos + 1, target - v, copyAdd(soFar, v)));
    }
  }
  else {
    return Stream.empty();
  }
}

// Exhaustive search for possible solutions
private static Stream<List<Integer>> permuteAll(List<ValueCell> cells, int target) {
  return permute(cells, 0, target, Collections.EMPTY_LIST);
}

public static int solveStep(List<ValueCell> cells, int target) {
  List<Possible> possibles = cells.stream().map(Possible::new).collect(toList());
  int last = cells.size() - 1;
  permuteAll(cells, target)
          .filter(perm -> cells.get(last).isPossible(perm.get(last)))
          .filter(Sum::areAllDifferent)
          .forEach(perm -> IntStream.rangeClosed(0, last)
                  .forEach(i -> possibles.get(i).add(perm.get(i))));
  return possibles.stream()
          .mapToInt(Possible::update)
          .sum();
}

public int solveStep() {
  return solveStep(cells, total);
}

}
