
package com.flowlikeariver.kakuro;


public class DownAcrossCell implements Cell {

@Override
public void draw() {
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

}
