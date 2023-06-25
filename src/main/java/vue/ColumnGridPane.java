package vue;

import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;
import modele.DataTable;

import java.util.ArrayList;

/**
 * La classe ColumnGridPane représente un conteneur pour les cases à cocher associées aux colonnes d'une table de données.
 * Chaque case à cocher représente une colonne de la table.
 */
public class ColumnGridPane extends GridPane {

    private final ArrayList<CheckBox> listColumns = new ArrayList<>();

    /**
     * Constructeur de la classe ColumnGridPane.
     *
     * @param table la table de données associée
     */
    public ColumnGridPane(DataTable table) {
        int columnIndex = 0;
        int gridColIndex = 0;
        int gridRowIndex = 0;
        while (columnIndex < table.getColumns().size()) {
            if (gridRowIndex > 4) {
                gridRowIndex = 0;
                gridColIndex++;
            }
            CheckBox checkBox = new CheckBox(table.getColumnNames().get(columnIndex));
            listColumns.add(checkBox);
            add(checkBox, gridRowIndex, gridColIndex);
            gridRowIndex++;
            columnIndex++;
        }
    }

    /**
     * Retourne la liste des cases à cocher représentant les colonnes de la table.
     *
     * @return la liste des cases à cocher
     */
    public ArrayList<CheckBox> getListColumns() {
        return listColumns;
    }
}

