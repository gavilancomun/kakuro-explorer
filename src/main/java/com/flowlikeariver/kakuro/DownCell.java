package com.flowlikeariver.kakuro;

public class DownCell implements Cell {

@Override
public void draw() {
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

}
