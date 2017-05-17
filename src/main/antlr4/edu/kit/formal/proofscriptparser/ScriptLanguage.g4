grammar ScriptLanguage;
//TODO There is still sth. missing:import statements at the beginning to allow scripts from other files

/*start
    : stmtList
    ;
*/
start
    :   (SCRIPT name=ID '(' signature=argList? ')' INDENT body=stmtList DEDENT)*
    ;

argList
    :   varDecl (',' varDecl)*
    ;

varDecl
    :   name=ID ':' type=ID
    ;

stmtList
    :   statement*
    ;

statement
    :   //scriptDecl
       varDecl
    |   assignment
    |   repeatStmt
    |   casesStmt
    |   forEachStmt
    |   theOnlyStmt
    |   scriptCommand
  //  |   callStmt
    ;

/*scriptDecl
    :
    SCRIPT name=ID '(' signature=argList? ')' INDENT body=stmtList DEDENT
    ;
*/
assignment
    :   variable=ID (COLON type=ID)? ASSIGN expression SEMICOLON
    ;

expression
    :
        MINUS expression   #exprNegate
    |   NOT expression  #exprNot
    |   expression MUL expression #exprMultiplication
    |   <assoc=right> expression DIV expression #exprDivision
    |   expression op=(PLUS|MINUS) expression #exprLineOperators
    |   expression op=(LE|GE|LEQ|GEQ) expression #exprComparison
    |   expression op=(NEQ|EQ) expression  #exprEquality
    |   expression AND expression   #exprAnd
    |   expression OR expression    #exprOr
    |   expression IMP expression   #exprIMP
    //|   expression EQUIV expression already covered by EQ/NEQ
    |   '(' expression ')' #exprParen
    | literals             #exprLiterals
    | matchPattern         #exprMatch
;

literals :
        ID             #literalID
    |   DIGITS         #literalDigits
    |   TERM_LITERAL   #literalTerm
    |   STRING_LITERAL #literalString
    |   TRUE           #literalTrue
    |   FALSE          #literalFalse
;

/**
 * Example: <pre>
    match `f(x) ==>` using [x:term]

     </pre>*/
matchPattern
    :   MATCH matchPattern=expression
        (USING LBRACKET argList RBRACKET)?
    ;

scriptVar
    :   '?' ID
    ;

repeatStmt
    :   REPEAT INDENT stmtList DEDENT
    ;

casesStmt
    :   CASES INDENT
            casesList*
        (DEFAULT  COLON? INDENT
            defList=stmtList
          DEDENT)?
        DEDENT
    ;

casesList
    :   CASE expression COLON? INDENT stmtList DEDENT
    ;

forEachStmt
    :   FOREACH INDENT stmtList DEDENT
    ;

theOnlyStmt
    :   THEONLY INDENT stmtList DEDENT
    ;

scriptCommand
    :   cmd=ID parameters? SEMICOLON
    ;

parameters: parameter+;
parameter :  ((pname=ID '=')? expr=expression);

/*
callStmt
    :   CALL scriptCommand SEMICOLON
    ;
*/

//LEXER Rules
WS : [ \t\n\r]+ -> skip ;

//comments, allowing nesting.
SINGLE_LINE_COMMENT : '//' ~[\r\n]* -> skip;
MULTI_LINE_COMMENT  : '/*' (MULTI_LINE_COMMENT|.)*? '*/' -> skip;

CASES: 'cases';
CASE: 'case';
DEFAULT: 'default';
ASSIGN : ':=';
LBRACKET: '[';
RBRACKET:']';
USING : 'using';
MATCH : 'match';
SCRIPT : 'script' ;
TRUE : 'true' ;
FALSE : 'false' ;
CALL : 'call' ;
REPEAT : 'repeat' ;
/*INT : 'int' ;
BOOL: 'bool' ;
TERMTYPE : 'term' ;*/
FOREACH : 'foreach' ;
THEONLY : 'theonly' ;
INDENT : '{' ;
DEDENT : '}' ;
SEMICOLON : ';' ;
COLON : ':' ;

STRING_LITERAL
   : '\'' ('\'\'' | ~ ('\''))* '\''
   ;

TERM_LITERAL
   : '`' ~('`')* '`'
   ;

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
IMP : '==>' ;
EQUIV : '<=>' ;
NOT: 'not';

DIGITS : DIGIT+ ;
fragment DIGIT : [0-9] ;
ID : [a-zA-Z] [_a-zA-Z0-9]* ;