/** Optional javadoc style comment */
grammar ScriptLanguage;


start
    :   (SCRIPT ID '(' argList? ')' INDENT stmtList DEDENT)*
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
    :   statement+
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
    :   ID (':' type)? ':=' expression SEMICOLON
    ;

expression
    :   matchPattern
    |   '-' expression
    |   'not' expression
    |   expression MUL expression
    |   <assoc=right> expression DIV expression
    |   expression (PLUS|MINUS) expression
    |   expression LE expression
    |   expression GE expression
    |   expression LEQ expression
    |   expression GEQ expression
    |   expression EQ expression
    |   expression NEQ expression
    |   expression AND expression
    |   expression OR expression
    |   expression IMP expression
    |   expression EQUIV expression
    |   scriptVar
    |   ID
    |   DIGITS
    |   '(' expression ')'
    |   TERM
    |   STRINGLIT
    |   TRUE
    |   FALSE
    ;

matchPattern
    :   'match' TERM 'using' '[' ID COLON type (',' ID COLON type)* ']'
    ;

scriptVar
    :   '?' ID
    ;

repeatStmt
    :   REPEAT INDENT stmtList DEDENT
    ;

casesStmt
    :   'cases' INDENT casesList+ DEDENT
    ;

casesList
    :   'case' expression COLON INDENT stmtList DEDENT casesList*
    |   'default' COLON INDENT stmtList DEDENT
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
STRINGLIT : '"' ~["]* '"' ;
TERM : '`' ~[`]* '`' ;
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