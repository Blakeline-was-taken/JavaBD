package modele;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeSet;

/**
 * La classe DataTable représente une table de données avec des colonnes et des lignes.
 */
public class DataTable implements Serializable {
    private String name; // Le nom de la table
    private ArrayList<String> columnNames; // La liste des noms de colonnes
    private ArrayList<Column> columns; // Les colonnes de la table

    /**
     * Constructeur de la classe DataTable avec le nom spécifié.
     *
     * @param name Le nom de la table de données.
     */
    public DataTable(String name) {
        this.name = name;
        this.columnNames = new ArrayList<>();
        this.columns = new ArrayList<>();
    }

    /**
     * Constructeur de la classe DataTable avec le nom, les noms de colonnes et les colonnes spécifiés.
     *
     * @param name         Le nom de la table de données.
     * @param columnNames  Les noms des colonnes de la table de données.
     * @param columns      Les colonnes de la table de données.
     */
    public DataTable(String name, ArrayList<String> columnNames, ArrayList<Column> columns) {
        this.name = name;
        this.columnNames = columnNames;
        this.columns = columns;
    }

    /**
     * Retourne le nom de la table de données.
     *
     * @return Le nom de la table de données.
     */
    public String getName() {
        return name;
    }

    /**
     * Retourne le nom de la colonne spécifiée.
     *
     * @param column La colonne dont on veut obtenir le nom.
     * @return Le nom de la colonne spécifiée.
     */
    public String getColumnName(Column column) {
        return columnNames.get(columns.indexOf(column));
    }

    /**
     * Retourne le nom de la colonne à l'index spécifié.
     *
     * @param index L'index de la colonne dont on veut obtenir le nom.
     * @return Le nom de la colonne à l'index spécifié.
     */
    public String getColumnName(int index) {
        return columnNames.get(index);
    }

    /**
     * Retourne la liste des noms de colonnes de la table de données.
     *
     * @return La liste des noms de colonnes.
     */
    public ArrayList<String> getColumnNames() {
        return columnNames;
    }

    /**
     * Retourne la colonne correspondant au nom spécifié.
     *
     * @param columnName Le nom de la colonne.
     * @return La colonne correspondant au nom spécifié.
     */
    public Column getColumn(String columnName) {
        return columns.get(columnNames.indexOf(columnName));
    }

    /**
     * Retourne la colonne à l'index spécifié.
     *
     * @param index L'index de la colonne.
     * @return La colonne à l'index spécifié.
     */
    public Column getColumn(int index) {
        return columns.get(index);
    }

    /**
     * Retourne la liste des colonnes de la table de données.
     *
     * @return La liste des colonnes.
     */
    public ArrayList<Column> getColumns() {
        return columns;
    }

    /**
     * Retourne le nombre de colonnes de la table de données.
     *
     * @return Le nombre de colonnes.
     */
    public int getNumberOfColumns() {
        return columns.size();
    }

    /**
     * Retourne le nombre de lignes de la table de données.
     *
     * @return Le nombre de lignes.
     */
    public int getNumberOfRows() {
        if (columns.size() == 0) {
            return 0;
        }
        int maxHeight = 0;
        for (Column col : columns) {
            if (col.size() > maxHeight) {
                maxHeight = col.size();
            }
        }
        return maxHeight;
    }

    /**
     * Retourne la ligne à l'index spécifié.
     *
     * @param rowIndex L'index de la ligne.
     * @return La liste des valeurs de la ligne.
     */
    public ArrayList<Object> getRow(int rowIndex) {
        ArrayList<Object> row = new ArrayList<>();
        if (rowIndex < getNumberOfRows()) {
            for (Column col : columns) {
                row.add(col.getValue(rowIndex));
            }
        }
        return row;
    }

    /**
     * Retourne la liste des lignes de la table de données.
     *
     * @return La liste des lignes.
     */
    public ArrayList<ArrayList<Object>> getRows() {
        ArrayList<ArrayList<Object>> rows = new ArrayList<>();
        for (int i = 0; i < getNumberOfRows(); i++) {
            rows.add(getRow(i));
        }
        return rows;
    }

    /**
     * Ajoute une nouvelle colonne à la table de données avec le nom et le type spécifiés.
     *
     * @param columnName Le nom de la nouvelle colonne.
     * @param type       Le type de données de la nouvelle colonne.
     */
    public void addColumn(String columnName, Class<?> type) {
        Column column = new Column(type);
        columnNames.add(columnName);
        columns.add(column);
    }

