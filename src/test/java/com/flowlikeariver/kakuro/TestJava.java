package com.flowlikeariver.kakuro;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestJava {

@Test
public void testSet() {
  Set<Integer> t = new HashSet<>(Arrays.asList(1, 2, 3, 1, 2, 3));
  assertEquals(3, t.size());
}
}
