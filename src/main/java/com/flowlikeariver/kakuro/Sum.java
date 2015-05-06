package com.flowlikeariver.kakuro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

public class Sum {

private final int total;
private final List<EmptyCell> cells = new ArrayList<>();
Map<Integer, Set<Integer>> possibles = new HashMap<>();

public Sum(int total) {
  this.total = total;
}

public Sum(int total, Collection<EmptyCell> emptyCells) {
  this.total = total;
  cells.addAll(emptyCells);
}

public void add(EmptyCell cell) {
  cells.add(cell);
}

public void addAll(Collection<EmptyCell> emptyCells) {
  cells.addAll(emptyCells);
}

private void addPossible(int pos, int value) {
  if (!possibles.containsKey(pos)) {
    possibles.put(pos, new HashSet<>());
  }
  possibles.get(pos).add(value);
}

// All different is part of the definition of a kakuro puzzle
private boolean allDifferent(List<Integer> candidates, int value) {
  List<Integer> trial = new ArrayList<>(candidates);
  trial.add(value);
  return (new HashSet<>(trial).size() == trial.size());
}

private void recordPossible(List<Integer> candidates, int value) {
  IntStream.range(0, candidates.size())
          .forEach(i -> addPossible(i, candidates.get(i)));
  addPossible(candidates.size(), value);
}

private void handleLast(EmptyCell cell, int target, List<Integer> candidates) {
  if (target >= 1) {
    if (cell.isPossible(target) && allDifferent(candidates, target)) {
      recordPossible(candidates, target);
    }
  }
}

private void handleNotLast(EmptyCell cell, int target, int pos, List<Integer> candidates) {
  if (target >= 1) {
    cell.getValues().forEach(v -> {
      List<Integer> trial = new ArrayList<>(candidates);
      trial.add(v);
      solvePart(target - v, pos + 1, trial);
    });
  }
}

private void solvePart(int target, int pos, List<Integer> candidates) {
  if (pos == (cells.size() - 1)) {
    handleLast(cells.get(pos), target, candidates);
  }
  else {
    handleNotLast(cells.get(pos), target, pos, candidates);
  }
}

private boolean isPossible(int pos, int value) {
  if (possibles.containsKey(pos)) {
    return possibles.get(pos).contains(value);
  }
  else {
    return false;
  }
}

private int getRemoveCount(int pos) {
  EmptyCell cell = cells.get(pos);
  return new HashSet<>(cell.getValues()).stream()
          .mapToInt(value -> isPossible(pos, value) ? 0 : cell.remove(value))
          .sum();
}

public int solve() {
  possibles = new HashMap<>();
  solvePart(total, 0, new ArrayList<>());
  return IntStream.range(0, cells.size())
          .map(this::getRemoveCount)
          .sum();
}

public int size() {
  return cells.size();
}

@Override
public String toString() {
  return "Sum: " + total;
}

}
