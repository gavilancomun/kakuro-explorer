package gavilan.kakuro;

import gavilan.kakuro.cell.ValueCell;
import gavilan.kakuro.cell.Cell;
import gavilan.kakuro.cell.DownAcrossCell;
import gavilan.kakuro.cell.DownCell;
import gavilan.kakuro.cell.AcrossCell;
import gavilan.kakuro.cell.Drawer;
import gavilan.kakuro.cell.EmptyCell;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RowDef {

List<Cell> cells = new ArrayList<>();

public RowDef() {
}

public int size() {
  return cells.size();
}

public String draw() {
  return cells.stream().map(Drawer::draw).collect(joining()) + "\n";
}

public RowDef addEmpty() {
  cells.add(new EmptyCell());
  return this;
}

public RowDef addValue(int n) {
  cells.addAll(IntStream.rangeClosed(1, n)
          .mapToObj(i -> new ValueCell())
          .collect(toList()));
  return this;
}

public RowDef addDown(int n) {
  cells.add(new DownCell(n));
  return this;
}

public RowDef addAcross(int n) {
  cells.add(new AcrossCell(n));
  return this;
}

public RowDef addDownAcross(int down, int across) {
  cells.add(new DownAcrossCell(down, across));
  return this;
}

public Optional<Cell> get(int i) {
  return (i >= cells.size()) ? Optional.empty() : Optional.ofNullable(cells.get(i));
}

public Stream<Cell> stream() {
  return cells.stream();
}
}
