package modele;

import java.util.ArrayList;

/**
 * La classe abstraite Condition représente une condition utilisée pour évaluer les données d'une DataTable.
 * Une condition est composée d'une colonne, d'un opérateur et d'une valeur à comparer.
 */
public abstract class Condition {

    protected String columnName; // Le nom de la colonne sur laquelle s'applique la condition
    protected String operator; // L'opérateur de comparaison utilisé dans la condition
    protected Object value; // La valeur avec laquelle comparer les données de la colonne

    /**
     * Constructeur de la classe Condition.
     *
     * @param columnName le nom de la colonne
     * @param operator   l'opérateur de comparaison
     * @param value      la valeur à comparer
     */
    public Condition(String columnName, String operator, Object value) {
        this.columnName = columnName;
        this.operator = operator;
        this.value = value;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères de l'objet Condition.
     *
     * @return une chaîne de caractères représentant la condition au format "columnName operator value"
     */
    public String toString(){
        return this.columnName + " " + this.operator + " " + this.value;
    }

    /**
     * Évalue la condition pour une DataTable donnée et renvoie les indices des lignes qui satisfont la condition.
     *
     * @param table la DataTable à évaluer
     * @return une liste d'entiers représentant les indices des lignes qui satisfont la condition
     */
    public abstract ArrayList<Integer> evaluateCondition(DataTable table);

    /**
     * Évalue la condition en comparant une valeur de cellule avec la valeur spécifiée selon l'opérateur de comparaison.
     *
     * @param cellValue la valeur de cellule à comparer
     * @param operator  l'opérateur de comparaison
     * @param value     la valeur spécifiée pour la comparaison
     * @return true si la condition est satisfaite, false sinon
     * @throws IllegalArgumentException si l'opérateur de condition est invalide
     */
    protected boolean evaluate(Object cellValue, String operator, Object value) {
        return switch (operator) {
            case "=" -> cellValue.equals(value);
            case "!=" -> !cellValue.equals(value);
            case ">" -> compareValues(cellValue, value) > 0;
            case ">=" -> compareValues(cellValue, value) >= 0;
            case "<" -> compareValues(cellValue, value) < 0;
            case "<=" -> compareValues(cellValue, value) <= 0;
            default -> throw new IllegalArgumentException("Opérateur de condition invalide : " + operator);
        };
    }

    /**
     * Compare deux valeurs pour déterminer leur ordre.
     *
     * @param value1 la première valeur à comparer
     * @param value2 la deuxième valeur à comparer
     * @return un entier négatif si value1 est inférieure à value2, 0 si elles sont égales,
     *         un entier positif si value1 est supérieure à value2
     * @throws IllegalArgumentException si les valeurs ne sont pas comparables
     */
    protected int compareValues(Object value1, Object value2) {
        if (value1 instanceof Comparable && value2 instanceof Comparable) {
            return ((Comparable) value1).compareTo(value2);
        }

        throw new IllegalArgumentException("Impossible de comparer les valeurs : " + value1 + " et " + value2);
    }
}
