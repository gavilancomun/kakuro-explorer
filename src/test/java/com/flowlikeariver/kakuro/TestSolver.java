package com.flowlikeariver.kakuro;

import java.io.StringReader;
import org.junit.Ignore;
import org.junit.Test;

public class TestSolver {

@Test
public void testAPI() {
  GridController grid = new GridController();
  grid.createRow().addSolid().addDown(4).addDown(22).addSolid().addDown(16).addDown(3);
  grid.createRow().addAcross(3).addEmpty(2).addDownAcross(16, 6).addEmpty(2);
  grid.createRow().addAcross(18).addEmpty(5);
  grid.createRow().addSolid().addDownAcross(17, 23).addEmpty(3).addDown(14);
  grid.createRow().addAcross(9).addEmpty(2).addAcross(6).addEmpty(2);
  grid.createRow().addAcross(15).addEmpty(2).addAcross(12).addEmpty(2);
  grid.solve();
}

@Ignore
@Test
public void testParse() {
  String k = "XXXXX  4\\-   22\\-  XXXXX  16\\-  3\\-\n" +
             "-\\3   .      .      16\\6  .      .\n" +
             "-\\18  .      .      .      .      .\n" +
             "XXXXX  17\\23 .      .      .      14\\-\n" +
             "-\\ 9  .      .      -\\6   .      .\n" +
             "-\\15  .      .      -\\12  .      .\n";
  GridController gc = Interpreter.interpret(new StringReader(k));
  gc.solve();
}

@Test
public void testWikipediaExample() {
  String k = "XXXXXX 23\\- 30\\-  XXXXXX XXXXXX 27\\- 12\\- 16\\- \n" +
             "-\\16  .     .      XXXXXX 17\\24 .     .     .     \n" +
             "-\\17  .     .      15\\29 .      .     .     .     \n" +
             "-\\35  .     .      .      .      .     12\\- XXXXXX\n" +
             "XXXXXX -\\7  .      .      7\\8   .     .     7\\-  \n" +
             "XXXXXX 11\\- 10\\16 .      .      .     .     .     \n" +
             "-\\21  .     .      .      .      -\\5  .     .     \n" +
             "-\\6   .     .      .      XXXXXX -\\3  .     .     \n";
  GridController gc = Interpreter.interpret(new StringReader(k));
  gc.solve();
}
}
