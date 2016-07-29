package com.flowlikeariver.kakuro2;

import static com.flowlikeariver.kakuro2.Kakuro.a;
import static com.flowlikeariver.kakuro2.Kakuro.allDifferent;
import static com.flowlikeariver.kakuro2.Kakuro.concatLists;
import static com.flowlikeariver.kakuro2.Kakuro.d;
import static com.flowlikeariver.kakuro2.Kakuro.da;
import static com.flowlikeariver.kakuro2.Kakuro.drawRow;
import static com.flowlikeariver.kakuro2.Kakuro.e;
import static com.flowlikeariver.kakuro2.Kakuro.gatherValues;
import static com.flowlikeariver.kakuro2.Kakuro.solvePair;
import static com.flowlikeariver.kakuro2.Kakuro.solver;
import static com.flowlikeariver.kakuro2.Kakuro.takeWhile;
import static com.flowlikeariver.kakuro2.Kakuro.transpose;
import static com.flowlikeariver.kakuro2.Kakuro.v;
import java.util.Arrays;
import static java.util.Arrays.asList;
import java.util.List;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestKakuro2 {

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
  List<Cell> line = asList(da(3, 4), v(), v(1, 2), d(4), e(), a(5), v(4), v(1));
  String result = drawRow(line);
  System.out.println(result);
  assertEquals("    3\\ 4   123456789 12.......    4\\--     -----     --\\ 5       4         1    \n", result);
}

@Test
public void testPermute() {
  List<ValueCell> vs = asList(v(), v(), v());
  List<List<Integer>> results = Kakuro.permuteAll(vs, 6);
  System.out.println(results);
  assertEquals(10, results.size());
  List<List<Integer>> diff = results.stream()
          .filter(Kakuro::allDifferent)
          .collect(toList());
  assertEquals(6, diff.size());
}

@Test
public void testTranspose() {
  List<List<Integer>> ints = IntStream.range(0, 3)
          .mapToObj(i -> IntStream.range(0, 4)
                  .boxed()
                  .collect(toList()))
          .collect(toList());
  List<List<Integer>> tr = transpose(ints);
  System.out.println(ints);
  System.out.println(tr);
  assertEquals(ints.size(), tr.get(0).size());
  assertEquals(ints.get(0).size(), tr.size());
}

@Test
public void testTakeWhile() {
  List<Integer> result = takeWhile(n -> n < 4, IntStream.range(0, 10).mapToObj(i -> i).collect(toList()));
  System.out.println(result);
  assertEquals(4, result.size());
}

@Test
public void testConcat() {
  List<Integer> a = asList(1, 2, 3);
  List<Integer> b = asList(4, 5, 6, 1, 2, 3);
  List<Integer> result = concatLists(a, b);
  System.out.println(result);
  assertEquals(9, result.size());
}

@Test
public void testDrop() {
  List<Integer> a = asList(1, 2, 3, 4, 5, 6);
  List<Integer> result = Kakuro.drop(4, a);
  System.out.println(result);
  assertEquals(2, result.size());
}

@Test
public void testTake() {
  List<Integer> a = asList(1, 2, 3, 4, 5, 6);
  List<Integer> result = Kakuro.take(4, a);
  System.out.println(result);
  assertEquals(4, result.size());
}

@Test
public void testPartBy() {
  List<Integer> data = asList(1, 2, 2, 2, 3, 4, 5, 5, 6, 7, 7, 8, 9);
  List<List<Integer>> result = Kakuro.partitionBy(n -> 0 == (n % 2), data);
  System.out.println(result);
  assertEquals(9, result.size());
}

@Test
public void testPartAll() {
  List<Integer> data = Arrays.asList(1, 2, 2, 2, 3, 4, 5, 5, 6, 7, 7, 8, 9);
  List<List<Integer>> result = Kakuro.partitionAll(5, 3, data);
  System.out.println(result);
  assertEquals(5, result.size());
}

@Test
public void testPartN() {
  List<Integer> data = Arrays.asList(1, 2, 2, 2, 3, 4, 5, 5, 6, 7, 7, 8, 9);
  List<List<Integer>> result = Kakuro.partitionN(5, data);
  System.out.println(result);
  assertEquals(3, result.size());
}

