grammar Scheme;


sequent : semisequent '==>' semisequent;
semisequent : | term (',' term)*;
term :
      term '->' term
    | term '&' term
    | term '|' term
    | term '=' term
    | term '!=' term
    | term '!=' term
//    | term ('-'|'+'|'*'|'/'|'%') term
    | '!' term
    | '(' term ')'
    | 'true' | 'false'
    | atom ( '(' term (',' term)*  ')' )?;


atom : ID | INT | FLOAT | SID schemeConstraint?;

schemeConstraint : ':' '(' term ')';
//?X:(.pos = ?Y.pos)

INT : '-'? [0-9]+;
FLOAT : '-'? [0-9]+'.'[0-9]+;
ID : [.a-zA-Z]+;
SID : '?'[.a-zA-Z]+ | '...';


WS:[ \r\n\f] -> channel(HIDDEN);

/* Examples
==> a -> b & ?X & f(x) & ?X(?Y)
f(?X) ==> g(?X)
?X, ?Y ==> ?X | ?Y
?F:(sort=INT)(a,b,c)==>
seqPerm(?X, ?Y), seqPerm(?Y, ?Z) ==> seqPerm(?X,?Z)
?X:(.sort = INT) ==>
?X:(.sort = INT) ==> ?Y:(.sort = ?X.sort)
?X:(.sort = INT) ==> ?Y:(equalSort(?X,?Y))
*/
