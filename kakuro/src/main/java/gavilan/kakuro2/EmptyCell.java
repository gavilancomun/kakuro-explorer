package gavilan.kakuro2;

public record EmptyCell() implements Cell {

@Override
public String draw() {
  return "   -----  ";
}

}
