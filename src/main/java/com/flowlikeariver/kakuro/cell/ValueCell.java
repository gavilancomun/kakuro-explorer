package com.flowlikeariver.kakuro.cell;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public class ValueCell implements Cell {

private Set<Integer> values;

public ValueCell() {
  values = new TreeSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
}

public boolean isPossible(int value) {
  return values.contains(value);
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

@Override
public void accept(Visitor visitor) {
  visitor.visitValue(this);
}

}
