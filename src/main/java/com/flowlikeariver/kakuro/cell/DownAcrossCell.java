
package com.flowlikeariver.kakuro.cell;


public class DownAcrossCell implements Cell, Across, Down {

int down;
int across;

public DownAcrossCell(int down, int across) {
  this.down = down;
  this.across = across;
}

@Override
public String draw() {
  return String.format("   %2d\\%-2d  ", down, across);
}

@Override
public boolean isAcross() {
  return true;
}

@Override
public boolean isDown() {
  return true;
}

@Override
public boolean isEmpty() {
  return false;
}

@Override
public int getAcrossTotal() {
  return across;
}

@Override
public int getDownTotal() {
  return down;
}

}
