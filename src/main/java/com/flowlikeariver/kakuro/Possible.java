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

/**
 * @return the cell
 */
public ValueCell getCell() {
  return cell;
}

public void add(int n) {
  values.add(n);
}

public int update() {
  int previousSize = cell.size();
  cell.setValues(values);
  return previousSize - cell.size();
}

}
