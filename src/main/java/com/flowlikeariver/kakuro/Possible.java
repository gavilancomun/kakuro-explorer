package com.flowlikeariver.kakuro;

import com.flowlikeariver.kakuro.cell.ValueCell;
import java.util.Set;
import java.util.TreeSet;

public class Possible {

private final ValueCell cell;
private final Set<Integer> values = new TreeSet<>();

public Possible(ValueCell cell) {
  this.cell = cell;
}

public void add(int n) {
  values.add(n);
}

public int update() {
  int difference = cell.size() - values.size();
  cell.setValues(values);
  return difference;
}

}
