package gavilan.kakuro2;

public record Pair<T, U>(T left, U right) {

@Override
public String toString() {
  return getClass().getName().replaceAll(".*\\.", "") + "[left=" + left.toString() + ", right=" + right.toString() + "]";
}

}
