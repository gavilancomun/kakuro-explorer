package com.flowlikeariver.kakuro;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class GridController {

final List<RowDef> rows = new ArrayList<>();
final List<Sum> sums = new ArrayList<>();
RowDef currentRowDef;

public GridController() {
}

public Optional<Cell> get(int i, int j) {
  return (i >= rows.size()) ? Optional.empty() : rows.get(i).get(j);
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

public void createAcrossSums() {
  rows.forEach(row -> {
    IntStream.range(0, rows.get(0).size()).forEach(c -> {
      row.get(c).filter(cell -> cell instanceof Across)
              .ifPresent(cell -> sums.add(new Sum(((Across) cell).getAcrossTotal(),
                                      row.stream()
                                      .skip(c + 1)
                                      .collect(new WhileEmpty()))));
    });
  });
}

public void createDownSums() {
  IntStream.range(0, rows.size()).forEach(r -> {
    IntStream.range(0, rows.get(0).size()).forEach(c -> {
      get(r, c).filter(cell -> cell instanceof Down)
              .ifPresent(cell -> sums.add(new Sum(((Down) cell).getDownTotal(),
                                      IntStream.range(r + 1, rows.size())
                                      .mapToObj(pos -> get(pos, c).orElse(new SolidCell()))
                                      .collect(new WhileEmpty()))));
    });
  });
}

public void createSums() {
  createAcrossSums();
  createDownSums();
}

public int oneScan() {
  return sums.stream()
          .mapToInt(sum -> sum.solve())
          .sum();
}

public void solve() {
  createSums();
  draw();
  int result = oneScan();
  while (result > 0) {
    System.out.println("\nresult " + result);
    draw();
    result = oneScan();
  }
}

}
