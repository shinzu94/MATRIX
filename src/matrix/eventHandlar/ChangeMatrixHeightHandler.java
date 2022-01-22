package matrix.eventHandlar;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import matrix.Main;

public class ChangeMatrixHeightHandler implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent actionEvent) {
        TextField textField = (TextField)actionEvent.getSource();
        Main.setMatrixGridHeight(Integer.parseInt(textField.getText()));
    }
}
