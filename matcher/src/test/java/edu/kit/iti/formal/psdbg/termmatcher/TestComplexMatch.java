package edu.kit.iti.formal.psdbg.termmatcher;


import de.uka.ilkd.key.api.KeYApi;
import de.uka.ilkd.key.api.ProofApi;
import de.uka.ilkd.key.api.ProofManagementApi;
import de.uka.ilkd.key.logic.Sequent;
import de.uka.ilkd.key.proof.init.ProofInputException;
import de.uka.ilkd.key.proof.io.ProblemLoaderException;
import org.junit.Test;

import java.io.File;
import java.util.regex.Pattern;

/*
         shouldMatchSeq("seqPerm(seqDef{int u;}(0, result_1.length, any::select(heapAfter_copyArray_0, result_1, arr(u))), seqDef{int u;}(0, result_0.length, any::select(heapAfter_copyArray_0, result_0, arr(u))))",
                "seqPerm(?X, ?Y)");
 */
public class TestComplexMatch {
    @Test
    public void test() throws ProblemLoaderException, ProofInputException {
        ProofManagementApi a = KeYApi.loadFromKeyFile(new File("src/test/resources/edu/kit/iti/formal/psdbg/termmatcher/javacomplex/project.key"));
        ProofApi pa = a.startProof(a.getProofContracts().get(3));
        Sequent sequent = pa.getProof().root().sequent();
        System.out.println(sequent);
       /* Matchings result = MatcherFacade.matches("... update-application(_, ?X) ...", sequent, false);
        System.out.println(result);
        String exp = normalize("\\<{exc=null;try { result=self.foo(_x)@Test;} catch (java.lang.Throwable e) { exc=e; } }\\> (and(and(and(gt(result,Z(0(#))),java.lang.Object::<inv>(heap,self)),equals(exc,null)),all{f:Field}(all{o:java.lang.Object}(or(or(elementOf(o,f,allLocs),and(not(equals(o,null)),not(equals(boolean::select(heapAtPre,o,java.lang.Object::<created>),TRUE)))),equals(any::select(heap,o,f),any::select(heapAtPre,o,f)))))))");
        String x = normalize(result.iterator().next().get("?X").getUnit().toString().replace("\n", " "));
        Assert.assertEquals(exp, x);*/
    }

    private String normalize(String s) {
        Pattern re = Pattern.compile("\\s+");
        return re.matcher(s).replaceAll(" ");
    }

}
