import de.uka.ilkd.key.macros.scripts.meta.ProofScriptArgument
import de.uka.ilkd.key.api.KeYApi
import de.uka.ilkd.key.macros.ProofMacro
import de.uka.ilkd.key.control.KeYEnvironment
import de.uka.ilkd.key.macros.scripts.ProofScriptCommand
import de.uka.ilkd.key.rule.Taclet
import org.key_project.util.collection.ImmutableList

propertiesFile = new File("rt-key/src/main/resources/edu/kit/iti/formal/psdbg/taclets.properties.xml")
dummyFile = new File("rt-key/src/test/resources/edu/kit/iti/formal/psdbg/interpreter/contraposition/contraposition.key")
websiteDoc = new File("website/doc/")

ImmutableList<Taclet> getTaclets() {
    println("Use dummy file: ${dummyFile}")
    KeYEnvironment env = KeYApi.loadFromKeyFile(dummyFile).getLoadedProof().getEnv()
    return env.initConfig.taclets
}

def writeProperties(File file, List<Taclet> taclets) {
    documentation = new Properties()
    for (Taclet taclet in taclets) {
        println(taclet.displayName())
        documentation.put(taclet.displayName(), taclet.toString())
    }
    // write properties file!
    propertiesFile.getParentFile().mkdirs()
    stream = new FileOutputStream(file)
    documentation.storeToXML(stream,
            "Generated on: ${new Date()}. Use extractDocumentation.groovy".toString(),
            "utf-8")
    stream.close()
}

def writeTacletDocumentation(File file, taclets) {
    file.parentFile.mkdirs()
    stream = new FileWriter(file)
    stream.write("""
# Taclets
    
Generated on: ${new Date()} by `gendoc.groovy`. 

Covering the *default* taclets of [KeY](http://key-project.org).""")

    for (t in taclets) {
        stream.write("\n\n## ${t.displayName()}\n\n")
        stream.write("```\n" + t.toString() + "\n```")
    }

    stream.close()
}

def writeMacros(File file) {
    macros = de.uka.ilkd.key.api.KeYApi.getMacroApi().getMacros()

    file.parentFile.mkdirs()
    stream = new FileWriter(file)
    stream.write("""
# Macros
    
Generated on: ${new Date()} by `gendoc.groovy`. 

Covering the macros of [KeY](http://key-project.org).""")

    macros.sort()

    for (t in macros) {
        stream.write("\n\n## ${t.name} (`${t.scriptCommandName}`) \n\n")
        stream.write("${t.category}\n\n")
        stream.write("${t.description}\n\n")
    }
    stream.close()
}


def helpForCommand(ProofScriptCommand c) {
    html = new StringBuilder()
    html.append("""
# ${c.getName()}

> Synopsis: `c.getName())""")

    for (a in c.getArguments()) {
        html.append(' ')
        if (a.isFlag()) {
            html.append("[").append(a.getName()).append("]")
        } else {
            if (!a.isRequired())
                html.append("[")

            if (a.getName().startsWith("#"))
                html.append("<${a.getType().getSimpleName().toUpperCase()}>")
            else
                html.append("${a.name}=<${a.getType().getSimpleName().toUpperCase()}>")

            if (!a.isRequired())
                html.append("]")
        }
    }
    html.append("`\n\n")

    html.append("**Arguments:**\n")
    for (a in c.getArguments()) {
        html.append("\n* `${a.getName()}` :  *${a.getType().getSimpleName().toUpperCase()}* ")
        if (a.isRequired()) {
            html.append("(*R*)")
        }
    }
    return html.toString()
}


def writeCommand(File file) {
    commands = de.uka.ilkd.key.api.KeYApi.getScriptCommandApi().getScriptCommands()

    file.parentFile.mkdirs()
    stream = new FileWriter(file)
    stream.write("""
# Commands
    
Generated on: ${new Date()} by `gendoc.groovy`. 

Covering the macros of [KeY](http://key-project.org).""")

    commands.sort()

    for (t in commands) {
        stream.write(helpForCommand(t) + "\n\n")
    }
    stream.close()
}




taclets = getTaclets().asList()
Collections.sort(taclets, new Comparator<Taclet>() {
    @Override
    int compare(Taclet o1, Taclet o2) {
        return o1.displayName().compareTo(o2.displayName())
    }
})

writeProperties(propertiesFile, taclets)
writeTacletDocumentation(new File(websiteDoc, "taclets.md"), taclets)
writeMacros(new File(websiteDoc, "macros.md"))
writeCommand(new File(websiteDoc, "commands.md"))