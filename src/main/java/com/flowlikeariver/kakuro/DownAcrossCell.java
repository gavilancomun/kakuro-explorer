
package com.flowlikeariver.kakuro;


public class DownAcrossCell implements Cell, Across, Down {

int down;
int across;

public DownAcrossCell(int down, int across) {
  this.down = down;
  this.across = across;
}

@Override
public void draw() {
  System.out.printf("   %2d\\%2d   ", down, across);
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

public int getAcrossTotal() {
  return across;
}

public int getDownTotal() {
  return down;
}

}
