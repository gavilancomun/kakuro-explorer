package gavilan.kakuro2;

public class DownAcrossCell implements Down, Across, Cell {

public int across;
public int down;

public DownAcrossCell(int down, int across) {
  this.across = across;
  this.down = down;
}

@Override
public String draw() {
  return String.format("   %2d\\%2d  ", down, across);
}

@Override
public int getDown() {
  return down;
}

@Override
public int getAcross() {
  return across;
}

@Override
public String toString() {
  return "DownAcrossCell[" + down + ", " + across + "]";
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
  DownAcrossCell other = (DownAcrossCell) obj;
  return (this.across == other.across) && (this.down == other.down);
}

@Override
public int hashCode() {
  int hash = 5;
  hash = 79 * hash + this.across;
  hash = 79 * hash + this.down;
  return hash;
}

}
