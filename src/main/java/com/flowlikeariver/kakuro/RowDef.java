package com.flowlikeariver.kakuro;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;

public class RowDef implements Iterable<Cell> {

private final int pos;
List<Cell> cells = new ArrayList<>();

public RowDef(int pos) {
  this.pos = pos;
}

public int size() {
  return cells.size();
}

public void draw() {
  cells.forEach(c -> c.draw());
  System.out.println();
}

public RowDef addSolid() {
  cells.add(new SolidCell());
  return this;
}

public RowDef addEmpty(int n) {
  cells.addAll(IntStream.rangeClosed(1, n)
    .mapToObj(i -> new EmptyCell())
    .collect(toList()));
  return this;
}

public RowDef addDown(int n) {
  cells.add(new DownCell(n));
  return this;
}

public RowDef addAcross(int n) {
  cells.add(new AcrossCell(n));
  return this;
}

public RowDef addDownAcross(int down, int across) {
  cells.add(new DownAcrossCell(down, across));
  return this;
}

public Cell get(int i) {
  return cells.get(i);
}

@Override
public Iterator<Cell> iterator() {
  return cells.iterator();
}
}
