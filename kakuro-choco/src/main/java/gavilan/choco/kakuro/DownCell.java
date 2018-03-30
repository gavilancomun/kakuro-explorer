package gavilan.choco.kakuro;

public class DownCell implements Down, Cell {

public int down;

public DownCell(int down) {
  this.down = down;
}

@Override
public String draw() {
  return String.format("   %2d\\--  ", down);
}

@Override
public int getDown() {
  return down;
}

@Override
public String toString() {
  return "DownCell[" + down + "]";
}

@Override
public int hashCode() {
  int hash = 5;
  hash = 41 * hash + this.down;
  return hash;
}

@Override
public boolean equals(Object obj) {
  if (this == obj) {
    return true;
  }
  if (obj == null) {
    return false;
  }
  if (getClass() != obj.getClass()) {
    return false;
  }
  final DownCell other = (DownCell) obj;
  return this.down == other.down;
}

}
