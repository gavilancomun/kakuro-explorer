package com.flowlikeariver.kakuro2;

public class AcrossCell implements Cell {

public int across;

public AcrossCell(int across) {
  this.across = across;
}

@Override
public String draw() {
  return String.format("   --\\%2d  ", across);
}

}
