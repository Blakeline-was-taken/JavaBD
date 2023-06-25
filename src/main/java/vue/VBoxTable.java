package vue;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import modele.DataTable;

/**
 * La classe VBoxTable est une classe JavaFX qui représente le conteneur VBox d'une table dans l'interface utilisateur.
 * Elle affiche les informations de la table, ainsi que la vue tabulaire de la table.
 */
public class VBoxTable extends VBox {

    /**
     * La vue tabulaire de la table.
     */
    public static DataTableView tableView;

    /**
     * La table de données à afficher.
     */
    public static DataTable table;

    /**
     * Constructeur de la classe VBoxTable.
     * Crée le conteneur VBox pour la table spécifiée.
     *
     * @param givenTable La table de données à afficher.
     */
    public VBoxTable(DataTable givenTable) {
        table = givenTable;

        GridPane fullTable = new GridPane();
        Button nameButton = new Button(table.getName());
        nameButton.setUserData("Name");
        nameButton.setOnAction(VBoxRoot.tableController);
        nameButton.getStyleClass().add("button-oth");

        tableView = new DataTableView(table);
        tableView.setOnMouseClicked(VBoxRoot.cellController);

        fullTable.add(new Label("Table Name (click to change) : "), 0, 0);
        fullTable.add(nameButton, 1, 0);
        fullTable.add(tableView, 0, 1, 2, 1);
        fullTable.add(new Label("Number of rows : " + table.getNumberOfRows()), 0, 2);
        fullTable.add(new Label("Number of columns : " + table.getNumberOfColumns()), 1, 2);

        getChildren().addAll(fullTable);
    }
}
