# Commands



Generated on: Tue Sep 12 15:08:55 CEST 2017by `gendoc.groovy`.

Covering the proof script commands of [KeY](http://key-project.org).

## assume
> Synopsis: `assume <TERM>`


The assume command is an unsound taclet rule and takes one argument:

The command adds the formula passed as argument to the antecedent
a formula #2 to which the command is applied


**Arguments:**

* `#2` :  *TERM* (*R*)null

## auto
> Synopsis: `auto [all=<BOOLEAN>] [steps=<INT>]`



**Arguments:**

* `all` :  *BOOLEAN* null
* `steps` :  *INT* null

## cut
> Synopsis: `cut <TERM>`



**Arguments:**

* `#2` :  *TERM* (*R*)null

## instantiate
> Synopsis: `instantiate formula=<TERM> var=<STRING> occ=<INT> with=<TERM>`






**Arguments:**

* `formula` :  *TERM* (*R*)null
* `var` :  *STRING* (*R*)null
* `occ` :  *INT* (*R*)null
* `with` :  *TERM* (*R*)null

## let
> Synopsis: `let`



**Arguments:**


## macro
> Synopsis: `macro <STRING>`



**Arguments:**

* `#2` :  *STRING* (*R*)null

## rule
> Synopsis: `rule <STRING> [on=<TERM>] [formula=<TERM>] [occ=<INT>]`



**Arguments:**

* `#2` :  *STRING* (*R*)null
* `on` :  *TERM* null
* `formula` :  *TERM* null
* `occ` :  *INT* null

## schemaVar
> Synopsis: `schemaVar <STRING> <STRING>`



**Arguments:**

* `#2` :  *STRING* (*R*)null
* `#3` :  *STRING* (*R*)null

## script
> Synopsis: `script <STRING>`



**Arguments:**

* `#2` :  *STRING* (*R*)null

## select
> Synopsis: `select formula=<TERM>`



**Arguments:**

* `formula` :  *TERM* (*R*)null

## set
> Synopsis: `set [oss=<BOOLEAN>]`



**Arguments:**

* `oss` :  *BOOLEAN* null

## skip
> Synopsis: `skip`



**Arguments:**


## smt
> Synopsis: `smt solver=<STRING>`



**Arguments:**

* `solver` :  *STRING* (*R*)null

## tryclose
> Synopsis: `tryclose steps=<INTEGER> <STRING>`



**Arguments:**

* `steps` :  *INTEGER* (*R*)null
* `#2` :  *STRING* (*R*)null

