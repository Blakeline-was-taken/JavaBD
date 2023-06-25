package controleur;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modele.Column;
import modele.DataTable;
import modele.Date;
import outils.VisualObjectInitialiser;
import vue.HBoxMain;
import vue.VBoxTable;

import java.util.Objects;

public class CellController implements EventHandler<MouseEvent> {
    @Override
    public void handle(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) {
            try {
                TableView<Object[]> tableView = VBoxTable.tableView;
                DataTable table = VBoxTable.table;

                // Récupérer la cellule sur laquelle le clic a été effectué
                TablePosition<?, ?> cell = tableView.getSelectionModel().getSelectedCells().get(0);

                // Récupérer la valeur de la cellule
                Object value = tableView.getItems().get(cell.getRow())[cell.getColumn()];

                // Créer la fenêtre de modification
                Stage editStage = new Stage();
                editStage.initModality(Modality.APPLICATION_MODAL);
                editStage.setTitle("Modify Value");
                editStage.setResizable(false);

                // Créer les éléments de la fenêtre
                String columnName = tableView.getColumns().get(cell.getColumn()).getText();
                Label columnNameLabel = new Label(columnName + " - Row " + (cell.getRow() + 1));
                Label valueLabel = new Label("Value : " + value);

                Column column = table.getColumn(columnName);
                Class<?> columnType = column.getType();
                HBox newValueField = VisualObjectInitialiser.get(columnType);

                Button cancelButton = new Button("Cancel");
                Button applyButton = new Button("Apply");

                // Gérer l'événement du bouton Cancel
                cancelButton.setOnAction(subEvent -> editStage.close());

                // Gérer l'événement du bouton Apply
                applyButton.setOnAction(subEvent -> {
                    try {
                        if (cell.getRow() >= column.size()){
                            for (int i = column.size(); i <= cell.getRow(); i++){
                                column.addValue(null);
                            }
                        }
                        if (columnType.equals(Integer.class)) {
                            TextField field = (TextField) newValueField.getChildren().get(0);
                            if (field.getText().length() > 0) {
                                column.setValue(cell.getRow(), Integer.parseInt(field.getText()));
                            } else {
                                column.setValue(cell.getRow(), null);
                            }
                        } else if (columnType.equals(Double.class)) {
                            TextField field = (TextField) newValueField.getChildren().get(0);
                            if (field.getText().length() > 0) {
                                column.setValue(cell.getRow(), Double.parseDouble(field.getText()));
                            } else {
                                column.setValue(cell.getRow(), null);
                            }
                        } else if (columnType.equals(String.class)) {
                            TextField field = (TextField) newValueField.getChildren().get(0);
                            if (field.getText().length() > 0) {
                                column.setValue(cell.getRow(), field.getText());
                            } else {
                                column.setValue(cell.getRow(), null);
                            }
                        } else if (columnType.equals(Boolean.class)) {
                            RadioButton trueButton = (RadioButton) newValueField.getChildren().get(0);
                            RadioButton falseButton = (RadioButton) newValueField.getChildren().get(1);
                            if (trueButton.isSelected()) {
                                column.setValue(cell.getRow(), true);
                            } else if (falseButton.isSelected()) {
                                column.setValue(cell.getRow(), false);
                            } else {
                                column.setValue(cell.getRow(), null);
                            }
                        } else {
                            ComboBox<String> days = null;
                            TextField year = null;
                            String day = null;
                            String month = null;
                            for (Node node : newValueField.getChildren()) {
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
                                if (date.isValid()){
                                    column.setValue(cell.getRow(), date);
                                } else {
                                    Alert dateDialog = new Alert(Alert.AlertType.ERROR);
                                    dateDialog.setTitle("Incorrect Date");
                                    dateDialog.setHeaderText("The date you are trying to input does not exist.");
                                    dateDialog.getButtonTypes().setAll(ButtonType.OK);
                                    dateDialog.showAndWait();
                                    return;
                                }
                            } else {
                                column.setValue(cell.getRow(), null);
                            }
                        }
                        editStage.close();
                        HBoxMain.updateTable(table);
                    } catch (Exception exception) {
                        Alert errorDialog = new Alert(Alert.AlertType.ERROR);
                        errorDialog.setTitle("Error");
                        errorDialog.setHeaderText("The values you are trying to input are incorrect.");
                        errorDialog.getButtonTypes().setAll(ButtonType.OK);
                        errorDialog.showAndWait();
                    }
                });

                HBox boxBoutons = new HBox(cancelButton, applyButton);

                // Créer la mise en page de la fenêtre
                VBox editLayout = new VBox(10);
                editLayout.setPadding(new Insets(10));
                editLayout.getChildren().addAll(columnNameLabel, valueLabel, new Separator(),
                        new Label("New value :"), newValueField, boxBoutons);

                // Créer la scène et afficher la fenêtre
                editStage.setScene(new Scene(editLayout));
                editStage.showAndWait();

            } catch (IndexOutOfBoundsException ignored) {}
        }
    }
}
