package com.flowlikeariver.kakuro.cell;

public class DownCell implements Cell, Down {

int total;

public DownCell(int total) {
  this.total = total;
}

@Override
public String draw() {
  return String.format("   %2d\\--  ", total);
}

@Override
public int getDownTotal() {
  return total;
}

}
