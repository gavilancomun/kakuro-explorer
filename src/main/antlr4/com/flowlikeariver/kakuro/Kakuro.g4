grammar Kakuro;

grid:   header row+;

header: solid (down | solid)+ NEW_LINE;

row:    (across | solid) row_part+ NEW_LINE;

across: '-' '\\' NUMBER;

down:   NUMBER '\\' '-';

down_across:  NUMBER '\\' NUMBER;

row_part: across
        | down
        | down_across
        | empty
        | solid
        ;

solid:  SOLID;

empty:  '.';



NEW_LINE:   '\r'? '\n';

SOLID:      'X'+;

fragment 
DIGIT:      [0-9];

NUMBER:     DIGIT DIGIT?;

WHITE_SPACE:    [ \t]+ -> channel(HIDDEN);

LINE_COMMENT:   '//' .*? '\r'? '\n' -> skip;

COMMENT:        '/*' .*? '*/' -> skip;

