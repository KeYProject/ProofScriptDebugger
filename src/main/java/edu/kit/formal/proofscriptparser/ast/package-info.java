/**
 * This package contains the nodes of the AST and related classes.
 * <p>
 * <p>
 * <b>How to extend the AST?</b>
 * Inherit from {@link edu.kit.formal.proofscriptparser.ast.ASTNode} with a proper template argument of the corresponding {@link org.antlr.v4.runtime.ParserRuleContext}.
 * You will need to define the {@link edu.kit.formal.proofscriptparser.ast.ASTNode#clone()}
 * and {@link edu.kit.formal.proofscriptparser.ast.ASTNode#accept(edu.kit.formal.proofscriptparser.Visitor)} method.
 *
 * @author Alexander Weigl
 * @version 1 (01.05.17)
 */
package edu.kit.formal.proofscriptparser.ast;