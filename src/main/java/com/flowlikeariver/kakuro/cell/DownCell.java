package com.flowlikeariver.kakuro.cell;

public class DownCell implements Cell, Down {

int total;

public DownCell(int total) {
  this.total = total;
}

@Override
public void draw() {
  System.out.printf("   %2d\\--  ", total);
}

@Override
public boolean isAcross() {
  return false;
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
public int getDownTotal() {
  return total;
}

}
