package com.flowlikeariver.kakuro;

import java.util.Set;
import java.util.TreeSet;

public class EmptyCell implements Cell {

Set<Integer> values = new TreeSet<>();

public EmptyCell() {
  reset();
}

@Override
public void draw() {
  System.out.print(" ");
  if (1 == values.size()) {
    values.forEach(i -> {
      System.out.print("   <" + i + ">   ");
    });
  }
  else {
    for (int i = 1; i < 10; ++i) {
      if (isPossible(i)) {
        System.out.print(i);
      }
      else {
        System.out.print(".");
      }
    }
  }
  System.out.print(" ");
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

boolean setImpossible(int value) {
  if (values.contains(value)) {
    values.remove(value);
    return true;
  }
  else {
    return false;
  }
}

final void reset() {
  for (int i = 1; i < 10; ++i) {
    values.add(i);
  }
}

Set<Integer> getValues() {
  return values;
}

}
