package gavilan.kakuro.cell;

public record AcrossCell(int across) implements Cell, Across {

@Override
public void accept(Visitor visitor) {
  visitor.visitAcross(this);
}

}
