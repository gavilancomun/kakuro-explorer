package com.flowlikeariver.kakuro;

public class CreateModelListener extends KakuroBaseListener {

private final GridController gc = new GridController();

public GridController getGridController() {
  return gc;
}

@Override
public void enterHeader(KakuroParser.HeaderContext ctx) {
  gc.createRow();
}

@Override
public void enterRow(KakuroParser.RowContext ctx) {
  gc.createRow();
}

@Override
public void enterDown_across(KakuroParser.Down_acrossContext ctx) {
  int down = Integer.parseInt(ctx.NUMBER(0).getText());
  int across = Integer.parseInt(ctx.NUMBER(1).getText());
  gc.addDownAcross(down, across);
}

@Override
public void enterAcross(KakuroParser.AcrossContext ctx) {
  int across = Integer.parseInt(ctx.NUMBER().getText());
  gc.addAcross(across);
}

@Override
public void enterDown(KakuroParser.DownContext ctx) {
  int down = Integer.parseInt(ctx.NUMBER().getText());
  gc.addDown(down);
}

@Override
public void enterSolid(KakuroParser.SolidContext ctx) {
  gc.addSolid();
}

@Override
public void enterEmpty(KakuroParser.EmptyContext ctx) {
  gc.addEmpty(1);
}

}
