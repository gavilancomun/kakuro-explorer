package gavilan.kakuro.cell;

public class DownCell implements Cell, Down {

int total;

public DownCell(int total) {
  this.total = total;
}

@Override
public int getDownTotal() {
  return total;
}

@Override
public void accept(Visitor visitor) {
  visitor.visitDown(this);
}

}
