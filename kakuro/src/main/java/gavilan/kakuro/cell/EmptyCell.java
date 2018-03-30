package gavilan.kakuro.cell;

public class EmptyCell implements Cell {

@Override
public void accept(Visitor visitor) {
  visitor.visitEmpty(this);
}

}
