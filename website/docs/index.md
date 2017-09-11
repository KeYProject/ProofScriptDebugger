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
            width: 33%;
            text-align: center;
        }

        .column img {
            width: 150px;
            text-align: center;
        }
</style>

<h1>Proof Script Debugger for the KeY System</h1>

<p>The proof script debugger is a prototypical implementation
of an interaction concept for program verification systems that are rule based and
use a program logic.
The prototype is implemented on top of the interactive program verification system
<a href="http://www.key-project.org">KeY</a>. KeY is an interactive program verification
system for Java program annotated with the Java Modeling Language (JML).
</p>

<p>
The protypical implementation includes a proof scripting language that is tailored to the
problem domain of program verification.
The main features of the language are:
<ol>
<li> integration of domain specific entities like goal, formula, term and rule as
first-class citizens into the language;</li>
<li> an expressive proof goal selection mechanism
<ul>
    <li>to identify and select individual proof branches,</li>
    <li>to easily switch between proof branches,</li>
    <li>to select multiple branches for uniform treatment (multi-matching);</li>
</ul>
that is resilient to small changes in the proof</li>
<li> a repetition construct which allows repeated application of proof strategies;</li>
<li> support for proof exploration within the language.</li>


</ol>
Together with the proof scripting language a debugging concept for failed proof attempts
is implemented that leverages well-known concepts from program debugging to
the analysis of failed proof attempts.

</p>
<h2>Publications</h2>
A full description of the language and debugging-concept is published at <a href="">HVC 2017</a>.

<h2>Features</h2>

<div class="column">
    <div >
        <img src="https://placeimg.com/150/150/any" />
        <h3>Feature 1</h3>
        <p>

        </p>
    </div>

    <div >
        <img src="https://placeimg.com/150/150/any" />
        <h3>Feature 2</h3>
    </div>

    <div >
        <img src="https://placeimg.com/150/150/any" />
        <h3>Feature 3</h3>
    </div>

</div>
<div style="clear: both;"/>
    <div class="column">
        <div >
            <img src="https://placeimg.com/150/150/any" />
            <h3>Feature 1</h3>
            <p>

            </p>
        </div>

        <div >
            <img src="https://placeimg.com/150/150/any" />
            <h3>Feature 2</h3>
        </div>

        <div >
            <img src="https://placeimg.com/150/150/any" />
            <h3>Feature 3</h3>
        </div>

    </div>
    <div style="clear: both;"/>


        <!--    <h2>Getting Started</h2>


            <h2>Downloads</h2>

            <ul>
            <li>Version 1.0
            <p>
            <a href="#">psdb-0.9-alpha.jar</a>
            </p>
            </li>
            </ul>
        -->
        <footer style="border-top: #ccc 1px solid">
            Contact: <a href="https://formal.iti.kit.edu/~grebing/">Sarah Grebing</a>
        </footer>
        </p>
