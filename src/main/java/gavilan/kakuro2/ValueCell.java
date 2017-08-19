package gavilan.kakuro2;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import static java.util.stream.Collectors.joining;
import java.util.stream.IntStream;

public class ValueCell implements Cell {

public Set<Integer> values = new HashSet<>();

public ValueCell(Collection<Integer> values) {
  this.values.addAll(values);
}

@Override
public String draw() {
  if (1 == values.size()) {
    return "     " + values.stream().findFirst().get() + "    ";
  }
  else {
    return " " + IntStream.range(1, 10)
            .mapToObj(n -> values.contains(n) ? String.valueOf(n) : ".")
            .collect(joining());
  }
}

public boolean contains(int n) {
  return values.contains(n);
}

@Override
public String toString() {
  return "ValueCell[" + values.stream()
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
  return Objects.equals(this.values, ((ValueCell) obj).values);
}

public Set<Integer> getValues() {
  return values;
}

}
