level(dedent,-1).
level(indent,1).
level(X,0).
level(Prev, colon, [indent,T],0).
level(colon,1).
level(Prev, Cur, Next, X) :- level(Cur,X).

onclearline(dedent,1).
onclearline(script, 2).
onclearline(cases, 1).
onclearline(foreach, 1).
onclearline(repeat, 2).
onclearline(comment, 1).
onclearline(Prev, Cur, Next, X) :- onclearline(Cur,X).

nlafter(Prev, colon, [H | T], 1) :- H \= indent .
nlafter(indent,1).
nlafter(dedent,1).
nlafter(semicolon,1).
nlafter(Prev, Cur, Next, X) :- nlafter(Cur,X).

wsbefore(Prev, Cur, Next, X) :- wsafter(Cur,X).
wsbefore(id,1).


wsafter(Prev, id, [semicolon | T], 0).
wsafter(id,1).
wsafter(cases,1).
wsafter(case,1).
wsafter(default,1).
wsafter(Prev, Cur, Next, X) :- wsafter(Cur,X).
