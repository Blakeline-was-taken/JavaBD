package vue;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new VBoxRoot());
        scene.getStylesheets().add((new File("css"+File.separator+"style.css")).toURI().toString());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String [] args){
        Application.launch(args);
    }
}