package edu.kit.formal.proofscriptparser.lint.checkers;

import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (23.05.17)
 */
public interface SearcherFactory {
    Searcher create();
}
