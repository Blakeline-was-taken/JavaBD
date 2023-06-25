package vue;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

/**
 * La classe MainApplication est la classe principale de l'application JavaFX.
 * Elle étend la classe Application de JavaFX et gère le démarrage de l'application.
 */
public class MainApplication extends Application {

    /**
     * La méthode start est appelée lors du démarrage de l'application.
     * Elle crée une scène avec un conteneur VBoxRoot, lui applique une feuille de style CSS
     * et l'affiche dans la fenêtre principale.
     *
     * @param stage la fenêtre principale de l'application
     */
    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new VBoxRoot());
        scene.getStylesheets().add((new File("css"+File.separator+"style.css")).toURI().toString());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * La méthode main est le point d'entrée de l'application.
     * Elle lance l'application JavaFX en appelant la méthode launch avec les arguments spécifiés.
     *
     * @param args les arguments de ligne de commande
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
}
