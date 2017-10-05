import de.uka.ilkd.key.api.KeYApi;
import de.uka.ilkd.key.control.KeYEnvironment;
import de.uka.ilkd.key.macros.ProofMacro;
import de.uka.ilkd.key.macros.scripts.ProofScriptCommand;
import de.uka.ilkd.key.macros.scripts.meta.ProofScriptArgument;
import de.uka.ilkd.key.proof.io.ProblemLoaderException;
import de.uka.ilkd.key.rule.Taclet;
import org.key_project.util.collection.ImmutableList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Alexander Weigl
 * @version 1 (11.09.17)
 */
public class GenDoc {
    private static final Set<String> FORBBIDEN = new TreeSet<>();

    static {
        FORBBIDEN.add("exit");
        FORBBIDEN.add("focus");
        FORBBIDEN.add("javascript");
        FORBBIDEN.add("leave");
        FORBBIDEN.add("let \n");
        /*TODO
        script
                schemaVar
        select
                set
        skip
              */
    }

    private static File basedir = new File("..");
    private static File propertiesFile = new File(basedir, "rt-key/src/main/resources/edu/kit/iti/formal/psdbg/taclets.properties.xml");
    private static File dummyFile = new File(".", "rt-key/src/test/resources/edu/kit/iti/formal/psdbg/interpreter/contraposition/contraposition.key");
    private static File websiteDoc = new File(basedir, "website/docs/");


    private static List<Taclet> getTaclets() throws ProblemLoaderException {
        System.out.println("Use dummy file: " + dummyFile.getAbsolutePath());
        KeYEnvironment env = KeYApi.loadFromKeyFile(dummyFile).getLoadedProof().getEnv();
        ImmutableList<Taclet> a = env.getInitConfig().getTaclets();
        return a.stream().collect(Collectors.toList());
    }

    private static void writeProperties(File file, List<Taclet> taclets) {
        Properties documentation = new Properties();
        for (Taclet taclet : taclets) {
            System.out.println((taclet.displayName()));
            documentation.put(taclet.displayName(), taclet.toString());
        }
        // write properties file!
        propertiesFile.getParentFile().mkdirs();
        try (FileOutputStream stream = new FileOutputStream(file)) {
            documentation.storeToXML(stream,
                    String.format("Generated on: %s. Use gen", new Date()),
                    "utf-8");
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeTacletDocumentation(File file, List<Taclet> taclets) {
        file.getParentFile().mkdirs();
        try (FileWriter stream = new FileWriter(file)) {
            stream.write("# Taclets\n\n");
            stream.write(String.format("Generated on: %s", new Date()) +
                    "\n\nCovering the *default* taclets of [KeY](http://key-project.org).");

            for (Taclet t : taclets) {
                stream.write("\n\n## " + t.displayName() + "\n\n");
                stream.write("```\n" + t.toString() + "\n```");
            }

            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeMacros(File file) {
        List<ProofMacro> macros = new ArrayList<>(KeYApi.getMacroApi().getMacros());
        file.getParentFile().mkdirs();
        try (FileWriter stream = new FileWriter(file)) {
            stream.write("# Macros\n\n" +
                    "Generated on: ${new Date()} by `gendoc.groovy`.\n\n"
                    + "Covering the macros of [KeY](http://key-project.org).");

            macros.sort(Comparator.comparing(ProofMacro::getScriptCommandName));

            for (ProofMacro t : macros) {
                stream.write(String.format("\n\n## %s (`%s`) \n\n",
                        t.getName(), t.getScriptCommandName()));
                stream.write(t.getCategory() + "\n\n");
                stream.write(t.getDescription() + "\n\n");
            }
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static String helpForCommand(ProofScriptCommand<?> c) {
        StringBuilder html = new StringBuilder();
        html.append("## " + c.getName());
        html.append("\n> Synopsis: `" + c.getName());

        for (ProofScriptArgument a : c.getArguments()) {
            html.append(' ');
            if (a.isFlag()) {
                html.append("[").append(a.getName()).append("]");
            } else {
                if (!a.isRequired())
                    html.append("[");

                if (a.getName().startsWith("#"))
                    html.append("<" + a.getType().getSimpleName().toUpperCase() + ">");
                else
                    html.append(a.getName() + "=<" + a.getType().getSimpleName().toUpperCase() + ">");

                if (!a.isRequired())
                    html.append("]");
            }
        }
        html.append("`\n\n");

        html.append(c.getDocumentation().replaceAll("\n[ \t]*", "\n"));

        html.append("\n\n**Arguments:**\n");
        for (ProofScriptArgument<?> a : c.getArguments()) {
            html.append(
                    String.format("\n* `%s` :  *%s* ",
                            a.getName(), a.getType().getSimpleName().toUpperCase()));
            if (a.isRequired()) {
                html.append("(*R*)");
            }
            html.append(a.getDocumentation());
        }
        return html.toString();
    }


    private static void writeCommand(File file) {
        List<ProofScriptCommand> commands = new ArrayList<>(KeYApi.getScriptCommandApi().getScriptCommands());

        file.getParentFile().mkdirs();
        try (FileWriter stream = new FileWriter(file)) {
            stream.write("# Commands\n\n");
            stream.write("\n\nGenerated on: " + new Date() + "by `gendoc.groovy`.");
            stream.write("\n\nCovering the proof script commands of [KeY](http://key-project.org).\n\n");

            commands.sort(Comparator.comparing(ProofScriptCommand::getName));

            for (ProofScriptCommand t : commands) {
                if (!FORBBIDEN.contains(t.getName()))
                    stream.write(helpForCommand(t) + "\n\n");
            }
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws ProblemLoaderException {
        List<Taclet> taclets = getTaclets();
        taclets.sort(Comparator.comparing(Taclet::name));

        writeProperties(propertiesFile, taclets);
        writeTacletDocumentation(new File(websiteDoc, "taclets.md"), taclets);
        writeMacros(new File(websiteDoc, "macros.md"));
        writeCommand(new File(websiteDoc, "commands.md"));
    }

}
