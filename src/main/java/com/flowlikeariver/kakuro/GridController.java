package com.flowlikeariver.kakuro;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class GridController {

final Map<Integer, Map<Integer, Cell>> grid = new HashMap<>();
final LinkedList<RowDef> rows = new LinkedList<>();
final LinkedList<Sum> sums = new LinkedList<>();
RowDef currentRowDef;

public GridController() {
}

public int width() {
  return rows.get(0).size();
}

public int height() {
  return rows.size();
}

public Cell get(int i, int j) {
  if (grid.containsKey(i)) {
    if (grid.get(i).containsKey(j)) {
      return grid.get(i).get(j);
    }
    else {
      return null;
    }
  }
  else {
    return null;
  }
}

public void set(int i, int j, Cell cell) {
  if (!grid.containsKey(i)) {
    grid.put(i, new HashMap<>());
  }
  grid.get(i).put(j, cell);
}

public RowDef newRowDef() {
  currentRowDef = new RowDef(1 + rows.size());
  rows.add(currentRowDef);
  return currentRowDef;
}

public void draw() {
  System.out.println();
  rows.forEach(r -> r.draw());
}

public void addSolid() {
  currentRowDef.addSolid();
}

public void addEmpty(int n) {
  currentRowDef.addEmpty(n);
}

public void addDown(int n) {
  currentRowDef.addDown(n);
}

public void addAcross(int n) {
  currentRowDef.addAcross(n);
}

public void addDownAcross(int down, int across) {
  currentRowDef.addDownAcross(down, across);
}

public boolean defined(Cell c) {
  return null != c;
}

public void createAcrossSums() {
  for (int r = 0; r < height(); ++r) {
    for (int c = 0; c < width(); ++c) {
      Cell cell = get(r, c);
      if (cell.isAcross()) {
        Sum sum = new Sum(((Across) cell).getAcrossTotal());
        int pos = c + 1;
        Cell blank = get(r, pos);
        while (defined(blank) && blank.isEmpty()) {
          sum.add((EmptyCell) blank);
          ++pos;
          blank = get(r, pos);
        }
        sums.push(sum);
      }
    }
  }
}

public void createDownSums() {
  for (int c = 0; c < width(); ++c) {
    for (int r = 0; r < height(); ++r) {
      Cell cell = get(r, c);
      if (cell.isDown()) {
        Sum sum = new Sum(((Down) cell).getDownTotal());
        int pos = r + 1;
        Cell blank = get(pos, c);
        while (defined(blank) && blank.isEmpty()) {
          sum.add((EmptyCell) blank);
          ++pos;
          blank = get(pos, c);
        }
        sums.push(sum);
      }
    }
  }
}

public void createSums() {
  createAcrossSums();
  createDownSums();
}

public void parseDef() {
  int r = 0;
  for (RowDef row : rows) {
    for (int c = 0; c < row.size(); ++c) {
      set(r, c, row.get(c));
    }
    ++r;
  }
  createSums();
}

public int oneScan() {
  return sums.stream()
    .mapToInt(sum -> sum.solve())
    .sum();
}

public void solve() {
  parseDef();
  draw();
  int result = oneScan();
  while (result > 0) {
    System.out.println("\nresult " + result);
    draw();
    result = oneScan();
  }
}

}
