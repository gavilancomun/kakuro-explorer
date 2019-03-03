package gavilan.choco.kakuro;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;
import java.util.stream.IntStream;
import org.chocosolver.solver.variables.IntVar;

public class ValueCell implements Cell {

public IntVar logicVar;

public ValueCell(IntVar intVar) {
  logicVar = intVar;
}

private List<Integer> elements() {
  var results = new ArrayList<Integer>();
  var vit = logicVar.getValueIterator(true);
  while (vit.hasNext()) {
    int v = vit.next();
    results.add(v);
  }
  vit.dispose();
  return results;
}

private Set<Integer> getValues() {
  return elements().stream().collect(toSet());
}

@Override
public String draw() {
  if (logicVar.isInstantiated()) {
    return "     " + logicVar.getValue() + "    ";
  }
  else {
    return " " + IntStream.range(1, 10)
            .mapToObj(n -> contains(n) ? String.valueOf(n) : ".")
            .collect(joining());
  }
}

public boolean contains(int n) {
  return logicVar.contains(n);
}

@Override
public String toString() {
  return "ValueCell[" + elements()
          .stream()
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

}
