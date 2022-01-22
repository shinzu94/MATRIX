package matrix.domain.matrix;

public class Expression {
    private String expression;

    public Expression(String Expression) {
        expression = Expression;
    }

    public String getExpression() {
        return expression;
    }

    public Expression setExpression(String expression) {
        this.expression = expression;
        return this;
    }

    public String calculate() {
        return expression;
    }

    @Override
    public String toString() {
        return expression;
    }
}
