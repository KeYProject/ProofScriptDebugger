package edu.kit.iti.formal.psdbg.interpreter.dbg;

import edu.kit.iti.formal.psdbg.parser.ast.ASTNode;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public abstract class Blocker {
    public interface BlockPredicate extends Predicate<ASTNode> {
    }

    @Data
    public static class Breakpoint {
        private int line;
        private String source;
    }

    public static class BreakpointLine implements BlockPredicate {
        @Getter
        @Setter
        private final Set<Integer> lines = new TreeSet<>();

        private final Breakpoint cmp = new Breakpoint();

        @Override
        public boolean test(ASTNode node) {
            cmp.line = node.getStartPosition().getLineNumber();
            cmp.source = node.getOrigin();
            return lines.contains(cmp);
        }
    }

    public static class CounterBlocker implements BlockPredicate {
        @Getter
        private final AtomicInteger stepUntilBlock = new AtomicInteger(-1);

        public CounterBlocker(int steps) {
            stepUntilBlock.set(steps);
        }

        @Override
        public boolean test(ASTNode astNode) {
            if (stepUntilBlock.get() > 0) {
                stepUntilBlock.decrementAndGet();
            }
            return stepUntilBlock.get() == 0;
        }

        public void deactivate() {
            stepUntilBlock.set(-1);
        }
    }

    @RequiredArgsConstructor
    public static class UntilNode implements BlockPredicate {
        @Getter
        private final ASTNode node;

        @Override
        public boolean test(ASTNode astNode) {
            return node.equals(astNode);
        }
    }

    @RequiredArgsConstructor
    public static class NextWithParent implements BlockPredicate {
        @Getter
        private final ASTNode parent;

        @Override
        public boolean test(ASTNode astNode) {
            return parent.equals(astNode.getParent());
        }
    }


    @RequiredArgsConstructor
    public static class ParentInContext implements BlockPredicate {
        @Getter
        private final ASTNode[] context;

        @Override
        public boolean test(ASTNode astNode) {
            for (ASTNode node : context) {
                if (astNode.isAncestor(node))
                    return true;
            }
            return false;
        }
    }


}
