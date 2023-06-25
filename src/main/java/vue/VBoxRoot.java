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

/**
 * La classe VBoxRoot est une classe JavaFX qui représente le conteneur VBox principal de l'application.
 * Elle contient les contrôleurs, les données des tables, ainsi que les menus et les actions associées.
 */
public class VBoxRoot extends VBox {

    public static OptionsController optionsController = new OptionsController();
    public static TableController tableController = new TableController();
    public static CellController cellController = new CellController();
    public static ArrayList<DataTable> tables = new ArrayList<>();
    public static Menu menuTables;
    public static final File dossierSauvegarde = new File("saved_data");

    /**
     * Le constructeur de la classe VBoxRoot.
     * Il initialise les contrôleurs, charge les données des tables et crée les menus.
     */
    public VBoxRoot() {
        if (!dossierSauvegarde.exists() || Objects.requireNonNull(dossierSauvegarde.listFiles()).length == 0) {
            // Le dossier de sauvegarde n'existe pas ou ne contient aucune table
            if (!dossierSauvegarde.exists()) {
                boolean success = dossierSauvegarde.mkdir();
                if (!success) {
                    System.out.println("Impossible de créer le dossier.");
                    System.exit(1);
                }
            }
            tables.add(new DataTable("New Table"));
        } else {
            for (File fichier : Objects.requireNonNull(dossierSauvegarde.listFiles())) {
                if (fichier.isFile() && fichier.getName().endsWith(".ser")) {
                    DataTable table = (DataTable) ReaderWriter.read(fichier);
                    tables.add(table);
                }
            }
        }

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
        for (DataTable table : tables) {
            MenuItem itemTable = new MenuItem("▶ " + table.getName());
            itemTable.setUserData(tables.size());
            itemTable.setOnAction(optionsController);
            menuTables.getItems().add(itemTable);
        }

        menuBar.getMenus().addAll(menuActions, menuTables);
        HBoxMain hBoxMain = new HBoxMain();
        getChildren().addAll(menuBar, hBoxMain);
    }
}
