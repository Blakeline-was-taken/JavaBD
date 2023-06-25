package outils;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;

/**
 * La classe VisualObjectInitialiser permet de générer un formulaire adapté au type d'objet
 * que l'utilisateur souhaite créer.
 */
public class VisualObjectInitialiser {

    /**
     * Initialise un HBox avec des contrôles visuels adaptés au type d'objet spécifié.
     *
     * @param type Le type d'objet pour lequel initialiser les contrôles visuels.
     * @return Un HBox contenant les contrôles visuels adaptés au type d'objet.
     */
    public static HBox get(Class<?> type) {
        if (type.equals(Integer.class) || type.equals(Double.class) || type.equals(String.class)) {
            return new HBox(new TextField());
        } else if (type.equals(Boolean.class)) {
            RadioButton trueButton = new RadioButton("True");
            RadioButton falseButton = new RadioButton("False");
            ToggleGroup groupButton = new ToggleGroup();
            groupButton.getToggles().addAll(trueButton, falseButton);
            return new HBox(trueButton, falseButton);
        } else {
            ComboBox<String> days = new ComboBox<>();
            for (int i = 1; i < 32; i++) {
                String day = String.valueOf(i);
                if (day.length() == 1) {
                    day = "0" + day;
                }
                days.getItems().add(day);
            }
            ComboBox<String> months = new ComboBox<>();
            for (int i = 1; i < 13; i++) {
                String month = String.valueOf(i);
                if (month.length() == 1) {
                    month = "0" + month;
                }
                months.getItems().add(month);
            }
            return new HBox(days, new Label(" / "), months, new Label(" / "), new TextField());
        }
    }
}
