package com.flowlikeariver.kakuro;

import java.util.Set;
import static java.util.stream.Collectors.toSet;
import java.util.stream.IntStream;

public class EmptyCell implements Cell {

Set<Integer> values;

public EmptyCell() {
  reset();
}

@Override
public void draw() {
  if (1 == values.size()) {
    values.forEach(i -> System.out.print("    <" + i + ">    "));
  }
  else {
    System.out.print(" ");
    IntStream.rangeClosed(1, 9).forEach(i -> System.out.print(isPossible(i) ? i : "."));
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

boolean removeImpossible(int value) {
  if (values.contains(value)) {
    values.remove(value);
    return true;
  }
  else {
    return false;
  }
}

final void reset() {
  values = IntStream.rangeClosed(1, 9).mapToObj(i -> i).collect(toSet());
}

Set<Integer> getValues() {
  return values;
}

}
