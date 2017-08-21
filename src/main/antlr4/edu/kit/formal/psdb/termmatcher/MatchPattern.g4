grammar MatchPattern;

/* Examples for testing

f(x)
f(x,y,g(y))
X
?Y
_
...
f(... ?X ...)
f(..., ?X)
f(..., ...?X...)
f(..., ... g(x) ...)
f(_, x, _, y, ... y ...)
*/

sequentPattern : antec=semiSeqPattern? ARROW succ=semiSeqPattern? | anywhere=semiSeqPattern;

semiSeqPattern : termPattern (',' termPattern)*;

termPattern :
      termPattern MUL termPattern                               #mult
    | <assoc=right> termPattern op=(DIV|MOD) termPattern        #divMod
    | termPattern op=(PLUS|MINUS) termPattern                   #plusMinus
    | termPattern op=(LE|GE|LEQ|GEQ) termPattern                #comparison
    | termPattern op=(NEQ|EQ) termPattern                       #equality
    | termPattern AND termPattern                               #and
    | termPattern OR termPattern                                #or
    | termPattern IMP termPattern                               #impl
    | termPattern XOR termPattern                               #xor
    //|   termPattern EQUIV termPattern already covered by EQ/NEQ
    | MINUS termPattern                                         #exprNegate
    | NOT termPattern                                           #exprNot
    | '(' termPattern ')'                                       #exprParen
    | func=ID ( '(' termPattern (',' termPattern)* ')')?        #function
    |  DONTCARE                                                 #dontCare
    //| STARDONTCARE  #starDontCare
    | SID                                                       #schemaVar
    | STARDONTCARE termPattern STARDONTCARE                     #anywhere
    | DIGITS                                                    #number
    // not working because of ambigue | left=termPattern op=(PLUS|MINUS|MUL|LE|GE|LEQ|GEQ|NEQ|EQ| AND|OR|IMP) right=termPattern #binaryOperation
    ;

/*
f(x), f(x,y,g(y)), X, ?Y, _, ..., f(... ?X ...), f(..., ?X), f(..., ...?X...), f(..., ... g(x) ...), f(_, x, _, y, ... y ...)
*/


ARROW : '⇒' | '==>';
DONTCARE: '?' | '_' | '█';
STARDONTCARE: '...' | '…';
PLUS : '+' ;
MINUS : '-' ;
MUL : '*' ;
DIV : '/' ;
EQ : '=' ;
NEQ : '!=' ;
GEQ : '>=' ;
LEQ : '<=' ;
GE : '>' ;
LE : '<' ;
AND : '&' ;
OR: '|' ;
IMP: '->';
MOD:'%';
XOR:'^';
FORALL: '\forall' | '∀';
EXISTS: '\exists';

DIGITS : DIGIT+ ;
fragment DIGIT : [0-9] ;
SID: '?' [_a-zA-Z0-9\\]+ ;
ID : [a-zA-Z\\_] ([_a-zA-Z0-9\\])*;

COMMENT: '//' ~[\n\r]* -> channel(HIDDEN);
WS: [\n\f\r\t ] -> channel(HIDDEN);
