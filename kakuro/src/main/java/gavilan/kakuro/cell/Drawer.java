package gavilan.kakuro.cell;

import static java.util.stream.Collectors.joining;
import java.util.stream.IntStream;

public class Drawer implements Visitor {

private String result;

public static String draw(Cell cell) {
  var drawer = new Drawer();
  cell.accept(drawer);
  return drawer.result;
}

@Override
public void visitEmpty(EmptyCell cell) {
  result = "   -----  ";
}

@Override
public void visitValue(ValueCell cell) {
  if (1 == cell.getValues().size()) {
    result = cell.getValues().stream()
      .map(i -> "     " + i + "    ")
      .collect(joining());
  }
  else {
    result = " "
      + IntStream.rangeClosed(1, 9)
      .mapToObj(i -> cell.isPossible(i) ? String.valueOf(i) : ".")
      .collect(joining());
  }
}

@Override
public void visitDown(DownCell cell) {
  result = String.format("   %2d\\--  ", cell.down());
}

@Override
public void visitAcross(AcrossCell cell) {
  result = String.format("   --\\%-2d  ", cell.across());
}

@Override
public void visitDownAcross(DownAcrossCell cell) {
  result = String.format("   %2d\\%-2d  ", cell.down(), cell.across());
}

}
