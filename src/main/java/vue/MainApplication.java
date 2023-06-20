package vue;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new VBoxRoot());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String [] args){
        Application.launch(args);
    }
}