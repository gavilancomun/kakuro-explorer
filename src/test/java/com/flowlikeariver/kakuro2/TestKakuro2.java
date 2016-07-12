package com.flowlikeariver.kakuro2;

import static com.flowlikeariver.kakuro2.Kakuro.allDifferent;
import static com.flowlikeariver.kakuro2.Kakuro.concatLists;
import static com.flowlikeariver.kakuro2.Kakuro.takeWhile;
import static com.flowlikeariver.kakuro2.Kakuro.transpose;
import static com.flowlikeariver.kakuro2.Kakuro.v;
import java.util.Arrays;
import java.util.List;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestKakuro2 {

@Test
public void testPermute() {
  List<ValueCell> vs = IntStream.range(0, 3).mapToObj(i -> v()).collect(toList());
  List<List<Integer>> results = Kakuro.permuteAll(vs, 6);
  System.out.println(results);
  assertEquals(10, results.size());
  List<List<Integer>> diff = results.stream()
    .filter(p -> allDifferent(p))
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
  List<Integer> a = Arrays.asList(1, 2, 3);
  List<Integer> b = Arrays.asList(4, 5, 6, 1, 2, 3);
  List<Integer> result = concatLists(a, b);
  System.out.println(result);
  assertEquals(9, result.size());
}

@Test
public void testDrop() {
  List<Integer> a = Arrays.asList(1, 2, 3, 4, 5, 6);
  List<Integer> result = Kakuro.drop(4, a);
  System.out.println(result);
  assertEquals(2, result.size());
}

@Test
public void testTake() {
  List<Integer> a = Arrays.asList(1, 2, 3, 4, 5, 6);
  List<Integer> result = Kakuro.take(4, a);
  System.out.println(result);
  assertEquals(4, result.size());
}

@Test
public void testPartBy() {
  List<Integer> data = Arrays.asList(1, 2, 2, 2, 3, 4, 5, 5, 6, 7, 7, 8, 9);
  List<List<Integer>> result = Kakuro.partitionBy(n -> 0 == (n % 2), data);
  System.out.println(result);
  assertEquals(9, result.size());
}

@Test
public void testPartN() {
  List<Integer> data = Arrays.asList(1, 2, 2, 2, 3, 4, 5, 5, 6, 7, 7, 8, 9);
  List<List<Integer>> result = Kakuro.partitionN(5, data);
  System.out.println(result);
  assertEquals(3, result.size());
}

@Test
public void testPartAll() {
  List<Integer> data = Arrays.asList(1, 2, 2, 2, 3, 4, 5, 5, 6, 7, 7, 8, 9);
  List<List<Integer>> result = Kakuro.partitionAll(5, 3, data);
  System.out.println(result);
  assertEquals(5, result.size());
}

}
