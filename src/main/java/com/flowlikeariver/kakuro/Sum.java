package com.flowlikeariver.kakuro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
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
private boolean allDifferent(LinkedList<Integer> candidates, int value) {
  List<Integer> trial = new ArrayList<>(candidates);
  trial.add(value);
  return (new HashSet<>(trial).size() == trial.size());
}

private void recordPossible(LinkedList<Integer> candidates, int value) {
  IntStream.range(0, candidates.size())
          .forEach(i -> addPossible(i, candidates.get(i)));
  addPossible(candidates.size(), value);
}

private void handleLast(EmptyCell cell, int target, LinkedList<Integer> candidates) {
  if (target >= 1) {
    if (cell.isPossible(target) && allDifferent(candidates, target)) {
      recordPossible(candidates, target);
    }
  }
}

private void handleNotLast(EmptyCell cell, int target, int pos, LinkedList<Integer> candidates) {
  if (target >= 1) {
    for (int v : cell.getValues()) {
      candidates.add(v);
      solvePart(target - v, pos + 1, candidates);
      candidates.removeLast();
    }
  }
}

private void solvePart(int target, int pos, LinkedList<Integer> candidates) {
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

public int countRecentImpossibles() {
  int result = 0;
  int pos = 0;
  for (EmptyCell cell : cells) {
    for (int value : new HashSet<>(cell.getValues())) {
      if (isPossible(pos, value)) {
        result += 0;
      }
      else {
        result += cell.remove(value) ? 1 : 0;
      }
    }
    ++pos;
  }
  return result;
}

public int solve() {
  possibles = new HashMap<>();
  solvePart(total, 0, new LinkedList<>());
  int result = countRecentImpossibles();
  return result;
}

public int size() {
  return cells.size();
}

@Override
public String toString() {
  return "Sum: " + total;
}

}
