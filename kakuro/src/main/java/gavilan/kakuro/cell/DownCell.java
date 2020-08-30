package gavilan.kakuro.cell;

public record DownCell(int down) implements Cell, Down {

@Override
public void accept(Visitor visitor) {
  visitor.visitDown(this);
}

}
