package matrix;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import matrix.domain.matrix.Expression;
import matrix.domain.matrix.Matrix;
import matrix.domain.wolframalpha.MatrixToWolframalphaConverter;
import matrix.eventHandlar.ChangeMatrixHeightHandler;
import matrix.eventHandlar.ChangeMatrixWidthHandler;
import matrix.eventHandlar.SubmitMatrixToSystemOfEquationsWolframAlphaHandler;
import matrix.eventHandlar.SubmitMatrixToWolframAlphaHandler;

public class Main extends Application {
    public static final String MATRIX_PANE_ID = "matrixPane";
    public static final String MATRIX_WIDTH_ID = "matrixWidth";
    public static final String MATRIX_HEIGHT_ID = "matrixHeight";
    public static final String VARIABLE_LABEL_ID = "variableLabel";
    public static final String RESULT_ID = "result";
    private static final BorderPane mainPane = new BorderPane();
    private static String[] variables = new String[]{"x", "y", "z", "w"};
    private static int matrixGridWidth = 5;
    private static int matrixGridHeight = 5;
    private static int previousMatrixGridWidth = 5;
    private static int previousMatrixGridHeight = 5;
    private static Matrix previousMatrix = new Matrix();
    private static Matrix currentMatrix = new Matrix();
    private static GridPane matrixPane = new GridPane();
    private static GridPane variablesPane = new GridPane();
    private static TextField result = new TextField();

    public static int getMatrixGridWidth() {
        return matrixGridWidth;
    }

    public static void setMatrixGridWidth(int matrixGridWidth) {
        Main.previousMatrixGridWidth = Main.matrixGridWidth;
        Main.matrixGridWidth = matrixGridWidth;
        redefineMatrixPane();
        prepareVariablesPane(false);
        Main.previousMatrixGridWidth = Main.matrixGridWidth;
    }

    public static int getMatrixGridHeight() {
        return matrixGridHeight;
    }

    public static void setMatrixGridHeight(int matrixGridHeight) {
        Main.previousMatrixGridHeight = Main.matrixGridHeight;
        Main.matrixGridHeight = matrixGridHeight;
        redefineMatrixPane();
        Main.previousMatrixGridHeight = Main.matrixGridHeight;
    }

    private static void redefineMatrixPane() {
        ObservableList<Node> rows = matrixPane.getChildren();
        Expression[][] expressions = new Expression[previousMatrixGridHeight][previousMatrixGridWidth];

        for (Node item : rows) {
            TextField field = (TextField) item;

            String[] cellIdParts = field.getId().split("-");
            int positionX = Integer.parseInt(cellIdParts[2]);
            int positionY = Integer.parseInt(cellIdParts[1]);
            if (!(positionY >= previousMatrixGridHeight || positionX >= previousMatrixGridWidth)) {
                expressions[positionY][positionX] = new Expression(field.getText());
            }

            field.setText("");
            field.setId("");
            field.setVisible(false);
        }
        matrixPane.setId("");
        createMatrixPane();

        previousMatrix = new Matrix(expressions);

        Expression[][] newExpressions = new Expression[matrixGridHeight][matrixGridWidth];
        for (int i = 0; i < matrixGridHeight; i++) {
            for (int j = 0; j < matrixGridWidth; j++) {
                TextField field = createMatrixField(i, j);
                if (i < previousMatrixGridHeight && j < previousMatrixGridWidth) {
                    field.setText(previousMatrix.getInString(i, j));
                    field.setPrefWidth((previousMatrix.getInString(i, j).length() + 1) * 10);
                    newExpressions[i][j] = new Expression(previousMatrix.getInString(i, j));
                }
                matrixPane.add(field, j, i);
            }
        }

        currentMatrix = new Matrix(newExpressions);
    }

    static public void updateCurrentMatrix() {
        Expression[][] expressions = new Expression[matrixGridHeight][matrixGridWidth];
        ObservableList<Node> rows = matrixPane.getChildren();
        for (Node item : rows) {
            TextField field = (TextField) item;

            String[] cellIdParts = field.getId().split("-");
            int positionX = Integer.parseInt(cellIdParts[2]);
            int positionY = Integer.parseInt(cellIdParts[1]);
            if (!(positionY >= matrixGridHeight || positionX >= matrixGridWidth)
                    &&
                    !(positionY >= previousMatrixGridHeight || positionX >= previousMatrixGridWidth)) {
                expressions[positionY][positionX] = new Expression(field.getText());
            }
        }

        currentMatrix = new Matrix(expressions);
    }

    private static TextField createMatrixField(int i, int j) {
        TextField field = new TextField();
        field.setId("matrix-" + i + "-" + j);
        field.setPrefWidth(30);
        field.setMinWidth(30);
        field.setMaxWidth(600);
        return field;
    }

    private static void createMatrixPane() {
        matrixPane = new GridPane();
        matrixPane.setId(MATRIX_PANE_ID);
        matrixPane.setPrefHeight(matrixGridHeight);
        matrixPane.setPrefWidth(matrixGridWidth);
        matrixPane.setPadding(new Insets(40));
        matrixPane.setVgap(20);
        matrixPane.setHgap(20);
        mainPane.setCenter(matrixPane);
    }