@Test
public void testSolveStep() {
  List<ValueCell> result = Kakuro.solveStep(Arrays.asList(v(1, 2), v()), 5);
  System.out.println("solve step " + result);
  assertEquals(v(1, 2), result.get(0));
  assertEquals(v(3, 4), result.get(1));
}

@Test
public void testGatherValues() {
  List<Cell> line = Arrays.asList(da(3, 4), v(), v(), d(4), e(), a(4), v(), v());
  List<List<Cell>> result = gatherValues(line);
  System.out.println("gather " + result);
  assertEquals(4, result.size());
  assertEquals(da(3, 4), result.get(0).get(0));
  assertEquals(d(4), result.get(2).get(0));
  assertEquals(e(), result.get(2).get(1));
  assertEquals(a(4), result.get(2).get(2));
}

@Test
public void testPairTargets() {
  List<Cell> line = asList(da(3, 4), v(), v(), d(4), e(), a(4), v(), v());
  List<SimplePair<List<Cell>>> result = Kakuro.pairTargetsWithValues(line);
  System.out.println("pair " + result);
  assertEquals(2, result.size());
  assertEquals(da(3, 4), result.get(0).left.get(0));
  assertEquals(d(4), result.get(1).left.get(0));
  assertEquals(e(), result.get(1).left.get(1));
  assertEquals(a(4), result.get(1).left.get(2));
}

@Test
public void testSolvePair() {
  List<Cell> line = asList(da(3, 4), v(), v(), d(4), e(), a(4), v(), v());
  List<SimplePair<List<Cell>>> pairs = Kakuro.pairTargetsWithValues(line);
  SimplePair<List<Cell>> pair = pairs.get(0);
  List<Cell> result = solvePair(cell -> ((Down) cell).getDown(), pair);
  System.out.println("solvePair " + result);
  assertEquals(3, result.size());
  assertEquals(v(1, 2), result.get(1));
  assertEquals(v(1, 2), result.get(2));
}

@Test
public void testSolveLine() {
  List<Cell> line = asList(da(3, 4), v(), v(), d(4), e(), a(5), v(), v());
  List<Cell> result = Kakuro.solveLine(line, x -> ((Across) x).getAcross());
  System.out.println("solve line " + result);
  assertEquals(8, result.size());
  assertEquals(v(1, 3), result.get(1));
  assertEquals(v(1, 3), result.get(2));
  assertEquals(v(1, 2, 3, 4), result.get(6));
  assertEquals(v(1, 2, 3, 4), result.get(7));
}

@Test
public void testSolveRow() {
  List<Cell> result = Kakuro.solveRow(asList(a(3), v(1, 2, 3), v(1)));
  System.out.println("solve row " + result);
  assertEquals(v(2), result.get(1));
  assertEquals(v(1), result.get(2));
}

@Test
public void testSolveCol() {
  List<Cell> result = Kakuro.solveColumn(asList(da(3, 12), v(1, 2, 3), v(1)));
  System.out.println("solve col " + result);
  assertEquals(v(2), result.get(1));
  assertEquals(v(1), result.get(2));
}

@Test
public void testSolver() {
  List<List<Cell>> grid1 = asList(
          asList(e(), d(4), d(22), e(), d(16), d(3)),
          asList(a(3), v(), v(), da(16, 6), v(), v()),
          asList(a(18), v(), v(), v(), v(), v()),
          asList(e(), da(17, 23), v(), v(), v(), d(14)),
          asList(a(9), v(), v(), a(6), v(), v()),
          asList(a(15), v(), v(), a(12), v(), v()));
  List<List<Cell>> result = solver(grid1);
  assertEquals("   --\\ 3       1         2       16\\ 6       4         2    \n", drawRow(result.get(1)));
  assertEquals("   --\\18       3         5         7         2         1    \n", drawRow(result.get(2)));
  assertEquals("   -----     17\\23       8         9         6       14\\--  \n", drawRow(result.get(3)));
  assertEquals("   --\\ 9       8         1       --\\ 6       1         5    \n", drawRow(result.get(4)));
  assertEquals("   --\\15       9         6       --\\12       3         9    \n", drawRow(result.get(5)));
}

}
