package modele;

import java.util.ArrayList;

/**
 * La classe ValueCondition représente une condition basée sur une valeur précise.
 * Elle hérite de la classe abstraite Condition.
 */
public class ValueCondition extends Condition {

    /**
     * Constructeur de la classe ValueCondition.
     *
     * @param columnName le nom de la colonne sur laquelle appliquer la condition
     * @param operator   l'opérateur de comparaison
     * @param value      la valeur avec laquelle comparer les cellules de la colonne
     */
    public ValueCondition(String columnName, String operator, Object value) {
        super(columnName, operator, value);
    }

    /**
     * Évalue la condition pour une table de données donnée.
     *
     * @param table la table de données sur laquelle appliquer la condition
     * @return une liste d'entiers représentant les indices des lignes satisfaisant la condition
     */
    @Override
    public ArrayList<Integer> evaluateCondition(DataTable table) {
        ArrayList<Integer> selectedRows = new ArrayList<>();
        int numRows = table.getNumberOfRows();

        // Obtenir l'indice de la colonne spécifiée dans la table
        int columnIndex = table.getColumnNames().indexOf(columnName);

        // Parcourir les lignes et évaluer la condition
        for (int rowIndex = 0; rowIndex < numRows; rowIndex++) {
            Object cellValue = table.getColumns().get(columnIndex).getValues().get(rowIndex);

            // Vérifier si la condition est satisfaite
            if (evaluate(cellValue, operator, value)) {
                selectedRows.add(rowIndex);
            }
        }

        return selectedRows;
    }
}
