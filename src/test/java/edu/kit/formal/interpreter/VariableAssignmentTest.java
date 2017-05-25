package edu.kit.formal.interpreter;

import edu.kit.formal.proofscriptparser.ast.Type;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by sarah on 5/23/17.
 */
public class VariableAssignmentTest {
    VariableAssignment va1;
    VariableAssignment va2;
    VariableAssignment va3;

    @Before
    public void setup() {
        va1 = new VariableAssignment(null);
        va1.addVarDecl("a", Type.INT);
        va1.addVarDecl("b", Type.INT);
        va1.addVarDecl("c", Type.INT);
        va1.setVarValue("a", Value.from(1));
        va1.setVarValue("b", Value.from(2));
        va1.setVarValue("c", Value.from(3));

        va2 = new VariableAssignment(null);
        va2.addVarDecl("d", Type.INT);
        va2.addVarDecl("e", Type.INT);
        va2.addVarDecl("f", Type.INT);
        va2.setVarValue("d", Value.from(1));
        va2.setVarValue("e", Value.from(2));
        va2.setVarValue("f", Value.from(3));

        va3 = new VariableAssignment(null);
        va3.addVarDecl("a", Type.INT);
        va3.addVarDecl("b", Type.INT);
        va3.addVarDecl("c", Type.INT);
        va3.setVarValue("a", Value.from(1));
        va3.setVarValue("b", Value.from(3));
        va3.setVarValue("c", Value.from(3));
    }

    @Test
    public void testjoinWithOutCheck1() {
        va1.joinWithoutCheck(va2);
        // Assert.assertEquals(6, va1.getValues().size());

    }

    @Test
    public void testjoinWithOutCheck2() {
        va1.joinWithoutCheck(va3);
        Assert.assertEquals(3, va1.getValues().size());

    }

    @Test
    public void testjoinWithCheck1() {
        VariableAssignment ret = va1.joinWithCheck(va2);
        // Assert.assertEquals(6, ret.getValues().size());

    }

    @Test
    public void testjoinWithCheck2() {
        VariableAssignment ret = va1.joinWithCheck(va3);
        Assert.assertEquals(0, ret.getValues().size());

    }
}
