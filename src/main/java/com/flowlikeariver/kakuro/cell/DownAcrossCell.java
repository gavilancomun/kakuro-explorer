
package com.flowlikeariver.kakuro.cell;


public class DownAcrossCell implements Cell, Across, Down {

int down;
int across;

public DownAcrossCell(int down, int across) {
  this.down = down;
  this.across = across;
}

@Override
public int getAcrossTotal() {
  return across;
}

@Override
public int getDownTotal() {
  return down;
}

@Override
public void accept(Visitor visitor) {
  visitor.visitDownAcross(this);
}

}