    private static void defineMatrixPane() {
        matrixPane.setPrefHeight(matrixGridHeight);
        matrixPane.setPrefWidth(matrixGridWidth);

        for (int i = 0; i < matrixGridHeight; i++) {
            for (int j = 0; j < matrixGridWidth; j++) {
                TextField field = createMatrixField(i, j);
                matrixPane.add(field, j, i);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static FlowPane prepareHeaderPane(Stage primaryStage) {
        FlowPane headerPane = new FlowPane();
        headerPane.setPadding(new Insets(20));
        headerPane.setHgap(15);
        primaryStage.setTitle("Macierze");

        GridPane actionPane = prepareActionPane();

        Label matrixLabelHeight = new Label("Wysokość macierzy: ");
        TextField matrixHeightField = prepareMatrixHeightField();

        Label matrixLabelWidth = new Label("Szerokość macierzy: ");
        TextField matrixWidthField = prepareMatrixWidthField();

        prepareVariablesPane(true);


        headerPane.getChildren().addAll(matrixLabelHeight, matrixHeightField, matrixLabelWidth, matrixWidthField, actionPane, variablesPane);
        return headerPane;
    }

    private static GridPane prepareActionPane() {
        GridPane actionPane = new GridPane();
        variablesPane.setHgap(5);
        variablesPane.setVgap(10);

        Button convertToWolframAlpha = new Button();
        convertToWolframAlpha.setText("Konwertuj do\nWolframAlpha");
        convertToWolframAlpha.setOnAction(new SubmitMatrixToWolframAlphaHandler());
        convertToWolframAlpha.setPrefHeight(70);

        Button convertToSystemEquationsWolframAlpha = new Button();
        convertToSystemEquationsWolframAlpha.setText("Konwertuj do\nukładu równań\nWolframAlpha");
        convertToSystemEquationsWolframAlpha.setPrefHeight(70);
        convertToSystemEquationsWolframAlpha.setOnAction(new SubmitMatrixToSystemOfEquationsWolframAlphaHandler());

        actionPane.add(convertToWolframAlpha, 0, 0);
        actionPane.add(convertToSystemEquationsWolframAlpha, 1, 0);
        return actionPane;
    }

    private static void prepareVariablesPane(boolean firstExecute) {
        if (firstExecute) {
            variablesPane = new GridPane();
            variablesPane.setHgap(5);
        }

        int variableCount = matrixGridWidth - 1;
        String[] newVariables = new String[variableCount];
        int max = 0;
        if (!firstExecute) {
            for (Node item : variablesPane.getChildren()) {
                if (item.getId() != VARIABLE_LABEL_ID) {
                    TextField field = (TextField) item;
                    String[] partsId = field.getId().split("-");
                    if (Integer.parseInt(partsId[1]) >= max) {
                        max = Integer.parseInt(partsId[1]) + 1;
                    }
                    if (Integer.parseInt(partsId[1]) < variableCount) {
                        field.setVisible(true);
                        newVariables[Integer.parseInt(partsId[1])] = field.getText();
                    } else {
                        field.setVisible(false);
                    }
                }
            }
        } else {
            Label label = new Label("Zmienne:");
            label.setId(VARIABLE_LABEL_ID);
            variablesPane.add(label, 0, 0);
        }
        for (int i = max; i < variableCount; i++) {
            TextField variableField = new TextField();
            variableField.setId("variable-" + i);
            if (firstExecute && variables.length > i) {
                variableField.setText(variables[i]);
            }
            if(variableCount > i) {
                newVariables[i] = variableField.getText();
            }

            variableField.setPrefWidth(30);
            variablesPane.add(variableField, i + 1, 0);
        }
        variables = newVariables;
    }

    private static TextField prepareMatrixHeightField() {
        TextField matrixHeightField = new TextField();
        matrixHeightField.setId(MATRIX_HEIGHT_ID);
        matrixHeightField.setText(String.valueOf(matrixGridHeight));
        matrixHeightField.setPrefColumnCount(3);
        matrixHeightField.setOnAction(new ChangeMatrixHeightHandler());
        return matrixHeightField;
    }

    private static TextField prepareMatrixWidthField() {
        TextField matrixWidthField = new TextField();
        matrixWidthField.setId(MATRIX_WIDTH_ID);
        matrixWidthField.setText(String.valueOf(matrixGridWidth));
        matrixWidthField.setPrefColumnCount(3);
        matrixWidthField.setOnAction(new ChangeMatrixWidthHandler());
        return matrixWidthField;
    }

    static public void setResultText(String text) {
        result.setText(text);
    }

    static public Matrix getCurrentMatrix() {
        return currentMatrix;
    }

    public static String[] getVariables() {
        return variables;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FlowPane headerPane = prepareHeaderPane(primaryStage);

        mainPane.setTop(headerPane);
        FlowPane resultPane = new FlowPane();

        result = new TextField();
        result.setId(RESULT_ID);
        result.setMinWidth(300);

        resultPane.setHgap(40);
        resultPane.setPadding(new Insets(40));

        resultPane.getChildren().addAll(result);
        mainPane.setBottom(resultPane);
        createMatrixPane();
        defineMatrixPane();

        primaryStage.setScene(new Scene(mainPane, 700, 500));
        primaryStage.show();
    }

}
