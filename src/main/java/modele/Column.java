package modele;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * La classe Column représente une colonne de données dans une DataTable.
 * Elle est caractérisée par un type de données et une liste de valeurs.
 */
public class Column implements Serializable {
    private final Class<?> type; // Le type de données de la colonne
    private final ArrayList<Object> values; // La liste des valeurs de la colonne

    /**
     * Constructeur de la classe Column.
     *
     * @param type le type de données de la colonne
     */
    public Column(Class<?> type) {
        this.type = type;
        this.values = new ArrayList<>();
    }

    public Column(Class<?> type, ArrayList<Object> values){
        this.type = type;
        this.values = new ArrayList<>();
        for (Object val : values){
            addValue(val);
        }
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

    /**
     * Ajoute une valeur à la colonne.
     *
     * @param value la valeur à ajouter
     * @throws IllegalArgumentException si le type de valeur est invalide pour la colonne
     */
    public void addValue(Object value) throws IllegalArgumentException {
        if (!type.isInstance(value) && value != null) {
            throw new IllegalArgumentException("Type de valeur invalide. Attendu : " + type.getSimpleName());
        }
        values.add(value);
    }

    /**
     * Remplace la valeur à l'index spécifié par une nouvelle valeur.
     *
     * @param index l'index de la valeur à remplacer
     * @param value la nouvelle valeur à assigner
     * @throws IllegalArgumentException si le type de valeur est invalide pour la colonne
     * @throws IndexOutOfBoundsException si l'index est en dehors des limites de la colonne
     */
    public void setValue(int index, Object value) throws IllegalArgumentException, IndexOutOfBoundsException {
        if (index < 0 || index >= values.size()) {
            throw new IndexOutOfBoundsException("L'index est en dehors des limites de la colonne.");
        }
        if (!type.isInstance(value) && value != null) {
            throw new IllegalArgumentException("Type de valeur invalide. Attendu : " + type.getSimpleName());
        }
        values.set(index, value);
    }

    /**
     * Ajoute plusieurs valeurs à la colonne.
     *
     * @param values les valeurs à ajouter
     * @throws IllegalArgumentException si le type des valeurs est invalide pour la colonne
     */
    public void addValues(Object... values) throws IllegalArgumentException {
        for (Object val : values){
            addValue(val);
        }
    }

    /**
     * Ajoute une liste de valeurs à la colonne.
     *
     * @param values la liste des valeurs à ajouter
     * @throws IllegalArgumentException si le type des valeurs est invalide pour la colonne
     */
    public void addValues(ArrayList<Object> values) throws IllegalArgumentException {
        for (Object val : values) {
            addValue(val);
        }
    }

    /**
     * Supprime la valeur à l'index spécifié de la colonne.
     *
     * @param index L'index de la valeur à supprimer.
     * @throws IndexOutOfBoundsException Si l'index est en dehors des limites de la colonne.
     */
    public void removeValue(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= values.size()) {
            throw new IndexOutOfBoundsException("L'index est en dehors des limites de la colonne.");
        }
        values.remove(index);
    }

    /**
     * Supprime toutes les valeurs de la colonne, la vidant complètement.
     */
    public void clear(){
        values.clear();
    }

    /**
     * Retourne le nombre de valeurs dans la colonne.
     *
     * @return le nombre de valeurs dans la colonne
     */
    public int size() {
        return getValues().size();
    }
}
