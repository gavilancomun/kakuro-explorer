package com.flowlikeariver.kakuro2;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;
import java.util.stream.IntStream;

public class Kakuro {

public static Cell v() {
  return new ValueCell(IntStream.range(1, 10).mapToObj(i -> i).collect(toSet()));
}

public static Cell v(Set<Integer> values) {
  return new ValueCell(values);
}

public static Cell e() {
  return new EmptyCell();
}

public static Cell d(int d) {
  return new DownCell(d);
}

public static Cell a(int a) {
  return new AcrossCell(a);
}

public static Cell da(int d, int a) {
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

//function permute(vs, target, soFar)
//  if (target >= 1) then
//    if #soFar == (#vs - 1) then
//      return {conj(soFar, target)}
//    else
//      local step1 = vs[#soFar + 1]
//      local step2 = step1.values
//      local step3 = map(function (n)
//                          return permute(vs, (target - n), conj(soFar, n))
//                        end, step2)
//      return flatten1(step3)
//    end
//  else
//    return {}
//  end
//end

}
