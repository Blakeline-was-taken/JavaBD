package vue;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modele.*;
import outils.VisualObjectInitialiser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VBoxCondition extends VBox {
    private final ArrayList<Condition> conditions;

    public VBoxCondition(DataTable table) {
        conditions = new ArrayList<>();
        GridPane gridPane = new GridPane();
        gridPane.add(addConditionButton(table), 0, 0);
        getChildren().add(gridPane);
    }

    private GridPane gridPaneConditions(DataTable table, ArrayList<Condition> parConditions){
        GridPane toReturn = new GridPane();
        int rowIndex = 0;
        for (Condition condition : parConditions){
            Button removeCondition = new Button("➖");

            removeCondition.getStyleClass().add("button-rem");
            removeCondition.setOnAction(event -> {
                parConditions.remove(condition);
                getChildren().clear();
                getChildren().add(gridPaneConditions(table, parConditions));
            });

            toReturn.add(new HBox(removeCondition, new Label(condition.toString())), 0, rowIndex);
            rowIndex++;
        }
        toReturn.add(addConditionButton(table), 0, rowIndex);
        return toReturn;
    }

    public ArrayList<Condition> getConditions(){
        return conditions;
    }
    
    private Button addConditionButton(DataTable table){
        Button addCondition = new Button("➕ Add Condition");

        addCondition.getStyleClass().add("button-add");
        addCondition.setOnAction(event -> {
            // Créer la fenêtre d'ajout de condition
            Stage conditionStage = new Stage();
            conditionStage.initModality(Modality.APPLICATION_MODAL);
            conditionStage.setTitle("Add Condition");
            conditionStage.setResizable(false);
            conditionStage.setWidth(500);

            RadioButton value = new RadioButton("Value condition");
            RadioButton column = new RadioButton("Column condition");
            value.setSelected(true);
            ToggleGroup conditionType = new ToggleGroup();
            conditionType.getToggles().addAll(value, column);

            ComboBox<String> columnName = new ComboBox<>();
            columnName.getItems().addAll(table.getColumnNames());
            ComboBox<String> operator = new ComboBox<>();
            operator.getItems().addAll(new ArrayList<>(List.of(new String[]{"=", "!=", ">", ">=", "<", "<="})));
            ComboBox<String> otherColumnName = new ComboBox<>();
            otherColumnName.getItems().addAll(table.getColumnNames());
            HBox lastField = new HBox(new TextField());

            value.setOnAction(radioEvent -> {
                lastField.getChildren().clear();
                if (columnName.getSelectionModel().getSelectedItem() == null){
                    lastField.getChildren().add(new TextField());
                } else {
                    String name = columnName.getSelectionModel().getSelectedItem();
                    Class<?> type = table.getColumn(name).getType();
                    lastField.getChildren().addAll(VisualObjectInitialiser.get(type).getChildren());
                }
            });

            column.setOnAction(radioEvent -> {
                lastField.getChildren().removeAll(lastField.getChildren());
                lastField.getChildren().add(otherColumnName);
            });

            columnName.setOnAction(comboEvent -> {
                if (conditionType.getSelectedToggle() == value){
                    String name = columnName.getSelectionModel().getSelectedItem();
                    Class<?> type = table.getColumn(name).getType();
                    lastField.getChildren().removeAll(lastField.getChildren());
                    lastField.getChildren().addAll(VisualObjectInitialiser.get(type).getChildren());
                }
            });

            Button cancelButton = new Button("Cancel");
            Button addButton = new Button("Add");

            // Gérer l'événement du bouton Cancel
            cancelButton.setOnAction(subEvent -> conditionStage.close());

            // Gérer l'événement du bouton Add
            addButton.setOnAction(subEvent -> {
                String name = columnName.getSelectionModel().getSelectedItem();
                String op = operator.getSelectionModel().getSelectedItem();
                if (name == null){
                    Alert errorDialog = new Alert(Alert.AlertType.ERROR);
                    errorDialog.setTitle("Error");
                    errorDialog.setHeaderText("You must choose a column to apply the condition to.");
                    errorDialog.getButtonTypes().setAll(ButtonType.OK);
                    errorDialog.showAndWait();
                    return;
                }
                if (op == null){
                    Alert errorDialog = new Alert(Alert.AlertType.ERROR);
                    errorDialog.setTitle("Error");
                    errorDialog.setHeaderText("You must choose an operator for the condition.");
                    errorDialog.getButtonTypes().setAll(ButtonType.OK);
                    errorDialog.showAndWait();
                    return;
                }
                Class<?> type = table.getColumn(name).getType();
                if (conditionType.getSelectedToggle() == value){
                    Object val;
                    try {
                        if (type.equals(Integer.class)) {
                            TextField field = (TextField) lastField.getChildren().get(0);
                            if (field.getText().length() > 0) {
                                val = Integer.parseInt(field.getText());
                            } else {
                                val = null;
                            }
                        } else if (type.equals(Double.class)) {
                            TextField field = (TextField) lastField.getChildren().get(0);
                            if (field.getText().length() > 0) {
                                val = Double.parseDouble(field.getText());
                            } else {
                                val = null;
                            }
                        } else if (type.equals(String.class)) {
                            TextField field = (TextField) lastField.getChildren().get(0);
                            if (field.getText().length() > 0) {
                                val = field.getText();
                            } else {
                                val = null;
                            }
                        } else if (type.equals(Boolean.class)) {
                            RadioButton trueButton = (RadioButton) lastField.getChildren().get(0);
                            RadioButton falseButton = (RadioButton) lastField.getChildren().get(1);
                            if (trueButton.isSelected() || falseButton.isSelected()) {
                                val = trueButton.isSelected();
                            } else {
                                val = null;
                            }
                        } else {
                            ComboBox<String> days = null;
                            TextField year = null;
                            String day = null;
                            String month = null;
                            for (Node node : lastField.getChildren()) {
                                if (node instanceof ComboBox) {
                                    ComboBox<String> comboBox = (ComboBox<String>) node;
                                    if (days == null) {
                                        days = comboBox;
                                        day = days.getSelectionModel().getSelectedItem();
                                    } else {
                                        month = comboBox.getSelectionModel().getSelectedItem();
                                    }
                                } else if (node instanceof TextField) {
                                    year = (TextField) node;
                                }
                            }
                            if (day != null && month != null && Objects.requireNonNull(year).getText().length() > 0) {
                                Date date = new Date(
                                        Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year.getText())
                                );
                                if (date.isValid()) {
                                    val = date;
                                } else {
                                    Alert dateDialog = new Alert(Alert.AlertType.ERROR);
                                    dateDialog.setTitle("Incorrect Date");
                                    dateDialog.setHeaderText("The date you are trying to input does not exist.");
                                    dateDialog.getButtonTypes().setAll(ButtonType.OK);
                                    dateDialog.showAndWait();
                                    return;
                                }
                            } else {
                                val = null;
                            }
                        }
                        conditions.add(new ValueCondition(name, op, val));
                        conditionStage.close();
                        getChildren().clear();
                        getChildren().add(gridPaneConditions(table, conditions));
                    } catch (Exception exception) {
                        Alert errorDialog = new Alert(Alert.AlertType.ERROR);
                        errorDialog.setTitle("Error");
                        errorDialog.setHeaderText("The values you are trying to input are incorrect.");
                        errorDialog.getButtonTypes().setAll(ButtonType.OK);
                        errorDialog.showAndWait();
                    }
                } else {
                    String otherName = otherColumnName.getSelectionModel().getSelectedItem();
                    Class<?> otherType = table.getColumn(otherName).getType();
                    if (!type.equals(otherType) &&
                            !((type.equals(Integer.class) && otherType.equals(Double.class)) ||
                              (type.equals(Double.class) && otherType.equals(Integer.class)))
                    ){
                        Alert columnDialog = new Alert(Alert.AlertType.ERROR);
                        columnDialog.setTitle("Non-matching columns");
                        columnDialog.setHeaderText("The type of these columns do not match.");
                        columnDialog.getButtonTypes().setAll(ButtonType.OK);
                        columnDialog.showAndWait();
                        return;
                    }
                    conditions.add(new ColumnCondition(name, op, otherName));
                    conditionStage.close();
                    getChildren().clear();
                    getChildren().add(gridPaneConditions(table, conditions));
                }
            });

            HBox boxBoutons = new HBox(cancelButton, addButton);

            // Créer la mise en page de la fenêtre
            VBox conditionLayout = new VBox(10);
            conditionLayout.setPadding(new Insets(10));
            conditionLayout.getChildren().addAll(new Label("Type de condition :"), new VBox(value, column),
                    new Label("Condition :"), new HBox(columnName, new Separator(Orientation.VERTICAL), operator, new Separator(Orientation.VERTICAL), lastField),
                    new Separator(), boxBoutons);

            // Créer la scène et afficher la fenêtre
            conditionStage.setScene(new Scene(conditionLayout));
            conditionStage.showAndWait();
        });
        
        return addCondition;
    }
}
