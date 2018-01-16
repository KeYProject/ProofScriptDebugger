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

sequentPattern : antec=semiSeqPattern? ARROW succ=semiSeqPattern? #sequentArrow
               | anywhere=semiSeqPattern #sequentAnywhere
               ;

semiSeqPattern : termPattern (',' termPattern)*;

termPattern :
   '(' quantifier=(EXISTS|FORALL) boundVars+=(SID|ID)+  skope=termPattern ')'  #quantForm
    |  termPattern MUL termPattern                              #mult
    | <assoc=right> termPattern op=(DIV|MOD) termPattern        #divMod
    | termPattern op=(PLUS|MINUS) termPattern                   #plusMinus
    | termPattern op=(LE|GE|LEQ|GEQ) termPattern                #comparison
    | termPattern op=(NEQ|EQ) termPattern                       #equality
    | termPattern AND termPattern                               #and
    | termPattern OR termPattern                                #or
    | termPattern IMP termPattern                               #impl
    | termPattern XOR termPattern                               #xor
    | termPattern EQUIV termPattern                             #equivalence
    | MINUS termPattern                                         #exprNegate
    | NOT termPattern                                           #exprNot
    | '(' termPattern ')' bindClause?                                      #exprParen
    | func=ID ( '(' termPattern (',' termPattern)* ')')? bindClause?        #function
    |  DONTCARE   bindClause?                                              #dontCare
    //| STARDONTCARE  #starDontCare
    | SID                                                       #schemaVar
    | STARDONTCARE termPattern STARDONTCARE                     #anywhere
    | DIGITS                                                    #number
    // not working because of ambigue | left=termPattern op=(PLUS|MINUS|MUL|LE|GE|LEQ|GEQ|NEQ|EQ| AND|OR|IMP) right=termPattern #binaryOperation
    ;

/*
f(x), f(x,y,g(y)), X, ?Y, _, ..., f(... ?X ...), f(..., ?X), f(..., ...?X...), f(..., ... g(x) ...), f(_, x, _, y, ... y ...)
*/

bindClause : ('\\as' | ':') SID;

DONTCARE: '?' | '_' | '█';
DIGITS : DIGIT+ ;
fragment DIGIT : [0-9] ;

ARROW : '⇒' | '==>';
STARDONTCARE: '...' | '…';
PLUS : '+' ;
MINUS : '-' ;
MUL : '*' ;
DIV : '/' ;
EQ : '=' ;
NEQ : '!=' ;
GEQ : '>=' ;
LEQ : '<=' ;
EQUIV : '<->';
GE : '>' ;
LE : '<' ;
AND : '&' ;
OR: '|' ;
IMP: '->';
MOD:'%';
XOR:'^';
NOT :'!';
FORALL: '\\forall' | '∀';
EXISTS: '\\exists';
SID: '?' [_a-zA-Z0-9\\]+ ;
ID : [a-zA-Z\\_] ([_a-zA-Z0-9\\])*
   | 'update-application'
   | 'parallel-upd'
   | 'elem-update'
   ;


COMMENT: '//' ~[\n\r]* -> channel(HIDDEN);
WS: [\n\f\r\t ] -> channel(HIDDEN);
