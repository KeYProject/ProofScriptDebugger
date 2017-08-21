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
	   DONTCARE      #dontCare
	 //| STARDONTCARE  #starDontCare
	 | SID           #schemaVar
	 | STARDONTCARE termPattern STARDONTCARE #anywhere
	 | DIGITS #number
	 | func=ID ( '(' termPattern (',' termPattern)* ')')? #function
	 | left=termPattern op=(PLUS|MINUS|MUL|LE|GE|LEQ|GEQ|NEQ|EQ| AND|OR|IMP) right=termPattern #binaryOperation
	 ;

	 /*
	       expression MUL expression #exprMultiplication
         | <assoc=right> expression DIV expression #exprDivision
         | expression op=(PLUS|MINUS) expression #exprLineOperators
         | expression op=(LE|GE|LEQ|GEQ) expression #exprComparison
         | expression op=(NEQ|EQ) expression  #exprEquality
         | expression AND expression   #exprAnd
         | expression OR expression    #exprOr
         | expression IMP expression   #exprIMP
         //|   expression EQUIV expression already covered by EQ/NEQ
         | expression LBRACKET substExpressionList RBRACKET #exprSubst
         | MINUS expression   #exprNegate
         | NOT expression  #exprNot
         | '(' expression ')' #exprParen
	 */
/*
f(x), f(x,y,g(y)), X, ?Y, _, ..., f(... ?X ...), f(..., ?X), f(..., ...?X...), f(..., ... g(x) ...), f(_, x, _, y, ... y ...)
*/


ARROW : '⇒' | '==>';
DONTCARE: '?' | '_' | '█';
STARDONTCARE: '...' | '…';
DIGITS : DIGIT+ ;
fragment DIGIT : [0-9] ;
SID: '?' [_a-zA-Z0-9\\]+ ;
ID : [a-zA-Z\\_] ([_a-zA-Z0-9\\])*;


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

COMMENT: '//' ~[\n\r]* -> channel(HIDDEN);
WS: [\n\f\r\t ] -> channel(HIDDEN);