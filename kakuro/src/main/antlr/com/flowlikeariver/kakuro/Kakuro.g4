grammar Kakuro;

@header {
  package com.flowlikeariver.kakuro;
}

grid:   header row+;

header: empty (down | empty)+ NEW_LINE;

row:    (across | empty) row_part+ NEW_LINE;

across: '-' '\\' NUMBER;

down:   NUMBER '\\' '-';

down_across:  NUMBER '\\' NUMBER;

row_part: across
        | down
        | down_across
        | value
        | empty
        ;

empty:  EMPTY;

value:  '.';



NEW_LINE:   '\r'? '\n';

EMPTY:      'X'+;

fragment 
DIGIT:      [0-9];

NUMBER:     DIGIT DIGIT?;

WHITE_SPACE:    [ \t]+ -> channel(HIDDEN);

LINE_COMMENT:   '//' .*? '\r'? '\n' -> skip;

COMMENT:        '/*' .*? '*/' -> skip;
