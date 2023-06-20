package vue;

import controleur.MainOptionsController;
import controleur.TableController;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import modele.DataTable;
import outils.ReaderWriter;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class VBoxRoot extends VBox {
    public static MainOptionsController mainOptionsController = new MainOptionsController();
    public static TableController tableController = new TableController();
    public static ArrayList<DataTable> tables = new ArrayList<>();
    public static Menu menuTables;
    public static final File dossierSauvegarde = new File("saved_data");

    public VBoxRoot(){
        if (dossierSauvegarde.exists() && dossierSauvegarde.listFiles() != null) {
            // Des tables existent déjà
            MenuBar menuBar = new MenuBar();

            Menu menuActions = new Menu("☰");
            MenuItem itemNouveau = new MenuItem("➕ New Table");
            MenuItem itemSupprimer = new MenuItem("➖ Delete Table");
            MenuItem itemEnregistrer = new MenuItem("💾 Save Table");
            MenuItem itemQuitter = new MenuItem("❌ Quit");
            itemQuitter.setOnAction(event -> {
                // Créer une boîte de dialogue de confirmation
                Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationDialog.setTitle("Warning");
                confirmationDialog.setHeaderText("Are you sure you want to quit ?");
                confirmationDialog.setContentText("All unsaved progress will be lost.");

                // Ajouter les boutons OK et Cancel à la boîte de dialogue
                confirmationDialog.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

                // Gérer l'action de l'utilisateur en fonction du bouton cliqué
                confirmationDialog.showAndWait().ifPresent(buttonType -> {
                    if (buttonType == ButtonType.OK) {
                        Platform.exit();
                    }
                });
            });

            menuActions.getItems().addAll(itemNouveau, new SeparatorMenuItem(), itemSupprimer, itemEnregistrer, new SeparatorMenuItem(), itemQuitter);

            menuTables = new Menu("TABLES");
            for (File fichier : Objects.requireNonNull(dossierSauvegarde.listFiles())) {
                if (fichier.isFile() && fichier.getName().endsWith(".ser")) {
                    DataTable table = (DataTable) ReaderWriter.read(fichier);
                    tables.add(table);
                    MenuItem itemTable = new MenuItem("▶ " + table.getName());
                    menuTables.getItems().add(itemTable);
                }
            }

            menuBar.getMenus().addAll(menuActions, menuTables);
            HBoxMain hBoxRoot = new HBoxMain();
            getChildren().addAll(menuBar, hBoxRoot);

        } else {
            // Le dossier de sauvegarde n'existe pas ou ne contient aucune table
            if (!dossierSauvegarde.exists()){
                boolean success = dossierSauvegarde.mkdir();
                if (!success) {
                    System.out.println("Impossible de créer le dossier.");
                    System.exit(1);
                }
            }
        }
    }
}
