package com.flowlikeariver.kakuro;

import org.junit.Test;

public class TestSolver {

@Test
public void test1() {
  GridController grid = new GridController();
  grid.createRow().addSolid().addDown(4).addDown(22).addSolid().addDown(16).addDown(3);
  grid.createRow().addAcross(3).addEmpty(2).addDownAcross(16, 6).addEmpty(2);
  grid.createRow().addAcross(18).addEmpty(5);
  grid.createRow().addSolid().addDownAcross(17, 23).addEmpty(3).addDown(14);
  grid.createRow().addAcross(9).addEmpty(2).addAcross(6).addEmpty(2);
  grid.createRow().addAcross(15).addEmpty(2).addAcross(12).addEmpty(2);
  grid.solve();
}
}
