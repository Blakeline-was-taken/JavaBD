package controleur;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import vue.HBoxMain;

public class MainOptionsController implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent event) {
        Button btn = (Button) event.getSource();
        switch (btn.getText()){
            case "◀": {
                if (HBoxMain.history.size() > 0){
                    HBoxMain.undo();
                }
            }
            case "▶": {
                if (HBoxMain.future.size() > 0){
                    HBoxMain.redo();
                }
            }
            case "💾": {
                // TODO: Sauvegarde
                return;
            }
            case "SELECT": {
                // TODO: Select
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Pop-up");
                alert.setHeaderText(null);
                alert.setContentText("Hello World !");
                alert.showAndWait();
            }
            case "JOIN": {
                // TODO: Join
                return;
            }
            default: {
                return;
            }
        }
    }
}
