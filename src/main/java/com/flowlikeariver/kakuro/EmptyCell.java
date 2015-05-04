

package com.flowlikeariver.kakuro;


public class EmptyCell implements Cell {

@Override
public void draw() {
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
  return true;
}

}
