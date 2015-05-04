
package com.flowlikeariver.kakuro;


public class RowDef {

private final int pos;

public RowDef(int pos) {
  this.pos = pos;
}

public int size() {
  return -1;
}

public void draw() {
  
}

public void addSolid() {
  
}

public void addEmpty(int n) {
}

public void addDown(int n) {
}

public void addAcross(int n) {
}

public void addDownAcross(int down, int across) {
}
}