    /**
     * Insère une nouvelle ligne dans la table de données avec les valeurs spécifiées.
     *
     * @param objects Les valeurs des colonnes pour la nouvelle ligne.
     * @throws IllegalArgumentException Si le nombre de valeurs ne correspond pas au nombre de colonnes.
     */
    public void insertRow(Object... objects) throws IllegalArgumentException {
        if (objects.length != getNumberOfColumns()) {
            throw new IllegalArgumentException("Le nombre de valeurs ne correspond pas au nombre de colonnes.");
        }

        ArrayList<Column> updatedColumns = new ArrayList<>(columns);
        for (int i = 0; i < objects.length; i++) {
            Object value = objects[i];
            updatedColumns.get(i).addValue(value);
        }
        columns = updatedColumns;
    }

    /**
     * Modifie le contenu de la cellule à l'index de colonne et l'index de ligne spécifiés.
     *
     * @param columnIndex L'index de la colonne de la cellule.
     * @param rowIndex    L'index de la ligne de la cellule.
     * @param value       La nouvelle valeur à assigner à la cellule.
     * @throws IndexOutOfBoundsException Si l'index de colonne ou l'index de ligne spécifié est invalide.
     * @throws IllegalArgumentException Si le type de valeur est invalide pour la colonne.
     */
    public void setCell(int columnIndex, int rowIndex, Object value) throws IndexOutOfBoundsException, IllegalArgumentException {
        if (columnIndex < 0 || columnIndex >= getNumberOfColumns()) {
            throw new IndexOutOfBoundsException("L'index de colonne spécifié est invalide.");
        }
        if (rowIndex < 0 || rowIndex >= getNumberOfRows()) {
            throw new IndexOutOfBoundsException("L'index de ligne spécifié est invalide.");
        }

        Column column = columns.get(columnIndex);
        Class<?> type = column.getType();
        if (!type.isInstance(value) && value != null) {
            throw new IllegalArgumentException("Type de valeur invalide. Attendu : " + type.getSimpleName());
        }

        column.setValue(rowIndex, value);
    }

    /**
     * Modifie le contenu de la cellule dans la colonne spécifiée et à l'index de ligne spécifié.
     *
     * @param columnName Le nom de la colonne de la cellule.
     * @param rowIndex   L'index de la ligne de la cellule.
     * @param value      La nouvelle valeur à assigner à la cellule.
     * @throws IllegalArgumentException Si la colonne spécifiée n'existe pas ou si l'index de ligne spécifié est invalide.
     * @throws IllegalArgumentException Si le type de valeur est invalide pour la colonne.
     */
    public void setCell(String columnName, int rowIndex, Object value) throws IllegalArgumentException {
        int columnIndex = columnNames.indexOf(columnName);
        if (columnIndex == -1) {
            throw new IllegalArgumentException("La colonne spécifiée n'existe pas.");
        }
        setCell(columnIndex, rowIndex, value);
    }

