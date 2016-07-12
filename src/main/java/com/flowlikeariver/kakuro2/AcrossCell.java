package com.flowlikeariver.kakuro2;

import static java.util.stream.Collectors.joining;

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

}
