package edu.kit.iti.formal.psdbg.lint;

import edu.kit.iti.formal.psdbg.lint.checkers.IssuesId;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

/**
 * @author Alexander Weigl
 * @version 1 (23.05.17)
 */
public class IssuesRepository {
    private static Issues ISSUES;

    public static Issues get() {
        if (ISSUES == null) {
            load();
        }
        return ISSUES;
    }


    public static Issue getIssue(IssuesId id) {
        return get().getIssue()
                .stream()
                .filter(i -> id.toString().equalsIgnoreCase(i.getRulename()))
                .findFirst().orElse(new Issue());

    }

    private static void load() {
        try {
            Unmarshaller unmarshaller =
                    JAXBContext.newInstance(ObjectFactory.class).createUnmarshaller();
            //unmarshaller.setSchema(schema);
            InputStream issuesFile = LinterStrategy.class
                    .getResourceAsStream("lint-issues-en.xml");
            if (issuesFile != null) {
                JAXBElement<Issues> e = (JAXBElement<Issues>) unmarshaller.unmarshal(issuesFile);
                ISSUES = e.getValue();
            } else
                ISSUES = new Issues();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
