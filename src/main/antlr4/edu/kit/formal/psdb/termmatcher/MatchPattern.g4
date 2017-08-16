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

termPattern :
	   DONTCARE      #dontCare
	 //| STARDONTCARE  #starDontCare
	 | SID           #schemaVar
	 | STARDONTCARE termPattern STARDONTCARE #anywhere
	 | func=ID ( '(' termPattern (',' termPattern)* ')')? #function
	 ;
/*
f(x), f(x,y,g(y)), X, ?Y, _, ..., f(... ?X ...), f(..., ?X), f(..., ...?X...), f(..., ... g(x) ...), f(_, x, _, y, ... y ...)
*/
semiSeqPattern : termPattern (',' termPattern)*;
sequentPattern : semiSeqPattern? ARROW semiSeqPattern?;

ARROW : '⇒' | '==>';
DONTCARE: '?' | '_' | '█';
STARDONTCARE: '...' | '…';
DIGITS : DIGIT+ ;
fragment DIGIT : [0-9] ;
SID: '?' [_a-zA-Z0-9\\]+ ;
ID : [a-zA-Z\\_] ([_a-zA-Z0-9\\])* ;
COMMENT: '//' ~[\n\r]* -> channel(HIDDEN);
WS: [\n\f\r\t ] -> channel(HIDDEN);