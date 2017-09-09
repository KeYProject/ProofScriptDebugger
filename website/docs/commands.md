
# Commands
    
Generated on: Sat Sep 09 23:02:01 CEST 2017 by `gendoc.groovy`. 

Covering the macros of [KeY](http://key-project.org).
# macro

> Synopsis: `c.getName()) <STRING>`

**Arguments:**

* `#2` :  *STRING* (*R*)


# auto

> Synopsis: `c.getName()) [all=<BOOLEAN>] [steps=<INT>]`

**Arguments:**

* `all` :  *BOOLEAN* 
* `steps` :  *INT* 


# cut

> Synopsis: `c.getName()) <TERM>`

**Arguments:**

* `#2` :  *TERM* (*R*)


# set

> Synopsis: `c.getName()) [oss=<BOOLEAN>]`

**Arguments:**

* `oss` :  *BOOLEAN* 


# select

> Synopsis: `c.getName()) formula=<TERM>`

**Arguments:**

* `formula` :  *TERM* (*R*)


# schemaVar

> Synopsis: `c.getName()) <STRING> <STRING>`

**Arguments:**

* `#2` :  *STRING* (*R*)
* `#3` :  *STRING* (*R*)


# focus

> Synopsis: `c.getName()) <SEQUENT>`

**Arguments:**

* `#2` :  *SEQUENT* (*R*)


# rule

> Synopsis: `c.getName()) <STRING> [on=<TERM>] [formula=<TERM>] [occ=<INT>]`

**Arguments:**

* `#2` :  *STRING* (*R*)
* `on` :  *TERM* 
* `formula` :  *TERM* 
* `occ` :  *INT* 


# skip

> Synopsis: `c.getName())`

**Arguments:**



# instantiate

> Synopsis: `c.getName()) formula=<TERM> var=<STRING> occ=<INT> with=<TERM>`

**Arguments:**

* `formula` :  *TERM* (*R*)
* `var` :  *STRING* (*R*)
* `occ` :  *INT* (*R*)
* `with` :  *TERM* (*R*)


# script

> Synopsis: `c.getName()) <STRING>`

**Arguments:**

* `#2` :  *STRING* (*R*)


# javascript

> Synopsis: `c.getName()) <STRING>`

**Arguments:**

* `#2` :  *STRING* (*R*)


# exit

> Synopsis: `c.getName())`

**Arguments:**



# tryclose

> Synopsis: `c.getName()) steps=<INTEGER> <STRING>`

**Arguments:**

* `steps` :  *INTEGER* (*R*)
* `#2` :  *STRING* (*R*)


# leave

> Synopsis: `c.getName())`

**Arguments:**



# let

> Synopsis: `c.getName())`

**Arguments:**



# smt

> Synopsis: `c.getName()) solver=<STRING>`

**Arguments:**

* `solver` :  *STRING* (*R*)


# assume

> Synopsis: `c.getName()) <TERM>`

**Arguments:**

* `#2` :  *TERM* (*R*)

