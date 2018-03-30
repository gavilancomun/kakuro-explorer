package gavilan.kakuro.cell;

public class AcrossCell implements Cell, Across {

int total;

public AcrossCell(int total) {
  this.total = total;
}

@Override
public int getAcrossTotal() {
  return total;
}

@Override
public void accept(Visitor visitor) {
  visitor.visitAcross(this);
}

}
