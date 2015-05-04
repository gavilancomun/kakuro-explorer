

package com.flowlikeariver.kakuro;


public class AcrossCell implements Cell {

@Override
public void draw() {
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

}
