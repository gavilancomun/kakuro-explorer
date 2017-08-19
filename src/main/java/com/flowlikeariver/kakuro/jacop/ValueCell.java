package com.flowlikeariver.kakuro.jacop;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;
import java.util.stream.IntStream;
import org.jacop.core.IntVar;

public class ValueCell implements Cell {

public IntVar logicVar;

public ValueCell(IntVar intVar, Collection<Integer> values) {
  logicVar = intVar;
  values.stream()
          .sorted()
          .forEach(i -> logicVar.addDom(i, i));
}

public ValueCell(IntVar intVar) {
  logicVar = intVar;
}

IntStream elements() {
  return Arrays.stream(logicVar.dom().toIntArray()); 
}

public Set<Integer> getValues() {
  return elements().mapToObj(i -> i).collect(toSet());
}

@Override
public String draw() {
  if (logicVar.singleton()) {
    return "     " + logicVar.value() + "    ";
  }
  else {
    return " " + IntStream.range(1, 10)
            .mapToObj(n -> contains(n) ? String.valueOf(n) : ".")
            .collect(joining());
  }
}

public boolean contains(int n) {
  return logicVar.dom().contains(n);
}

@Override
public String toString() {
  return "ValueCell[" + elements()
          .mapToObj(String::valueOf)
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

}
