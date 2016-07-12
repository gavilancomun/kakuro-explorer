package com.flowlikeariver.kakuro2;

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

}
