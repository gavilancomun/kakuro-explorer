package com.flowlikeariver.kakuro;

import com.flowlikeariver.kakuro.cell.ValueCell;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;

public class Sum {

private final int total;
private final List<ValueCell> cells = new ArrayList<>();
List<Set<Integer>> possibles;

public Sum(int total, Collection<ValueCell> emptyCells) {
  this.total = total;
  cells.addAll(emptyCells);
}

// All different is part of the definition of a kakuro puzzle
private boolean allDifferent(int value, List<Integer> candidates) {
  List<Integer> trial = new ArrayList<>(candidates);
  trial.add(value);
  return (new HashSet<>(trial).size() == trial.size());
}

private void solvePart(int pos, int target, List<Integer> candidates) {
  if (target >= 1) {
    if (pos == (cells.size() - 1)) {
      if (cells.get(pos).isPossible(target) && allDifferent(target, candidates)) {
        IntStream.range(0, candidates.size())
                .forEach((i) -> possibles.get(i).add(candidates.get(i)));
        possibles.get(candidates.size()).add(target);
      }
    }
    else {
      cells.get(pos).getValues().forEach(v -> {
        List<Integer> trial = new ArrayList<>(candidates);
        trial.add(v);
        solvePart(pos + 1, target - v, trial);
      });
    }
  }
}

private int getRemoveCount(int pos) {
  ValueCell cell = cells.get(pos);
  return new HashSet<>(cell.getValues()).stream()
          .mapToInt(value -> possibles.get(pos).contains(value) ? 0 : cell.remove(value))
          .sum();
}

public int solve() {
  possibles = cells.stream().map(cell -> new HashSet<Integer>()).collect(toList());
  solvePart(0, total, new ArrayList<>());
  return IntStream.range(0, cells.size())
          .map(this::getRemoveCount)
          .sum();
}

}
