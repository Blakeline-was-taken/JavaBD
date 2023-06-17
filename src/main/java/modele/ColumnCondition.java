package modele;

import java.util.ArrayList;

/**
 * La classe ColumnCondition représente une condition utilisée pour évaluer les données d'une DataTable
 * en comparant les valeurs de deux colonnes.
 * Une condition de colonne est composée de deux noms de colonnes et d'un opérateur de comparaison.
 */
public class ColumnCondition extends Condition {

    /**
     * Constructeur de la classe ColumnCondition.
     *
     * @param columnName1 le nom de la première colonne
     * @param operator    l'opérateur de comparaison
     * @param columnName2 le nom de la deuxième colonne
     */
    public ColumnCondition(String columnName1, String operator, String columnName2) {
        super(columnName1, operator, columnName2);
    }

    /**
     * Évalue la condition pour une DataTable donnée en comparant les valeurs des colonnes spécifiées.
     * Renvoie les indices des lignes qui satisfont la condition.
     *
     * @param table la DataTable à évaluer
     * @return une liste d'entiers représentant les indices des lignes qui satisfont la condition
     */
    @Override
    public ArrayList<Integer> evaluateCondition(DataTable table) {
        ArrayList<Integer> selectedRows = new ArrayList<>();
        int numRows = table.getNumberOfRows();

        // Obtenir les indices des colonnes spécifiées
        int columnIndex1 = table.getColumnNames().indexOf(columnName);
        int columnIndex2 = table.getColumnNames().indexOf(value);

        // Parcourir les lignes et évaluer la condition
        for (int rowIndex = 0; rowIndex < numRows; rowIndex++) {
            Object cellValue1 = table.getColumns().get(columnIndex1).getValues().get(rowIndex);
            Object cellValue2 = table.getColumns().get(columnIndex2).getValues().get(rowIndex);

            // Vérifier si la condition est satisfaite
            if (evaluate(cellValue1, operator, cellValue2)) {
                selectedRows.add(rowIndex);
            }
        }

        return selectedRows;
    }
}
