package com.flowlikeariver.kakuro2;

public class AcrossCell implements Across, Cell {

public int across;

public AcrossCell(int across) {
  this.across = across;
}

@Override
public String draw() {
  return String.format("   --\\%2d  ", across);
}

@Override
public int getAcross() {
  return across;
}

@Override
public String toString() {
  return "AcrossCell[" + across + "]";
}

@Override
public int hashCode() {
  int hash = 7;
  hash = 47 * hash + this.across;
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
  final AcrossCell other = (AcrossCell) obj;
  return this.across == other.across;
}

}
