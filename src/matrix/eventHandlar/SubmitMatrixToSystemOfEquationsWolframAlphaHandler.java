package matrix.eventHandlar;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import matrix.Main;
import matrix.domain.wolframalpha.MatrixToWolframalphaConverter;

public class SubmitMatrixToSystemOfEquationsWolframAlphaHandler implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent actionEvent) {
        Main.updateCurrentMatrix();
        Main.setResultText(MatrixToWolframalphaConverter.convertToSystemOfEquations(Main.getCurrentMatrix()));
    }
}
