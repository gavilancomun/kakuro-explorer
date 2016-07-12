package com.flowlikeariver.kakuro2;

public class EmptyCell implements Cell {

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
  if (getClass() != obj.getClass()) {
    return false;
  }
  return true;
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
