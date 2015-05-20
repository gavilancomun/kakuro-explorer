package com.flowlikeariver.kakuro.cell;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import static java.util.stream.Collectors.joining;
import java.util.stream.IntStream;

public class ValueCell implements Cell {

private Set<Integer> values;

public ValueCell() {
  values = new TreeSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
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
