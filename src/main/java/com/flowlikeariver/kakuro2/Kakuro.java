package com.flowlikeariver.kakuro2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import java.util.stream.IntStream;

public class Kakuro {

public static ValueCell v() {
  return new ValueCell(IntStream.range(1, 10).mapToObj(i -> i).collect(toSet()));
}

public static ValueCell v(Set<Integer> values) {
  return new ValueCell(values);
}

public static EmptyCell e() {
  return new EmptyCell();
}

public static DownCell d(int d) {
  return new DownCell(d);
}

public static AcrossCell a(int a) {
  return new AcrossCell(a);
}

public static DownAcrossCell da(int d, int a) {
  return new DownAcrossCell(d, a);
}

public static String drawRow(List<Cell> row) {
  return row.stream()
          .map(v -> v.draw())
          .collect(joining()) + "\n";
}

public static String drawGrid(List<List<Cell>> grid) {
  return grid.stream()
          .map(row -> drawRow(row))
          .collect(joining());
}

public static boolean allDifferent(List<Integer> nums) {
  return nums.size() == new HashSet<>(nums).size();
}

public static <T> List<T> conj(List<T> items, T item) {
  List<T> result = new ArrayList<>(items);
  result.add(item);
  return result;
}

public static List<List<Integer>> permute(List<ValueCell> vs, int target, List<Integer> soFar) {
  if (target >= 1) {
    if (soFar.size() == (vs.size() - 1)) {
      List<List<Integer>> result = new ArrayList<>();
      result.add(conj(soFar, target));
      return result;
    }
    else {
      return vs.get(soFar.size()).values.stream()
              .map(n -> permute(vs, (target - n), conj(soFar, n)))
              .flatMap(perm -> perm.stream())
              .collect(toList());
    }
  }
  else {
    return Collections.EMPTY_LIST;
  }
}

public static List<List<Integer>> permuteAll(List<ValueCell> vs, int target) {
  return permute(vs, target, new ArrayList<>());
}

public static boolean isPossible(ValueCell v, int n) {
  return v.contains(n);
}

public static <T> List<List<T>> transpose(List<List<T>> m) {
  if (m.isEmpty()) {
    return Collections.EMPTY_LIST;
  }
  else {
    return IntStream.range(0, m.get(0).size())
            .mapToObj(i -> IntStream.range(0, m.size())
                    .mapToObj(j -> m.get(j).get(i))
                    .collect(toList()))
            .collect(toList());
  }
}

}
