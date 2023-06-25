package vue;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;

import modele.DataTable;
import java.util.ArrayDeque;

/**
 * La classe HBoxMain est une classe JavaFX qui représente le conteneur HBox principal de l'interface utilisateur.
 * Elle gère le système d'historique des modifications de la table.
 */
public class HBoxMain extends HBox {

    /**
     * La vue de table principale.
     */
    public static VBoxTable vBoxTable;

    /**
     * L'historique des tables précédentes.
     */
    public static ArrayDeque<DataTable> history = new ArrayDeque<>();

    /**
     * L'historique des tables suivantes (annulées puis rétablies).
     */
    public static ArrayDeque<DataTable> future = new ArrayDeque<>();

    /**
     * La table de données courante.
     */
    public static DataTable currentTable;

    /**
     * Constructeur de la classe HBoxMain.
     * Crée le conteneur HBox principal avec les options, la séparation verticale et la vue de table.
     */
    public HBoxMain() {
        VBoxOptions vBoxOptions = new VBoxOptions();
        Separator separator = new Separator(Orientation.VERTICAL);
        separator.setPadding(new Insets(10, 0, 10, 0));
        currentTable = VBoxRoot.tables.get(0);
        vBoxTable = new VBoxTable(currentTable.copy());
        getChildren().addAll(vBoxOptions, separator, vBoxTable);
        setSpacing(10);
    }

    /**
     * Rafraîchis la vue de la table.
     */
    private static void updateVBoxTable() {
        vBoxTable.getChildren().clear();
        vBoxTable.getChildren().addAll(new VBoxTable(currentTable.copy()).getChildren());
    }

    /**
     * Met à jour la table courante avec une nouvelle table spécifiée,
     * et ajoute l'ancienne table à l'historique.
     *
     * @param table La nouvelle table à afficher.
     */
    public static void updateTable(DataTable table) {
        future.clear();
        history.add(currentTable.copy());
        currentTable = table;
        updateVBoxTable();
    }

    /**
     * Annule la dernière modification apportée à la table en restaurant la table précédente de l'historique.
     */
    public static void undo() {
        future.add(currentTable.copy());
        currentTable = history.getLast();
        history.removeLast();
        updateVBoxTable();
    }

    /**
     * Rétablit la dernière modification annulée en restaurant la table suivante de l'historique.
     */
    public static void redo() {
        history.add(currentTable.copy());
        currentTable = future.getLast();
        future.removeLast();
        updateVBoxTable();
    }
}
