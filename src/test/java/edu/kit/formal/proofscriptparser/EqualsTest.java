package edu.kit.formal.proofscriptparser;

/*-
 * #%L
 * ProofScriptParser
 * %%
 * Copyright (C) 2017 Application-oriented Formal Verification
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */



import edu.kit.formal.proofscriptparser.ast.ProofScript;
import edu.kit.formal.proofscriptparser.ast.Variable;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (01.05.17)
 */
public class EqualsTest {
    @Test public void test() throws IOException {
        List<ProofScript> list = Facade
                .getAST(new File("src/test/resources/edu/kit/formal/proofscriptparser/scripts/justarithmetic.txt"));
        ProofScript a = list.get(0);
        ProofScript b = a.copy();

        Assert.assertEquals("Test equality for 'a'",
                a.getSignature().get(new Variable("a")), b.getSignature().get(new Variable("a")));

        Assert.assertEquals("signatures not equals",
                a.getSignature().entrySet(),
                b.getSignature().entrySet());
        Assert.assertEquals(a.getBody(), b.getBody());
        Assert.assertEquals(a, b);
    }
}
