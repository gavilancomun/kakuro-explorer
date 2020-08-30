
package gavilan.kakuro.cell;

public record DownAcrossCell(int down, int across) implements Cell, Across, Down {

@Override
public void accept(Visitor visitor) {
  visitor.visitDownAcross(this);
}

}
