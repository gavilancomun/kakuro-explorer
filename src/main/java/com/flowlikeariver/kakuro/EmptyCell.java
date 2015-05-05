package com.flowlikeariver.kakuro;

import java.util.Set;
import java.util.TreeSet;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;
import java.util.stream.IntStream;

public class EmptyCell implements Cell {

Set<Integer> values;

public EmptyCell() {
  values = new TreeSet<>(IntStream.rangeClosed(1, 9).mapToObj(Integer::new).collect(toSet()));
}

@Override
public void draw() {
  if (1 == values.size()) {
    System.out.print(values.stream()
            .map(i -> "    <" + i + ">    ")
            .collect(joining()));
  }
  else {
    System.out.print(" ");
    System.out.print(IntStream.rangeClosed(1, 9)
            .mapToObj(i -> isPossible(i) ? String.valueOf(i) : ".")
            .collect(joining()));
    System.out.print(" ");
  }
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

boolean isPossible(int value) {
  return values.contains(value);
}

boolean remove(int value) {
  if (values.contains(value)) {
    values.remove(value);
    return true;
  }
  else {
    return false;
  }
}

Set<Integer> getValues() {
  return values;
}

}
