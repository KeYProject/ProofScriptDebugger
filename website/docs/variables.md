# Variables 

The proof script language supports script variables. 
These are variables that are managed by the interpreter and are not part of the explicit proof 
object&mdash;in contrast to the variables of the 
Java program and the logical variables of first oder logic formulas.

As Java or logic variables, we bind the assignments script variables to a sequences.
If a script variable is not assigned in the current selected goal, 
we look up its value in the sequence's ancestors, recursively. 

## Types 

Each variables has a type. Following *simple* types are defined: 

* `int`  -- arbitrary precision integers
* `bool` -- true or false
* `string` --  string (`""`)

For the interoperability with KeY we allow that parameterized term types `TERM<S>`, 
where `S` is a KeY sort (already defined by in the KeY namespace).
The type hierarchy is fixed.

* `TERM` -- for an unknown sort
* `TERM<S>` -- with sort `S` (e.g. int, locset)

## Special Variables

During the design of our scripting language we arrived at a point, 
in which we need access to configuration options of the underlying 
theorem prover. We decided to implement an access to these options via variables. 
The variable access has advantages over built-in commands. The main advantage is that 
you can do conditions other these options.
 
Here we give a list of the current special variables 

### Interpreter options

* `__MAX_ITERATIONS_REPEAT : int`

    Sets the the upper limit for iterations in repeat loops. Default value is really high. 

* `__STRICT_MODE : bool` 

    Defines if the interpreter is in strict or relax mode. 
    
    In strict mode the interpreter throws an exception in following cases:
    
    * access to undeclared or unassigned variable
    * application of non-applicable rule
    
    In non-strict mode these errors are ignored&mdash;a warning is given on the console. 
    
### KeY configuration

* `__KEY_AUTO_MAX_STEPS : int`

    Should be a positive number and is the limit for rule application in automatic proof searchs.
    
* `__KEY_MATH`