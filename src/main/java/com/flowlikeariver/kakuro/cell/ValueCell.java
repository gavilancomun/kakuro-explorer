package com.flowlikeariver.kakuro.cell;

import java.util.Set;
import java.util.TreeSet;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;
import java.util.stream.IntStream;

public class ValueCell implements Cell {

private Set<Integer> values;

public ValueCell() {
  values = new TreeSet<>(IntStream.rangeClosed(1, 9).mapToObj(Integer::new).collect(toSet()));
}

@Override
public boolean isAcross() {
  return false;
}

@Override
public boolean isDown() {
  return false;
}

@Override
public boolean isEmpty() {
  return true;
}

public boolean isPossible(int value) {
  return values.contains(value);
}

@Override
public String draw() {
  if (1 == values.size()) {
    return values.stream()
            .map(i -> "     " + i + "    ")
            .collect(joining());
  }
  else {
    return " "
            + IntStream.rangeClosed(1, 9)
            .mapToObj(i -> isPossible(i) ? String.valueOf(i) : ".")
            .collect(joining());
  }
}

public int size() {
  return values.size();
}

public Set<Integer> getValues() {
  return values;
}

public void setValues(Set<Integer> values) {
  this.values = values;
}
}
