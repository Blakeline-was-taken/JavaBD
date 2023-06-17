package modele;

import java.util.ArrayList;

/**
 * La classe Column représente une colonne de données dans une DataTable.
 * Elle est caractérisée par un type de données et une liste de valeurs.
 */
public class Column {
    private Class<?> type; // Le type de données de la colonne
    private ArrayList<Object> values; // La liste des valeurs de la colonne

    /**
     * Constructeur de la classe Column.
     *
     * @param type le type de données de la colonne
     */
    public Column(Class<?> type) {
        this.type = type;
        this.values = new ArrayList<>();
    }

    /**
     * Retourne le type de données de la colonne.
     *
     * @return le type de données de la colonne
     */
    public Class<?> getType() {
        return type;
    }

    /**
     * Ajoute une valeur à la colonne.
     *
     * @param value la valeur à ajouter
     * @throws IllegalArgumentException si le type de valeur est invalide pour la colonne
     */
    public void addValue(Object value) throws IllegalArgumentException {
        if (!type.isInstance(value)) {
            throw new IllegalArgumentException("Type de valeur invalide. Attendu : " + type.getSimpleName());
        }
        values.add(value);
    }

    /**
     * Retourne le nombre de valeurs dans la colonne.
     *
     * @return le nombre de valeurs dans la colonne
     */
    public int size() {
        return getValues().size();
    }

    /**
     * Retourne la liste des valeurs de la colonne.
     *
     * @return la liste des valeurs de la colonne
     */
    public ArrayList<Object> getValues() {
        return values;
    }

    /**
     * Retourne la valeur à l'index spécifié dans la colonne.
     *
     * @param index l'index de la valeur à récupérer
     * @return la valeur à l'index spécifié dans la colonne
     */
    public Object getValue(int index) {
        return values.get(index);
    }
}
