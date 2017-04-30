package edu.kit.formatl.proofscriptparser;

/**
 * @author Alexander Weigl
 * @version 1 (27.04.17)
 */
public interface Visitable {
    <T> T accept(Visitor<T> visitor);
}
