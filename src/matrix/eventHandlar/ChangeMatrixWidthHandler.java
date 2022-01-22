package matrix.eventHandlar;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import matrix.Main;

public class ChangeMatrixWidthHandler implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent actionEvent) {
        TextField textField = (TextField)actionEvent.getSource();
        Main.setMatrixGridWidth(Integer.parseInt(textField.getText()));
    }
}
