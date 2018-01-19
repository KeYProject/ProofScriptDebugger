<style>
        #content {
            width: 60em;
            margin: auto;
            border: 1px #ccc solid;
            border-bottom-left-radius: 2em;
            border-top-right-radius: 2em;
            background: ghostwhite;
            padding: 2em;
        }

        .column>div{
            float: left;
            width: 30%;
            margin:1%;
            text-align: left;
        }

        .column img {
            width: 150px;
            text-align: center;
        }
        
        img.thumb {
            width:100%;           
            border-radius:150px;
        }
       
       .column h3 {
       
       }
       
       div.feature-caption {
            text-align: block;           
           text-align: justify;
           font-size: 80%;
       }
       
</style>

# Proof Script Debugger for the KeY System

The proof script debugger is a prototypical implementation
of an interaction concept for program verification systems that are rule based and
use a program logic.
The prototype is implemented on top of the interactive program verification system
[KeY](http://www.key-project.org). KeY is an interactive program verification
system for Java program annotated with the Java Modeling Language (JML).


The protypical implementation includes a proof scripting language that is tailored to the
problem domain of program verification.
The main features of the language are:

1. integration of domain specific entities like goal, formula, term and rule as
first-class citizens into the language;</li>
1. an expressive proof goal selection mechanism
  * to identify and select individual proof branches,
  * to easily switch between proof branches,
  * to select multiple branches for uniform treatment (multi-matching);
  that is resilient to small changes in the proof
1. a repetition construct which allows repeated application of proof strategies;</li>
1. support for proof exploration within the language.</li>


Together with the proof scripting language a debugging concept for failed proof attempts
is implemented that leverages well-known concepts from program debugging to
the analysis of failed proof attempts.

## Publications

A full description of the language and debugging-concept 
is published at [HVC 2017](http://rdcu.be/E4fF)


## Features

<div class="column">
    <div>
        <h3>Inspection of different parts of the proof state</h3>
        <p> 
        The different parts of the proof state can be inspected:
        <ul>
        <li>list of open goals</li>
        <li>sequent of selected open goal</li>
        <li>path through program (if existing) for selected open goal</li>
        </ul>
        </p> 
    </div>
    <div >
        <h3>Adjustable view on list of open goals</h3>
        <img class="thumb" src="img/thumb_ScreenshotGoalList.png" />
        <div class="feature-caption"/></div>
    </div>
    <div >
        <h3>Explore the proof tree of KeY</h3>
        <img class="thumb" src="img/thumb_ScreenshotProofTree.png"/>
        <div class="feature-caption"/></div>
    </div>
</div>
<div style="clear: both;"/>

<div class="column">
    <div>
        <h3>Set a breakpoint and run execution to breakpoint</h3>
        <img src="img/thumb_ScreenshotBreakpoint.png" class="thumb"/>
        <div class="feature-caption">
            Mark lines with an (conditional) breakpoint to pause the script execution.
        </div>
    </div>
    <div> 
        <h3>Stepwise evaluation for time travellers</h3>
        <img src="img/thumb_ScreenshotStep.png" class="thumb" />
        <div class="feature-caption">
            Stepwise script execution: step over and into.
            Our special offers for time travellers: Go backwards in time
            and then Back to the Future,again!
        </div>
    </div>
    <div> 
      <h3> Interactive Rule Application</h3>
        <img src="img/thumb_ScreenshotInteractive.png" class="thumb" />
        <div class="feature-caption">
        Select rules for interactive application.
        </div>
  </div>
</div>

<div style="clear: both;"/> 

<h2>Getting Started</h2>

<h2>Downloads</h2>

<ul>
 <li>PSDBG - <strong>Version 1.0-FM</strong> 
    <a href="~/releases/psdbg-1.0-fm.jar">psdbg-1.0-fm.jar</a>
    <br>
    Special Version for the tool paper at Formal Methods 2018. 
 </li>
</ul>

<div style="clear: both;"/>

