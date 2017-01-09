package com.flowlikeariver.kakuro.jacop;

public class EmptyCell implements Cell {

@Override
public String draw() {
  return "   -----  ";
}

@Override
public boolean equals(Object obj) {
  if (this == obj) {
    return true;
  }
  if (obj == null) {
    return false;
  }
  return getClass() == obj.getClass();
}

@Override
public int hashCode() {
  int hash = 7;
  return hash;
}

@Override
public String toString() {
  return "EmptyCell";
}
}
