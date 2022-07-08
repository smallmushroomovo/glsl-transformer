package io.github.douira.glsl_transformer.ast.node.expression;

import io.github.douira.glsl_transformer.ast.traversal.*;
import io.github.douira.glsl_transformer.parse_ast.Type;

public class LiteralExpression extends TerminalExpression {
  public Type literalType;
  public boolean booleanValue;
  public long integerValue;
  public IntegerFormat integerFormat;
  public double floatingValue;

  public enum IntegerFormat {
    DECIMAL,
    HEXADECIMAL,
    OCTAL
  }

  public LiteralExpression(Type literalType, boolean booleanValue) {
    this.literalType = literalType;
    this.booleanValue = booleanValue;
  }

  public LiteralExpression(Type literalType, long integerValue) {
    this(literalType, integerValue, IntegerFormat.DECIMAL);
  }

  public LiteralExpression(Type literalType, long integerValue, IntegerFormat integerFormat) {
    this.literalType = literalType;
    this.integerValue = integerValue;
    this.integerFormat = integerFormat;
  }

  public LiteralExpression(Type literalType, double floatingValue) {
    this.literalType = literalType;
    this.floatingValue = floatingValue;
  }

  public Number getNumber() {
    switch (literalType.getNumberType()) {
      case BOOLEAN:
        return booleanValue ? 1 : 0;
      case SIGNED_INTEGER:
      case UNSIGNED_INTEGER:
        return integerValue;
      case FLOATING_POINT:
        return floatingValue;
      default:
        throw new IllegalArgumentException("Unsupported literal type: " + literalType);
    }
  }

  public boolean isPositive() {
    switch (literalType.getNumberType()) {
      case BOOLEAN:
        return booleanValue;
      case SIGNED_INTEGER:
      case UNSIGNED_INTEGER:
        return integerValue > 0;
      case FLOATING_POINT:
        return floatingValue > 0;
      default:
        throw new IllegalArgumentException("Unsupported literal type: " + literalType);
    }
  }

  public int getIntegerRadix() {
    return integerFormat == IntegerFormat.HEXADECIMAL
        ? 16
        : integerFormat == IntegerFormat.OCTAL
            ? 8
            : 10;
  }

  @Override
  public ExpressionType getExpressionType() {
    return ExpressionType.LITERAL;
  }

  @Override
  public <R> R expressionAccept(ASTVisitor<R> visitor) {
    return visitor.visitLiteralExpression(this);
  }

  @Override
  public void enterNode(ASTListener listener) {
    super.enterNode(listener);
    listener.enterLiteralExpression(this);
  }

  @Override
  public void exitNode(ASTListener listener) {
    super.exitNode(listener);
    listener.exitLiteralExpression(this);
  }
}
