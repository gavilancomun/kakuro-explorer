package com.flowlikeariver.kakuro;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.IntStream;

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

public boolean isEmpty(Cell c) {
  return defined(c) && (c instanceof EmptyCell);
}

public void createAcrossSums() {
  IntStream.range(0, height()).forEach(r -> {
    IntStream.range(0, width()).forEach(c -> {
      Cell cell = get(r, c);
      if (cell instanceof Across) {
        Sum sum = new Sum(((Across) cell).getAcrossTotal());
        int pos = c + 1;
        Cell nextCell = get(r, pos);
        while (isEmpty(nextCell)) {
          sum.add((EmptyCell) nextCell);
          ++pos;
          nextCell = get(r, pos);
        }
//        System.out.println("across " + r + " " + c + " " + sum.size());
        sums.push(sum);
      }
    });
  });
}

public void createDownSums() {
  IntStream.range(0, width()).forEach(c -> {
    IntStream.range(0, height()).forEach(r -> {
      Cell cell = get(r, c);
      if (cell instanceof Down) {
        Sum sum = new Sum(((Down) cell).getDownTotal());
        int pos = r + 1;
        Cell nextCell = get(pos, c);
        while (isEmpty(nextCell)) {
          sum.add((EmptyCell) nextCell);
          ++pos;
          nextCell = get(pos, c);
        }
//        System.out.println("down " + r + " " + c + " " + sum.size());
        sums.push(sum);
      }
    });
  });
}

public void createSums() {
  createAcrossSums();
  createDownSums();
}

public void parseDef() {
  IntStream.range(0, rows.size()).forEach(r -> {
    RowDef row = rows.get(r);
    IntStream.range(0, row.size())
            .forEach(c -> set(r, c, row.get(c)));
  });
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
