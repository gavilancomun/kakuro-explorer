package com.flowlikeariver.kakuro;

public class AcrossCell implements Cell, Across {

int total;

public AcrossCell(int total) {
  this.total = total;
}

@Override
public void draw() {
  System.out.printf("    -\\%2d   ", total);
}

@Override
public boolean isAcross() {
  return true;
}

@Override
public boolean isDown() {
  return false;
}

@Override
public boolean isEmpty() {
  return false;
}

public int getAcrossTotal() {
  return total;
}

}
