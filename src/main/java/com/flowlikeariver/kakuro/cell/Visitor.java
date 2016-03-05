package com.flowlikeariver.kakuro.cell;

public interface Visitor {

void visitEmpty(EmptyCell cell);

void visitValue(ValueCell cell);

void visitDown(DownCell cell);

void visitAcross(AcrossCell cell);

void visitDownAcross(DownAcrossCell cell);

}
