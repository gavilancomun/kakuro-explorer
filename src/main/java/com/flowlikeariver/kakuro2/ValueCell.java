package com.flowlikeariver.kakuro2;

import java.util.HashSet;
import java.util.Set;
import static java.util.stream.Collectors.joining;
import java.util.stream.IntStream;

public class ValueCell implements Cell {

Set<Integer> values = new HashSet<>();

public ValueCell(Set<Integer> values) {
  this.values = values;
}

@Override
public String draw() {
  if (1 == values.size()) {
    return "     " + values.stream().findFirst().get() + "    ";
  }
  else {
    return IntStream.range(1, 10)
      .mapToObj(n -> values.contains(n) ? String.valueOf(n) : ".")
      .collect(joining());
  }
}

public boolean contains(int n) {
  return values.contains(n);
}

}
