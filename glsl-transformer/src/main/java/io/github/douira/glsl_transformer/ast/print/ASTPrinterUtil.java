package io.github.douira.glsl_transformer.ast.print;

import java.util.List;

import io.github.douira.glsl_transformer.ast.ASTNode;
import io.github.douira.glsl_transformer.ast.print.token.*;
import io.github.douira.glsl_transformer.ast.traversal.ASTListenerVisitor;
import io.github.douira.glsl_transformer.print.filter.TokenChannel;

public abstract class ASTPrinterUtil extends ASTListenerVisitor<Void> {
  private ASTNode currentNode;

  protected ASTPrinterUtil() {
    super();
  }

  protected abstract String generateString();

  protected abstract void emitToken(PrintToken token);

  public static String printAST(ASTPrinter printer, ASTNode node) {
    printer.visit(node);
    return printer.generateString();
  }

  public static String printSimple(ASTNode node) {
    return printAST(new SimpleASTPrinter(), node);
  }

  protected void emitTokens(PrintToken... tokens) {
    for (PrintToken t : tokens) {
      emitToken(t);
    }
  }

  protected void emitLiteral(TokenRole role, String literal) {
    emitToken(new LiteralToken(currentNode, role, literal));
  }

  protected void emitLiteral(String literal) {
    emitLiteral(TokenRole.DEFAULT, literal);
  }

  protected void emitLiterals(TokenRole role, String... literals) {
    for (String l : literals) {
      emitLiteral(role, l);
    }
  }

  protected void emitLiterals(String... literals) {
    emitLiterals(TokenRole.DEFAULT, literals);
  }

  protected void emitType(TokenRole role, int type) {
    emitToken(new ParserToken(currentNode, role, type));
  }

  protected void emitType(int type) {
    emitType(TokenRole.DEFAULT, type);
  }

  protected void emitType(TokenRole role, int... types) {
    for (int t : types) {
      emitType(role, t);
    }
  }

  protected void emitType(int... types) {
    emitType(TokenRole.DEFAULT, types);
  }

  protected void emitWhitespace(TokenRole role, String whitespace) {
    emitToken(new LiteralToken(currentNode, TokenChannel.WHITESPACE, role, whitespace));
  }

  protected void emitExactWhitespace(String whitespace) {
    emitWhitespace(TokenRole.EXACT, whitespace);
  }

  private void emitSpace(TokenRole role) {
    emitWhitespace(role, " ");
  }

  protected void emitExactSpace() {
    emitSpace(TokenRole.EXACT);
  }

  protected void emitExtendableSpace() {
    emitSpace(TokenRole.EXTENDABLE_SPACE);
  }

  protected void emitBreakableSpace() {
    emitSpace(TokenRole.BREAKABLE_SPACE);
  }

  private void emitNewline(TokenRole role) {
    emitWhitespace(role, "\n");
  }

  protected void emitExactNewline() {
    emitNewline(TokenRole.EXACT);
  }

  protected void emitCommonNewline() {
    emitNewline(TokenRole.COMMON_FORMATTING);
  }

  protected void visitWithSeparator(List<? extends ASTNode> nodes, Runnable emitter) {
    for (int i = 0, size = nodes.size(); i < size; i++) {
      visit(nodes.get(i));
      if (i < size - 1) {
        emitter.run();
      }
    }
  }

  protected ASTNode getCurrentNode() {
    return currentNode;
  }

  protected void setCurrentNode(ASTNode currentNode) {
    this.currentNode = currentNode;
  }

  protected boolean visitSafe(ASTNode node) {
    if (node != null) {
      visit(node);
      return false;
    }
    return true;
  }

  @Override
  public Void visit(ASTNode node) {
    currentNode = node;
    return super.visit(node);
  }

  @Override
  public Void initialResult() {
    return null;
  }

  @Override
  public Void superNodeTypeResult() {
    return null;
  }

  @Override
  public Void defaultResult() {
    throw new IllegalStateException("The default value should never be used and all nodes should be printed properly!");
  }
}
