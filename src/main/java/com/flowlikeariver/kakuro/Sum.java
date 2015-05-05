package com.flowlikeariver.kakuro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static java.util.stream.Collectors.joining;
import java.util.stream.IntStream;

public class Sum {

private final int total;
private final List<EmptyCell> cells = new ArrayList<>();
Map<Integer, Set<Integer>> possibles = new HashMap<>();
private final LinkedList<Integer> candidates = new LinkedList<>();

public Sum(int total) {
  this.total = total;
}

public void add(EmptyCell cell) {
  cell.hasSum(this);
  cells.add(cell);
}

private void addPossible(int pos, int value) {
  if (!possibles.containsKey(pos)) {
    possibles.put(pos, new HashSet<>());
  }
  possibles.get(pos).add(value);
}

private int handleCandidate(int value) {
  List<Integer> trial = new ArrayList<>(candidates);
  trial.add(value);
  System.out.print("handle [" + total + "] " + trial.stream().map(i -> i.toString()).collect(joining(" ")));
  Set<Integer> done = new HashSet<>(trial);
  System.out.println((done.size() < trial.size()) ? " fail" : " success");
  if (done.size() < trial.size()) {
    return 0;
  }
  else {
    IntStream.range(0, trial.size())
            .forEach(i -> addPossible(i, trial.get(i)));
    return 1;
  }
}

private int solvePart(int target, int pos) {
  System.out.println("solvePart: " + target + " " + pos);
  int result = 0;
  if (target < 1) {
    return 0;
  }
  else {
    EmptyCell cell = cells.get(pos);
    if (pos == (cells.size() - 1)) {
      if (3 == total) {
        System.out.println("solvePart: " + cell.getValues().stream().map(i -> i.toString()).collect(joining(" ")));
      }
      return cell.isPossible(target) ? handleCandidate(target) : 0;
    }
    else {
      for (int v : cell.getValues()) {
        candidates.push(v);
        if (3 == total) {
          System.out.println("solvePart: value [" + v + "] " + candidates.stream()
                  .map(i -> i.toString())
                  .collect(joining(" ")));
        }
        result += solvePart(target - v, pos + 1);
        candidates.pop();
      }
    }
    return result;
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
  solvePart(total, 0);
  int result = countRecentImpossibles();
  System.out.println("solve: " + total + " " + result);
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
