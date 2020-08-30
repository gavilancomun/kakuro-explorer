package gavilan.kakuro2;

public record DownCell(int down) implements Down, Cell {

@Override
public String draw() {
  return String.format("   %2d\\--  ", down);
}

}
