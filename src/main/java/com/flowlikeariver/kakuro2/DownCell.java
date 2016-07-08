package com.flowlikeariver.kakuro2;

public class DownCell implements Cell {

public int down;

public DownCell(int down) {
  this.down = down;
}

@Override
public String draw() {
  return String.format("   %2d\\--  ", down);
}

}