    /**
     * Supprime la colonne à l'index spécifié de la table de données.
     *
     * @param index L'index de la colonne à supprimer.
     * @throws IndexOutOfBoundsException Si l'index est en dehors des limites de la table de données.
     */
    public void removeColumn(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= columns.size()) {
            throw new IndexOutOfBoundsException("L'index est en dehors des limites de la table de données.");
        }
        columnNames.remove(index);
        columns.remove(index);
    }

    /**
     * Supprime une colonne de la table de données.
     *
     * @param columnName Le nom de la colonne à supprimer.
     * @throws IllegalArgumentException Si la colonne spécifiée n'existe pas.
     */
    public void removeColumn(String columnName) throws IllegalArgumentException {
        int columnIndex = columnNames.indexOf(columnName);
        if (columnIndex == -1) {
            throw new IllegalArgumentException("La colonne spécifiée n'existe pas.");
        }
        columnNames.remove(columnIndex);
        columns.remove(columnIndex);
    }

    /**
     * Supprime une ligne de la table de données.
     *
     * @param rowIndex L'index de la ligne à supprimer.
     * @throws IllegalArgumentException Si l'index de ligne spécifié est invalide.
     */
    public void removeRow(int rowIndex) throws IllegalArgumentException {
        if (rowIndex < 0 || rowIndex >= getNumberOfRows()) {
            throw new IllegalArgumentException("L'index de ligne spécifié est invalide.");
        }
        for (Column column : columns) {
            if (column.size() >= rowIndex) {
                column.removeValue(rowIndex);
            }
        }
    }

    /**
     * Supprime la cellule à l'index spécifié dans la table de données.
     *
     * @param columnIndex L'index de la colonne de la cellule à supprimer.
     * @param rowIndex    L'index de la ligne de la cellule à supprimer.
     * @throws IndexOutOfBoundsException Si l'index de colonne ou l'index de ligne spécifié est invalide.
     */
    public void removeCell(int columnIndex, int rowIndex) throws IndexOutOfBoundsException {
        if (columnIndex < 0 || columnIndex >= getNumberOfColumns()) {
            throw new IndexOutOfBoundsException("L'index de colonne spécifié est invalide.");
        }
        if (rowIndex < 0 || rowIndex >= getNumberOfRows()) {
            throw new IndexOutOfBoundsException("L'index de ligne spécifié est invalide.");
        }

        Column column = columns.get(columnIndex);
        if (rowIndex < column.size() - 1) {
            column.setValue(rowIndex, null);
        } else {
            column.removeValue(rowIndex);
        }
    }

    /**
     * Supprime la cellule de la colonne spécifiée et à l'index de ligne spécifié dans la table de données.
     *
     * @param columnName Le nom de la colonne de la cellule à supprimer.
     * @param rowIndex   L'index de la ligne de la cellule à supprimer.
     * @throws IllegalArgumentException Si la colonne spécifiée n'existe pas ou si l'index de ligne spécifié est invalide.
     */
    public void removeCell(String columnName, int rowIndex) throws IllegalArgumentException {
        int columnIndex = columnNames.indexOf(columnName);
        if (columnIndex == -1) {
            throw new IllegalArgumentException("La colonne spécifiée n'existe pas.");
        }
        removeCell(columnIndex, rowIndex);
    }

    public DataTable copy(){
        ArrayList<Column> copyColumns = new ArrayList<>();
        for (Column col : columns){
            copyColumns.add(new Column(col.getType(), col.getValues()));
        }
        return new DataTable(name, columnNames, copyColumns);
    }

    /**
     * Affiche la table de données dans la console.
     */
    public void printTable() {
        int[] columnWidths = getColumnWidths();

        // Affiche le nom de la table avec un espacement
        int totalTableWidth = 0;
        for (int width : columnWidths) {
            totalTableWidth += width + 3; // Ajoute 3 pour les deux espaces et le séparateur
        }
        printSpaces((totalTableWidth - name.length() - 6) / 2); // Soustrait 6 pour "TABLE " et convertit en espacement
        System.out.println("TABLE " + name.toUpperCase());

        // Affiche les noms de colonnes
        System.out.print("| ");
        for (int i = 0; i < getNumberOfColumns(); i++) {
            String columnName = getColumnName(i);
            System.out.print(columnName);
            printSpaces(columnWidths[i] - columnName.length());
            System.out.print(" | ");
        }
        System.out.println();

        // Affiche la ligne de séparation
        System.out.print("-");
        for (int i = 0; i < totalTableWidth; i++) {
            System.out.print("-");
        }
        System.out.println();

        // Affiche les lignes
        int numRows = getNumberOfRows();
        for (int i = 0; i < numRows; i++) {
            System.out.print("| ");
            for (int j = 0; j < getNumberOfColumns(); j++) {
                Object value = (i < getColumn(j).size()) ? getColumn(j).getValue(i) : null;

                if (value != null) {
                    System.out.print(value);
                    printSpaces(columnWidths[j] - value.toString().length());
                } else {
                    printSpaces(columnWidths[j]);
                }
                System.out.print(" | ");
            }
            System.out.println();
        }
    }

    /**
     * Retourne les largeurs des colonnes de la table.
     *
     * @return Un tableau contenant les largeurs des colonnes.
     */
    private int[] getColumnWidths() {
        int numColumns = getNumberOfColumns();
        int[] columnWidths = new int[numColumns];
        for (int i = 0; i < numColumns; i++) {
            int maxColumnWidth = getColumnName(i).length();
            for (Object value : getColumn(i).getValues()) {
                if (value != null) {
                    int cellWidth = value.toString().length();
                    if (cellWidth > maxColumnWidth) {
                        maxColumnWidth = cellWidth;
                    }
                }
            }
            columnWidths[i] = maxColumnWidth;
        }
        return columnWidths;
    }

    /**
     * Affiche un certain nombre d'espaces dans la console.
     *
     * @param numSpaces Le nombre d'espaces à afficher.
     */
    private void printSpaces(int numSpaces) {
        for (int i = 0; i < numSpaces; i++) {
            System.out.print(" ");
        }
    }

    /**
     * Effectue une opération de sélection (SELECT) sur la table de données.
     *
     * @param fields      Les champs à sélectionner.
     * @param conditions  Les conditions à appliquer.
     * @return La nouvelle table résultant de l'opération SELECT.
     */
    public DataTable select(TreeSet<String> fields, Condition... conditions) {
        DataTable resultTable = this;
        for (Condition condition : conditions) {
            // Obtenir les indices des lignes qui satisfont les conditions
            ArrayList<Integer> selectedRows = condition.evaluateCondition(resultTable);

            // Créer la nouvelle table résultante du select
            ArrayList<Column> resultColumns = new ArrayList<>();
            for (Column column : resultTable.getColumns()) {
                resultColumns.add(new Column(column.getType()));
            }

            resultTable = new DataTable(name, columnNames, resultColumns);

            // Copier les lignes sélectionnées dans la nouvelle table
            for (Integer rowIndex : selectedRows) {
                ArrayList<Object> row = getRow(rowIndex);
                for (int columnIndex = 0; columnIndex < row.size(); columnIndex++) {
                    resultTable.getColumn(columnIndex).addValue(row.get(columnIndex));
                }
            }
        }

        ArrayList<String> resultColumnNames = new ArrayList<>(resultTable.getColumnNames());
        ArrayList<Column> resultColumns = new ArrayList<>(resultTable.getColumns());
        // Ne garder que les colonnes voulues
        if (fields.size() > 0 && !fields.contains("*")) {
            int i = 0;
            while (i < resultColumnNames.size()) {
                if (!fields.contains(resultColumnNames.get(i))) {
                    resultColumnNames.remove(i);
                    resultColumns.remove(i);
                } else {
                    i++;
                }
            }
        }

        return new DataTable(name, resultColumnNames, resultColumns);
    }

    /**
     * Effectue une opération JOIN entre cette table et une autre table spécifiée, en utilisant les colonnes spécifiées
     * pour la jointure.
     *
     * @param table   La table avec laquelle effectuer la jointure.
     * @param column1 Le nom de la colonne de cette table à utiliser pour la jointure.
     * @param column2 Le nom de la colonne de l'autre table à utiliser pour la jointure.
     * @return Une nouvelle table résultante de la jointure.
     * @throws IllegalArgumentException Si les colonnes spécifiées ne sont pas présentes dans les tables respectives,
     *                                  ou si les types des colonnes ne correspondent pas.
     */
    public DataTable join(DataTable table, String column1, String column2) throws IllegalArgumentException {
        // Vérifier si les colonnes spécifiées sont présentes dans les tables
        if (!columnNames.contains(column1) || !table.columnNames.contains(column2)) {
            throw new IllegalArgumentException("Les colonnes spécifiées ne sont pas présentes dans les tables.");
        }

        // Obtenir l'index des colonnes spécifiées
        int index1 = columnNames.indexOf(column1);
        int index2 = table.columnNames.indexOf(column2);

        // Vérifier si les types des colonnes sont les mêmes
        if (!getColumn(index1).getType().equals(table.getColumn(index2).getType())) {
            throw new IllegalArgumentException("Les types des colonnes spécifiées ne correspondent pas.");
        }

        // Créer les ensembles de colonnes pour la nouvelle table résultante du join
        ArrayList<String> resultColumnNames = new ArrayList<>(columnNames);
        ArrayList<Column> resultColumns = new ArrayList<>(columns);
        for (int i = 0; i < table.getNumberOfColumns(); i++) {
            String columnName = table.getColumnName(i);
            if (!columnName.equals(column2)) {
                Class<?> columnType = table.getColumn(i).getType();
                resultColumnNames.add(columnName);
                resultColumns.add(new Column(columnType));
            }
        }

        // Créer la nouvelle table résultante du join
        DataTable resultTable = new DataTable(name + "_" + table.name, resultColumnNames, resultColumns);

        // Parcourir les lignes des deux tables et effectuer le join
        for (int i = 0; i < getNumberOfRows(); i++) {
            Object value1 = getColumn(index1).getValue(i);
            int rowIndex = table.getColumn(index2).getValues().indexOf(value1);
            if (rowIndex != -1) {
                // Les valeurs correspondent, ajouter la ligne à la nouvelle table
                for (int j = 0; j < table.getNumberOfColumns(); j++) {
                    if (j != index2) {
                        int indResultCol = resultTable.getColumnNames().indexOf(table.getColumnName(j));
                        resultTable.getColumn(indResultCol).addValue(table.getColumn(j).getValue(rowIndex));
                    }
                }
            }
        }

        return resultTable;
    }
}