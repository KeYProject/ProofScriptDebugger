package edu.kit.formal.proofscriptparser;

/**
 * An interface for all classes that can be visited by {@link Visitor}.
 *
 * @author Alexander Weigl
 * @version 1 (27.04.17)
 */
public interface Visitable {
    <T> T accept(Visitor<T> visitor);
}
