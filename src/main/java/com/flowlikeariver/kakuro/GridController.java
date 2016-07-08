package com.flowlikeariver.kakuro;

import com.flowlikeariver.kakuro.cell.Across;
import com.flowlikeariver.kakuro.cell.EmptyCell;
import com.flowlikeariver.kakuro.cell.Down;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GridController {

final List<RowDef> rows = new ArrayList<>();
final List<Sum> sums = new ArrayList<>();
RowDef currentRowDef;

public GridController() {
}

public RowDef createRow() {
  currentRowDef = new RowDef();
  rows.add(currentRowDef);
  return currentRowDef;
}

public String draw() {
  return rows.stream()
          .map(RowDef::draw)
          .collect(Collectors.joining());
}

public void addEmpty() {
  currentRowDef.addEmpty();
}

public void addValue(int n) {
  currentRowDef.addValue(n);
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
              .map(cell -> (Across) cell)
              .ifPresent(cell -> sums.add(new Sum(cell.getAcrossTotal(),
                      row.stream()
                      .skip(c + 1)
                      .collect(new WhileValueCell()))));
    });
  });
}

public void createDownSums() {
  IntStream.range(0, rows.size()).forEach(r -> {
    IntStream.range(0, rows.get(0).size()).forEach(c -> {
      rows.get(r).get(c).filter(cell -> cell instanceof Down)
              .map(cell -> (Down) cell)
              .ifPresent(cell -> sums.add(new Sum(cell.getDownTotal(),
                      IntStream.range(r + 1, rows.size())
                      .mapToObj(pos -> rows.get(pos).get(c).orElse(new EmptyCell()))
                      .collect(new WhileValueCell()))));
    });
  });
}

public void createSums() {
  createAcrossSums();
  createDownSums();
}

public int scan() {
  return sums.stream().mapToInt(Sum::solveStep).sum();
}

public String solve() {
  createSums();
  System.out.println(draw());
  while (scan() > 0) {
    System.out.println(draw());
  }
  return draw();
}

}
