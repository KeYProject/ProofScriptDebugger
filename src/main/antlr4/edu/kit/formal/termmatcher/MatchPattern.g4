grammar MatchPattern;
schemaTerm :
	   '_'
	 |'...'
	 | schemaVar
	 | logicalVar
	 | functionSymbol ('('schemaTerm (',' schemaTerm)? ')')?
	 ;

termHelper :
      schemaTerm
	 | functionSymbol '('termHelper (',' termHelper)? ')'
	 ;

functionSymbol :
    ID+
    ;

schemaVar:
    '?'ID+
    ;

logicalVar:
    ID+
    ;


DIGITS : DIGIT+ ;
fragment DIGIT : [0-9] ;
ID : [a-zA-Z] ([_a-zA-Z0-9] | '.' | '\\' | '[]' | '-')* ;