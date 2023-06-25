package vue;

import controleur.CellController;
import controleur.OptionsController;
import controleur.TableController;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import modele.DataTable;
import outils.ReaderWriter;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class VBoxRoot extends VBox {
    public static OptionsController optionsController = new OptionsController();
    public static TableController tableController = new TableController();
    public static CellController cellController = new CellController();
    public static ArrayList<DataTable> tables = new ArrayList<>();
    public static Menu menuTables;
    public static final File dossierSauvegarde = new File("saved_data");

    public VBoxRoot(){
        if (dossierSauvegarde.exists() && dossierSauvegarde.listFiles() != null) {
            // Des tables existent déjà
            MenuBar menuBar = new MenuBar();

            Menu menuActions = new Menu("☰");
            MenuItem itemNouveau = new MenuItem("➕ New Table");
            itemNouveau.getStyleClass().add("button-add");
            MenuItem itemSupprimer = new MenuItem("➖ Delete Table");
            itemSupprimer.getStyleClass().add("button-rem");
            MenuItem itemEnregistrer = new MenuItem("💾 Save Table");
            MenuItem itemQuitter = new MenuItem("❌ Quit");

            itemNouveau.setOnAction(optionsController);
            itemSupprimer.setOnAction(optionsController);
            itemEnregistrer.setOnAction(optionsController);
            itemQuitter.setOnAction(optionsController);

            menuActions.getItems().addAll(itemNouveau, itemSupprimer, itemEnregistrer, new SeparatorMenuItem(), itemQuitter);

            menuTables = new Menu("TABLES");
            for (File fichier : Objects.requireNonNull(dossierSauvegarde.listFiles())) {
                if (fichier.isFile() && fichier.getName().endsWith(".ser")) {
                    DataTable table = (DataTable) ReaderWriter.read(fichier);
                    MenuItem itemTable = new MenuItem("▶ " + table.getName());
                    // Indice de la table à laquelle il fait référence.
                    itemTable.setUserData(tables.size());
                    itemTable.setOnAction(optionsController);
                    tables.add(table);
                    menuTables.getItems().add(itemTable);
                }
            }

            menuBar.getMenus().addAll(menuActions, menuTables);
            HBoxMain hBoxMain = new HBoxMain();
            getChildren().addAll(menuBar, hBoxMain);

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
