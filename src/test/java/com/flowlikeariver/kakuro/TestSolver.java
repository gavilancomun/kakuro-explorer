package com.flowlikeariver.kakuro;

import org.junit.Test;

public class TestSolver {

@Test
public void test1() {
  GridController grid = new GridController();
  grid.newRowDef();
  grid.addSolid();
  grid.addDown(4);
  grid.addDown(22);
  grid.addSolid();
  grid.addDown(16);
  grid.addDown(3);
  grid.newRowDef();
  grid.addAcross(3);
  grid.addEmpty(2);
  grid.addDownAcross(16, 6);
  grid.addEmpty(2);
  grid.newRowDef();
  grid.addAcross(18);
  grid.addEmpty(5);
  grid.newRowDef();
  grid.addSolid();
  grid.addDownAcross(17, 23);
  grid.addEmpty(3);
  grid.addDown(14);
  grid.newRowDef();
  grid.addAcross(9);
  grid.addEmpty(2);
  grid.addAcross(6);
  grid.addEmpty(2);
  grid.newRowDef();
  grid.addAcross(15);
  grid.addEmpty(2);
  grid.addAcross(12);
  grid.addEmpty(2);
  System.out.println("Size " + grid.width() + " x " + grid.height());
  grid.solve();
}
}
