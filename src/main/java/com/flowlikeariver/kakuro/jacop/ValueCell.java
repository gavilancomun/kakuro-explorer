package com.flowlikeariver.kakuro.jacop;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.jacop.core.IntDomain;
import org.jacop.core.IntVar;
import org.jacop.core.Store;

public class ValueCell implements Cell {

public IntVar logicVar;

public ValueCell(Store store, Collection<Integer> values) {
  this.logicVar = new IntVar(store);
  values.stream()
          .sorted()
          .forEach(i -> this.logicVar.addDom(i, i));
}

public static Stream<Integer> getValues(IntDomain dom) {
  return IntStream.rangeClosed(dom.min(), dom.max())
          .filter(dom::contains)
          .mapToObj(i -> i);
}

public static Stream<Integer> getValues(IntVar iv) {
  return getValues(iv.dom());
}

public static void printDomain(IntVar logicVariable) {
  StringBuilder s = new StringBuilder("domain [");
  IntDomain dom = logicVariable.dom();
  s.append(getValues(dom)
          .map(i -> "" + i)
          .collect(joining(" ")));
  System.out.println(s + "]");
}

@Override
public String draw() {
  if (1 == logicVar.dom().getSize()) {
    return "     " + getValues(logicVar).findFirst().get() + "    ";
  }
  else {
    return " " + IntStream.range(1, 10)
            .mapToObj(n -> getValues().contains(n) ? String.valueOf(n) : ".")
            .collect(joining());
  }
}

public boolean contains(int n) {
  return getValues(logicVar).anyMatch(i -> i == n);
}

@Override
public String toString() {
  return "ValueCell[" + getValues(logicVar)
          .map(String::valueOf)
          .collect(joining(", "))
          + "]";
}

@Override
public int hashCode() {
  int hash = 5;
  return hash;
}

@Override
public boolean equals(Object obj) {
  if (this == obj) {
    return true;
  }
  if (obj == null) {
    return false;
  }
  if (getClass() != obj.getClass()) {
    return false;
  }
  return Objects.equals(getValues(), ((ValueCell) obj).getValues());
}

public Set<Integer> getValues() {
  return getValues(logicVar).collect(toSet());
}

}
