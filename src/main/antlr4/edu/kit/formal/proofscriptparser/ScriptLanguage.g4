grammar ScriptLanguage;

start
    :   (SCRIPT name=ID '(' paramters=argList? ')' INDENT body=stmtList DEDENT)*
    ;

argList
    :   varDecl (',' varDecl)+
    ;

varDecl
    :   ID ':' type
    ;

type
    :   INT
    |   BOOL
    |   TERMTYPE
    ;

stmtList
    :   statement*
    ;

statement
    :   assignment
    |   repeatStmt
    |   casesStmt
    |   forEachStmt
    |   theOnlyStmt
    |   scriptCommand
    |   callStmt
    ;

assignment
    :   variable=ID (COLON type)? ASSIGN expression SEMICOLON
    ;

expression
    :
        MINUS expression   #exprMinus
    |   NEGATE expression  #exprNegate
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
    :   MATCH ( TERM_LITERAL | ID)
        (USING LBRACKET argList RBRACKET)?
    ;

scriptVar
    :   '?' ID
    ;

repeatStmt
    :   REPEAT INDENT stmtList DEDENT
    ;

casesStmt
    :   CASES INDENT casesList+ DEDENT
    ;

casesList
    :   CASE expression COLON INDENT stmtList DEDENT casesList*
    |   DEFAULT  COLON INDENT stmtList DEDENT
    ;

forEachStmt
    :   FOREACH INDENT stmtList DEDENT
    ;

theOnlyStmt
    :   THEONLY INDENT stmtList DEDENT
    ;

scriptCommand
    :   ID (ID '=' expression)* SEMICOLON
    ;

callStmt
    :   CALL scriptCommand SEMICOLON
    ;


//LEXER Rules
WS : [ \t\n\r]+ -> skip ;
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
INT : 'int' ;
BOOL: 'bool' ;
TERMTYPE : 'term' ;
FOREACH : 'foreach' ;
THEONLY : 'theonly' ;
ID : [a-zA-Z] [_a-zA-Z0-9]* ;
DIGITS : DIGIT+ ;
fragment DIGIT : [0-9] ;
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
IMP : '->' ;
EQUIV : '<=>' ;
NEGATE: 'not';

//options {...}
//import ... ;

//tokens {...}
//channels {...} // lexer only
//@actionName {...}

/*
//Script ::= 'script' ID (ARGLIST)? NEWLINE INDENT Cmd DEDENT | Cmd

Cmd ::= //Cmd ';' Cmd |
        VarAssign |
        //'repeat' NEWLINE INDENT Cmd+ DEDENT |
        'cases' NEWLINE DEDENT Case+ ('default:' NEWLINE INDENT Cmd DEDENT)? DEDENT |
        'foreach' NEWLINE INDENT Cmd+ DEDENT |
        'theonly'NEWLINE INDENT cmd DEDENT |
       // Cmd 'on' (ShemaTerm | ShemaVar)+ ('with' (ShemaVar| ShemaTerm))? |
       'call' scriptCommand;

Case ::= 'case' BoolExpr ':' NEWLINE INDENT Cmd+ DEDENT

VarAssign ::= ID ':' TYPE  |
              ID ':=' (AExpr| BExpr | ///PosExpr) //PosExp evtl. das matchpattern für Sequents?

//MatchPattern ::= 'match' ('Seq'? ShemaSeq) |
//                ('matchLabel'| 'matchRule') RegExpr

//                'match' ( `~[`]` | '~[\']' | ID)
*/



/*arithExpr
    :   ID
    |   DIGITS
    |   arithExpr arithOp arithExpr
    ;

arithOp
    :   '+' | '-' | '*' | '/'
    ;

boolExpr
    :   TRUE
    |   FALSE
    |   matchPattern
    |   'not' boolExpr
    |   boolExpr boolOp boolExpr
    |   arithExpr relOp arithExpr
    ;

boolOp
    :   'and' | 'or'
    ;

relOp
    :   '<' | '<=' | '=' | '>' | '>='
    ;
*/