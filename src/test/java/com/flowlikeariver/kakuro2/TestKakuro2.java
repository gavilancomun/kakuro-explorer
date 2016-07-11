package com.flowlikeariver.kakuro2;

import static com.flowlikeariver.kakuro2.Kakuro.allDifferent;
import static com.flowlikeariver.kakuro2.Kakuro.transpose;
import static com.flowlikeariver.kakuro2.Kakuro.v;
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

}
