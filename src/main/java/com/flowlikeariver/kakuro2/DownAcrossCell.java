package com.flowlikeariver.kakuro2;

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

}
