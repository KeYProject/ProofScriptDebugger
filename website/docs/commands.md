# Commands



Generated on: Mon Sep 11 17:22:08 CEST 2017by `gendoc.groovy`.

Covering the proof script commands of [KeY](http://key-project.org).

## assume
> Synopsis: `assume <TERM>`


The assume command is an unsound taclet rule and takes one argument:

The command adds the formula passed as argument to the antecedent
a formula #2 to which the command is applied


**Arguments:**

* `#2` :  *TERM* (*R*)

## auto
> Synopsis: `auto [all=<BOOLEAN>] [steps=<INT>]`


The AutoCommand invokes the automatic strategy "Auto"


**Arguments:**

* `all` :  *BOOLEAN* 
* `steps` :  *INT* 

## cut
> Synopsis: `cut <TERM>`


* The command object CutCommand has as scriptcommand name "cut"
* As parameters:
* a formula with the id "#2"


**Arguments:**

* `#2` :  *TERM* (*R*)

## exit
> Synopsis: `exit`





**Arguments:**


## focus
> Synopsis: `focus <SEQUENT>`



**Arguments:**

* `#2` :  *SEQUENT* (*R*)

## instantiate
> Synopsis: `instantiate [formula=<TERM>] [var=<STRING>] [occ=<INT>] [<STRING>] [with=<TERM>]`


instantiate var=a occ=2 with="a_8" hide
<p>
instantiate formula="\forall int a; phi(a)" with="a_8"



**Arguments:**

* `formula` :  *TERM* 
* `var` :  *STRING* 
* `occ` :  *INT* 
* `#2` :  *STRING* 
* `with` :  *TERM* 

## javascript
> Synopsis: `javascript <STRING>`



**Arguments:**

* `#2` :  *STRING* (*R*)

## leave
> Synopsis: `leave`





**Arguments:**


## let
> Synopsis: `let`





**Arguments:**


## macro
> Synopsis: `macro <STRING>`



**Arguments:**

* `#2` :  *STRING* (*R*)

## rule
> Synopsis: `rule <STRING> [on=<TERM>] [formula=<TERM>] [occ=<INT>]`


* Command that applies a calculus rule
* All parameters are passed as strings and converted by the command.
* The parameters are:
* <ol>
*     <li>#2 = <String>rule name</String></li>
*     <li>on= key.core.logic.Term on which the rule should be applied to as String (find part of the rule) </li>
*     <li>formula= toplevel formula in which term appears in</li>
*     <li>occ = occurrence number</li>
*     <li>inst_= instantiation</li>
* </ol>



**Arguments:**

* `#2` :  *STRING* (*R*)
* `on` :  *TERM* 
* `formula` :  *TERM* 
* `occ` :  *INT* 

## schemaVar
> Synopsis: `schemaVar <STRING> <STRING>`



**Arguments:**

* `#2` :  *STRING* (*R*)
* `#3` :  *STRING* (*R*)

## script
> Synopsis: `script <STRING>`



**Arguments:**

* `#2` :  *STRING* (*R*)

## select
> Synopsis: `select formula=<TERM>`



**Arguments:**

* `formula` :  *TERM* (*R*)

## set
> Synopsis: `set [oss=<BOOLEAN>]`



**Arguments:**

* `oss` :  *BOOLEAN* 

## skip
> Synopsis: `skip`



**Arguments:**


## smt
> Synopsis: `smt solver=<STRING>`



**Arguments:**

* `solver` :  *STRING* (*R*)

## tryclose
> Synopsis: `tryclose [steps=<INTEGER>] [<STRING>]`



**Arguments:**

* `steps` :  *INTEGER* 
* `#2` :  *STRING* 

