package matrix.domain.matrix;

public class Matrix {
    private Expression[][] matrix;

    public Matrix(Expression matrix[][]) {
        this.matrix = matrix;
    }

    public Matrix() {
        this.matrix = new Expression[][]{};
    }

    public String getInString(int row, int column) {
        return matrix[row][column] instanceof Expression ? matrix[row][column].toString() : "";
    }

    public Expression[][] getExpression() {
        return matrix;
    }
}
