package gavilan.kakuro2;

public record AcrossCell(int across) implements Across, Cell {

@Override
public String draw() {
  return String.format("   --\\%2d  ", across);
}

}
