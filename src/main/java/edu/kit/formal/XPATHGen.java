package edu.kit.formal;

import de.uka.ilkd.key.api.KeYApi;
import de.uka.ilkd.key.api.ProofApi;
import de.uka.ilkd.key.api.ProofManagementApi;
import de.uka.ilkd.key.logic.Sequent;
import de.uka.ilkd.key.logic.SequentFormula;
import de.uka.ilkd.key.logic.Term;
import de.uka.ilkd.key.proof.Proof;
import de.uka.ilkd.key.proof.io.ProblemLoaderException;
import org.basex.core.Context;
import org.basex.core.cmd.*;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;

import java.io.*;

/**
 * @author Alexander Weigl
 * @version 1 (19.07.17)
 */
public class XPATHGen {
    public static void main(String[] args) throws ProblemLoaderException, IOException {
        String s = loadSequentToXml(args[0]);
        Context context = new Context(false);
        new Set("CREATEFILTER", "*.xml").execute(context);
        new CreateDB("Collection").execute(context);
        new Add("", "src/main/resources/").execute(context);
        new Optimize().execute(context);
        //new CreateDB("Collection", "src/main/resources/").execute(context);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String declare = String.format("declare variable $term := %s;",
                s.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""));
        System.out.println(declare);
        new Add("", s);
        //new XQuery(declare).execute(context, System.out);

        outer:
        while (true) {
            String tmp = "";
            String query = "";
            while ((tmp = reader.readLine()) != null) {
                if (tmp.isEmpty()) break;
                if (tmp.equalsIgnoreCase("exit"))
                    break outer;
                query += tmp + '\n';
            }
            System.out.println(query);
            if (!query.isEmpty())
                new XQuery(query).execute(context, System.out);
        }
        //new DropDB("Collection").execute(context);
    }

    private static Element rewrite(SequentFormula f) {
        Element e = new Element("sequentFormula");
        e.getChildren().add(rewrite(f.formula()));
        return e;
    }

    private static Element rewrite(Term t) {
        Element e = new Element("term");

        e.setAttribute("depth", String.valueOf(t.depth()));
        e.setAttribute("arity", String.valueOf(t.arity()));
        e.setAttribute("rigid", String.valueOf(t.isRigid()));
        e.setAttribute("sort", t.sort().toString());
        e.setAttribute("serialNumber", String.valueOf(t.serialNumber()));
        e.setAttribute("name", t.op().toString());

        for (Term s : t.subs()) {
            e.getChildren().add(rewrite(s));
        }

        return e;
    }

    private static String loadSequentToXml(String file) throws ProblemLoaderException, IOException {
        ProofManagementApi pma = KeYApi.loadFromKeyFile(new File(file));
        ProofApi pa = pma.getLoadedProof();
        Proof p = pa.getProof();
        Sequent sequent = p.root().sequent();

        Element ante = new Element("antecedent");
        Element succ = new Element("succedent");
        Element root = new Element("sequent");
        root.getChildren().add(ante);
        root.getChildren().add(succ);
        Document doc = new Document(root);

        for (SequentFormula f : sequent.antecedent()) {
            ante.getChildren().add(rewrite(f));
        }

        for (SequentFormula f : sequent.succedent()) {
            succ.getChildren().add(rewrite(f));
        }


        // new XMLOutputter().output(doc, System.out);
        XMLOutputter xmlOutput = new XMLOutputter();//Format.getPrettyFormat());
        StringWriter writer = new StringWriter();
        xmlOutput.output(doc, writer);
        return writer.getBuffer().toString();
    }
}
