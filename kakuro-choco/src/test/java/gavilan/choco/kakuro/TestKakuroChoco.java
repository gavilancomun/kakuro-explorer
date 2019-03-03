package gavilan.choco.kakuro;

import static gavilan.choco.kakuro.Kakuro.a;
import static gavilan.choco.kakuro.Kakuro.d;
import static gavilan.choco.kakuro.Kakuro.da;
import static gavilan.choco.kakuro.Kakuro.drawRow;
import static gavilan.choco.kakuro.Kakuro.e;
import static gavilan.choco.kakuro.Kakuro.solver;
import static gavilan.choco.kakuro.Kakuro.v;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestKakuroChoco {

@Test
public void testDrawEmpty() {
  String result = e().draw();
  assertEquals("   -----  ", result);
}

@Test
public void testDrawAcross() {
  String result = a(5).draw();
  assertEquals("   --\\ 5  ", result);
}

@Test
public void testDrawDown() {
  String result = d(4).draw();
  assertEquals("    4\\--  ", result);
}

@Test
public void testDrawDownAcross() {
  String result = da(3, 4).draw();
  assertEquals("    3\\ 4  ", result);
}

@Test
public void testDrawValues() {
  String result = v().draw();
  assertEquals(" 123456789", result);
  String result12 = v(1, 2).draw();
  assertEquals(" 12.......", result12);
}

@Test
public void testDrawRow() {
  var line = asList(da(3, 4), v(), v(1, 2), d(4), e(), a(5), v(4), v(1));
  String result = drawRow(line);
  assertEquals("    3\\ 4   123456789 12.......    4\\--     -----     --\\ 5       4         1    \n", result);
}

@Test
public void testSolver() {
  var grid1 = asList(
    asList(e(), d(4), d(22), e(), d(16), d(3)),
    asList(a(3), v(), v(), da(16, 6), v(), v()),
    asList(a(18), v(), v(), v(), v(), v()),
    asList(e(), da(17, 23), v(), v(), v(), d(14)),
    asList(a(9), v(), v(), a(6), v(), v()),
    asList(a(15), v(), v(), a(12), v(), v()));
  var result = solver(grid1);
  assertEquals("   --\\ 3       1         2       16\\ 6       4         2    \n", drawRow(result.get(1)));
  assertEquals("   --\\18       3         5         7         2         1    \n", drawRow(result.get(2)));
  assertEquals("   -----     17\\23       8         9         6       14\\--  \n", drawRow(result.get(3)));
  assertEquals("   --\\ 9       8         1       --\\ 6       1         5    \n", drawRow(result.get(4)));
  assertEquals("   --\\15       9         6       --\\12       3         9    \n", drawRow(result.get(5)));
}

}
