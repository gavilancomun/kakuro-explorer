package com.flowlikeariver.kakuro;

import com.flowlikeariver.kakuro.cell.ValueCell;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Sum {

private final int total;
private final List<ValueCell> cells = new ArrayList<>();

public Sum(int total, Collection<ValueCell> valueCells) {
  this.total = total;
  cells.addAll(valueCells);
}

// All different is part of the definition of a kakuro puzzle
private boolean areAllDifferent(List<Integer> candidates) {
  return (new HashSet<>(candidates).size() == candidates.size());
}

private List<Integer> copyAdd(List<Integer> vs, int v) {
  return Stream.concat(vs.stream(), Stream.of(v)).collect(toList());
}

// Exhaustive search for possible solutions
private Stream<List<Integer>> permute(int pos, int target, List<Integer> candidates) {
  if (target >= 1) {
    if (pos == (cells.size() - 1)) {
      return Stream.of(copyAdd(candidates, target));
    }
    else {
      return cells.get(pos).getValues().stream()
              .flatMap(v -> permute(pos + 1, target - v, copyAdd(candidates, v)));
    }
  }
  else {
    return Stream.empty();
  }
}

private Stream<List<Integer>> permuteAll() {
  return permute(0, total, Collections.EMPTY_LIST);
}

public int solveStep() {
  List<Possible> possibles = cells.stream().map(Possible::new).collect(toList());
  int last = cells.size() - 1;
  permuteAll()
          .filter(p -> cells.get(last).isPossible(p.get(last)))
          .filter(this::areAllDifferent)
          .forEach(p -> IntStream.rangeClosed(0, last).forEach(i -> possibles.get(i).add(p.get(i))));
  return possibles.stream()
          .mapToInt(Possible::update)
          .sum();
}

}
