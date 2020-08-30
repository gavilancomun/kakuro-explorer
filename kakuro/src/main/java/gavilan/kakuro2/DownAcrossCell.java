package gavilan.kakuro2;

public record DownAcrossCell(int down, int across) implements Down, Across, Cell {
@Override
public String draw() {
  return String.format("   %2d\\%2d  ", down, across);
}

}
