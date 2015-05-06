package com.flowlikeariver.kakuro.cell;

public class SolidCell implements Cell {

@Override
public void draw() {
  System.out.print("   =====  ");
}

@Override
public boolean isAcross() {
  return false;
}

@Override
public boolean isDown() {
  return false;
}

@Override
public boolean isEmpty() {
  return false;
}

}
