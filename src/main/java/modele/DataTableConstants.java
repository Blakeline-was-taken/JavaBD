package modele;

import java.util.ArrayList;
import java.util.List;

public interface DataTableConstants {

    ArrayList<String> supportedClassNames = new ArrayList<>(List.of(new String[]{"Integer", "Double", "String", "Boolean", "Date"}));

    ArrayList<Class<?>> supportedClasses = new ArrayList<>(List.of(new Class<?>[]{Integer.class, Double.class, String.class, Boolean.class, Date.class}));
}
