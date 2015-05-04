package com.flowlikeariver.kakuro;

import java.util.ArrayList;
import java.util.List;

public class RowDef {

private final int pos;
List<Cell> cells = new ArrayList<>();

public RowDef(int pos) {
  this.pos = pos;
}

public int size() {
  return cells.size();
}

public void draw() {
  for (Cell c : cells) {
    c.draw();
  }
  System.out.println();
}

public void addSolid() {
  cells.add(new SolidCell());
}

public void addEmpty(int n) {
  for (int i = 0; i < n; ++i) {
    cells.add(new EmptyCell());
  }
}

public void addDown(int n) {
  cells.add(new DownCell(n));
}

public void addAcross(int n) {
  cells.add(new AcrossCell(n));
}

public void addDownAcross(int down, int across) {
  cells.add(new DownAcrossCell(down, across));
}

public Cell get(int i) {
  return cells.get(i);
}
}
