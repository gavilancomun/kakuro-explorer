package com.flowlikeariver.kakuro;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestJava {

@Test
public void testSet() {
  Set<Integer> t = new HashSet<>(Arrays.asList(1, 2, 3, 1, 2, 3));
  assertEquals(3, t.size());
}

@Test
public void testLinked() {
  LinkedList<Integer> list = new LinkedList<>();
  list.push(1);
  list.push(2);
  list.push(3);
  assertEquals(1, (int) list.get(2));
  assertEquals(2, (int) list.get(1));
  assertEquals(3, (int) list.get(0));
}
}
