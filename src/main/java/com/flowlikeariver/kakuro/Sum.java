package com.flowlikeariver.kakuro;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class Sum {

private final int total;
private final LinkedList<EmptyCell> cells = new LinkedList<>();
Map<Integer, Set<Integer>> possibles = new HashMap<>();
private final LinkedList<Integer> candidate = new LinkedList<>();

public Sum(int total) {
  this.total = total;
}

public void add(EmptyCell cell) {
  cells.push(cell);
}

private int addPossible(int pos, int value) {
  if (!possibles.containsKey(pos)) {
    possibles.put(pos, new HashSet<>());
  }
  possibles.get(pos).add(value);
  return 1;
}

private int handleCandidate(int value) {
  LinkedList<Integer> trial = new LinkedList<>(candidate);
  Set<Integer> done = new HashSet<>();
  trial.push(value);
  for (int v : trial) {
    if (done.contains(v)) {
      return 0;
    }
    done.add(v);
  }
  int pos = 0;
  for (int v : trial) {
    addPossible(pos, v);
    ++pos;
  }
  return 1;
}

private int solvePart(int total, int pos) {
  int result = 0;
  if (total < 1) {
    return 0;
  }
  EmptyCell cell = cells.get(pos);
  if (pos == (cells.size() - 1)) {
    return cell.isPossible(total) ? handleCandidate(total) : 0;
  }
  else {
    for (int value : cell.getValues()) {
      candidate.push(value);
      result += solvePart(total - value, pos + 1);
      candidate.pop();
    }
  }
  return result;
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
    Set<Integer> values = new HashSet<>(cell.getValues());
    for (int value : values) {
      if (!isPossible(pos, value)) {
        result += cell.removeImpossible(value) ? 1 : 0;
      }
    }
    ++pos;
  }
  return result;
}

public int solve() {
  possibles = new HashMap<>();
  solvePart(total, 0);
  int result = countRecentImpossibles();
  System.out.println("solve: " + result);
  return result;
}

}
