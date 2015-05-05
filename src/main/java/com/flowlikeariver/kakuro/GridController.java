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

public int width() {
  return rows.get(0).size();
}

public int height() {
  return rows.size();
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

public boolean isEmpty(Cell c) {
  return (c instanceof EmptyCell);
}

public void createAcrossSums() {
  IntStream.range(0, height()).forEach(r -> {
    IntStream.range(0, width()).forEach(c -> {
      get(r, c).filter(cell -> cell instanceof Across)
              .ifPresent(cell -> {
                Sum sum = new Sum(((Across) cell).getAcrossTotal());
                int pos = c + 1;
                Optional<Cell> optCell = get(r, pos);
                while (optCell.isPresent() && isEmpty(optCell.get())) {
                  sum.add((EmptyCell) optCell.get());
                  ++pos;
                  optCell = get(r, pos);
                }
                sums.add(sum);
              });
    });
  });
}

public void createDownSums() {
  IntStream.range(0, width()).forEach(c -> {
    IntStream.range(0, height()).forEach(r -> {
      get(r, c).filter(cell -> cell instanceof Down)
              .ifPresent(cell -> {
                Sum sum = new Sum(((Down) cell).getDownTotal());
                int pos = r + 1;
                Optional<Cell> optCell = get(pos, c);
                while (optCell.isPresent() && isEmpty(optCell.get())) {
                  sum.add((EmptyCell) optCell.get());
                  ++pos;
                  optCell = get(pos, c);
                }
                sums.add(sum);
              });
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
