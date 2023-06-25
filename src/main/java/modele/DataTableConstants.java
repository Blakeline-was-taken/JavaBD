package modele;

import java.util.ArrayList;
import java.util.List;

/**
 * L'interface DataTableConstants définit les constantes utilisées pour la classe DataTable.
 * Elle contient une liste des noms de classe supportés et une liste des classes supportées.
 */
public interface DataTableConstants {

    /**
     * Liste des noms de classe supportés.
     */
    ArrayList<String> supportedClassNames = new ArrayList<>(List.of(new String[]{"Integer", "Double", "String", "Boolean", "Date"}));

    /**
     * Liste des classes supportées.
     */
    ArrayList<Class<?>> supportedClasses = new ArrayList<>(List.of(new Class<?>[]{Integer.class, Double.class, String.class, Boolean.class, Date.class}));
}
