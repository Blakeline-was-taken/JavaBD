package controleur;

import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modele.*;
import outils.VisualObjectInitialiser;
import vue.ColumnGridPane;
import vue.VBoxCondition;
import vue.HBoxMain;
import vue.VBoxTable;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Cette classe est le contrôleur de la table de données.
 * Elle implémente l'interface EventHandler ActionEvent pour gérer les événements des boutons.
 */
public class TableController implements EventHandler<ActionEvent>, DataTableConstants {

    /**
     * Cette méthode gère les événements déclenchés par les boutons.
     *
     * @param actionEvent l'événement déclenché
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        switch ((String) btn.getUserData()){
            case "Columns" -> {
                // Créer la fenêtre de colonne
                Stage columnStage = new Stage();
                columnStage.initModality(Modality.APPLICATION_MODAL);
                columnStage.setResizable(false);

                switch (btn.getText()){
                    case "➕" -> {
                        // Ajouter une colonne
                        columnStage.setTitle("Add Column");

                        TextField nameField = new TextField();
                        ComboBox<String> typeComboBox = new ComboBox<>();
                        typeComboBox.getItems().addAll(supportedClassNames);
                        typeComboBox.getSelectionModel().select(0);

                        Button cancelButton = new Button("Cancel");
                        Button addButton = new Button("Add");

                        // Gérer l'événement du bouton Cancel
                        cancelButton.setOnAction(subEvent -> columnStage.close());

                        // Gérer l'événement du bouton Add
                        addButton.setOnAction(subEvent -> {
                            if (nameField.getText().length() > 0){
                                DataTable table = VBoxTable.table;
                                String typeName = typeComboBox.getSelectionModel().getSelectedItem();
                                Class<?> type = supportedClasses.get(supportedClassNames.indexOf(typeName));
                                table.addColumn(nameField.getText(), type);
                                HBoxMain.updateTable(table);
                                columnStage.close();
                            } else {
                                Alert dateDialog = new Alert(Alert.AlertType.ERROR);
                                dateDialog.setTitle("Invalid Name");
                                dateDialog.setHeaderText("The column's name cannot be empty.");
                                dateDialog.getButtonTypes().setAll(ButtonType.OK);
                                dateDialog.showAndWait();
                            }
                        });

                        HBox boxBoutons = new HBox(cancelButton, addButton);

                        // Créer la mise en page de la fenêtre
                        VBox nameLayout = new VBox(10);
                        nameLayout.setPadding(new Insets(10));
                        nameLayout.getChildren().addAll(new Label("New column's name : "), nameField,
                                new HBox(new Label("Column type : "), typeComboBox), new Separator(), boxBoutons);

                        // Créer la scène et afficher la fenêtre
                        columnStage.setScene(new Scene(nameLayout));
                        columnStage.showAndWait();
                    }
                    case "➖" -> {
                        // Retirer une colonne
                        if (VBoxTable.table.getColumns().size() == 0){
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Cannot delete");
                            alert.setHeaderText(null);
                            alert.setContentText("You don't have any columns to delete.");
                            alert.showAndWait();
                        } else {
                            DataTable table = VBoxTable.table;

                            // Créer la fenêtre de suppression de colonnes
                            Stage deleteStage = new Stage();
                            deleteStage.initModality(Modality.APPLICATION_MODAL);
                            deleteStage.setTitle("Delete Column");
                            deleteStage.setResizable(false);

                            // Initialisation de la GridPane
                            ColumnGridPane columnsPane = new ColumnGridPane(table);

                            Button cancelButton = new Button("Cancel");
                            Button deleteButton = new Button("Delete");

                            // Gérer l'événement du bouton Cancel
                            cancelButton.setOnAction(subEvent -> deleteStage.close());

                            // Gérer l'événement du bouton Delete
                            deleteButton.setOnAction(subEvent -> {
                                for (CheckBox box : columnsPane.getListColumns()){
                                    if (box.isSelected()){
                                        table.removeColumn(box.getText());
                                    }
                                }
                                deleteStage.close();
                            });

                            HBox boxBoutons = new HBox(cancelButton, deleteButton);

                            // Créer la mise en page de la fenêtre
                            VBox deleteLayout = new VBox(10);
                            deleteLayout.setPadding(new Insets(10));
                            deleteLayout.getChildren().addAll(new Label("What column(s) do you want to delete ?"), new Separator(),
                                    columnsPane, new Separator(), boxBoutons);

                            // Créer la scène et afficher la fenêtre
                            deleteStage.setScene(new Scene(deleteLayout));
                            deleteStage.showAndWait();
                        }
                    }
                }
            }
            case "Rows" -> {
                switch (btn.getText()){
                    case "➕" -> {
                        // Ajouter une ligne
                        if (VBoxTable.table.getColumns().size() == 0){
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Cannot add");
                            alert.setHeaderText(null);
                            alert.setContentText("You don't have any columns to add values to.");
                            alert.showAndWait();
                        } else {
                            DataTable table = VBoxTable.table;

                            // Créer la fenêtre d'ajout de ligne
                            Stage addStage = new Stage();
                            addStage.initModality(Modality.APPLICATION_MODAL);
                            addStage.setTitle("Delete Column");
                            addStage.setResizable(false);

                            // Initialisation de la TableView
                            TableView<Object[]> tableView = new TableView<>();
                            HBox[] insertRow = new HBox[table.getNumberOfColumns()];

                            // Ajouter les colonnes et la ligne d'insertion à la TableView
                            for (int columnIndex = 0; columnIndex < table.getNumberOfColumns(); columnIndex++) {
                                final int index = columnIndex;
                                TableColumn<Object[], Object> column = new TableColumn<>(table.getColumnName(columnIndex));
                                column.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue()[index]));
                                column.setReorderable(false);
                                column.setSortable(false);

                                insertRow[columnIndex] = VisualObjectInitialiser.get(table.getColumn(columnIndex).getType());
                                tableView.getColumns().add(column);
                            }

                            tableView.getItems().add(insertRow);
                            tableView.setPrefHeight(70);

                            Button cancelButton = new Button("Cancel");
                            Button addButton = new Button("Add");

                            // Gérer l'événement du bouton Cancel
                            cancelButton.setOnAction(subEvent -> addStage.close());

                            // Gérer l'événement du bouton Add
                            addButton.setOnAction(subEvent -> {
                                Object[] rowToAdd = new Object[table.getNumberOfColumns()];
                                int i = 0;
                                boolean hasNull = false;

                                try {
                                    while (i < table.getNumberOfColumns()) {
                                        Class<?> columnType = table.getColumn(i).getType();
                                        if (columnType.equals(Integer.class)) {
                                            TextField field = (TextField) insertRow[i].getChildren().get(0);
                                            if (field.getText().length() > 0) {
                                                rowToAdd[i] = Integer.parseInt(field.getText());
                                            } else {
                                                rowToAdd[i] = null;
                                                hasNull = true;
                                            }
                                        } else if (columnType.equals(Double.class)) {
                                            TextField field = (TextField) insertRow[i].getChildren().get(0);
                                            if (field.getText().length() > 0) {
                                                rowToAdd[i] = Double.parseDouble(field.getText());
                                            } else {
                                                rowToAdd[i] = null;
                                                hasNull = true;
                                            }
                                        } else if (columnType.equals(String.class)) {
                                            TextField field = (TextField) insertRow[i].getChildren().get(0);
                                            if (field.getText().length() > 0) {
                                                rowToAdd[i] = field.getText();
                                            } else {
                                                rowToAdd[i] = null;
                                                hasNull = true;
                                            }
                                        } else if (columnType.equals(Boolean.class)) {
                                            RadioButton trueButton = (RadioButton) insertRow[i].getChildren().get(0);
                                            RadioButton falseButton = (RadioButton) insertRow[i].getChildren().get(1);
                                            if (trueButton.isSelected() || falseButton.isSelected()) {
                                                rowToAdd[i] = trueButton.isSelected();
                                            } else {
                                                rowToAdd[i] = null;
                                                hasNull = true;
                                            }
                                        } else {
                                            ComboBox<String> days = null;
                                            TextField year = null;
                                            String day = null;
                                            String month = null;
                                            for (Node node : insertRow[i].getChildren()) {
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
                                                    rowToAdd[i] = date;
                                                } else {
                                                    Alert dateDialog = new Alert(Alert.AlertType.ERROR);
                                                    dateDialog.setTitle("Incorrect Date");
                                                    dateDialog.setHeaderText(table.getColumnName(i) + " field is invalid.");
                                                    dateDialog.setContentText("The date you are trying to input does not exist.");
                                                    dateDialog.getButtonTypes().setAll(ButtonType.OK);
                                                    dateDialog.showAndWait();
                                                    return;
                                                }
                                            } else {
                                                rowToAdd[i] = null;
                                                hasNull = true;
                                            }
                                        }
                                        i++;
                                    }
                                } catch (Exception exception) {
                                    Alert errorDialog = new Alert(Alert.AlertType.ERROR);
                                    errorDialog.setTitle("Error");
                                    errorDialog.setHeaderText(table.getColumnName(i) + " field is invalid.");
                                    errorDialog.setContentText("The values you are trying to input are incorrect.");
                                    errorDialog.getButtonTypes().setAll(ButtonType.OK);
                                    errorDialog.showAndWait();
                                    return;
                                }

                                if (hasNull){
                                    AtomicBoolean canInsert = new AtomicBoolean(false);
                                    Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                                    confirmationDialog.setTitle("Confirmation");
                                    confirmationDialog.setHeaderText("Some fields have empty values.");
                                    confirmationDialog.setContentText("Insert anyway ?");
                                    confirmationDialog.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                                    confirmationDialog.showAndWait().ifPresent(buttonType -> {
                                        if (buttonType == ButtonType.YES) {
                                            canInsert.set(true);
                                        }
                                    });
                                    if (!canInsert.get()){
                                        return;
                                    }
                                }
                                table.insertRow(rowToAdd);
                                HBoxMain.updateTable(table);
                                addStage.close();
                            });

                            HBox boxBoutons = new HBox(cancelButton, addButton);

                            // Créer la mise en page de la fenêtre
                            VBox addLayout = new VBox(10);
                            addLayout.setPadding(new Insets(10));
                            addLayout.getChildren().addAll(
                                    new Label("Please input the values you want for the row :"), tableView, boxBoutons
                            );

                            // Créer la scène et afficher la fenêtre
                            addStage.setScene(new Scene(addLayout));
                            addStage.showAndWait();
                        }
                    }
                    case "➖" -> {
                        // Retirer une ligne
                        if (VBoxTable.table.getRows().size() == 0){
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Cannot delete");
                            alert.setHeaderText(null);
                            alert.setContentText("You don't have any rows to delete.");
                            alert.showAndWait();
                        } else {
                            DataTable table = VBoxTable.table;

                            // Créer la fenêtre de suppression de lignes
                            Stage deleteStage = new Stage();
                            deleteStage.initModality(Modality.APPLICATION_MODAL);
                            deleteStage.setTitle("Delete Rows");
                            deleteStage.setResizable(false);
                            deleteStage.setHeight(300);

                            VBoxCondition conditionPane = new VBoxCondition(table);

                            Button cancelButton = new Button("Cancel");
                            Button deleteButton = new Button("Delete");

                            // Gérer l'événement du bouton Cancel
                            cancelButton.setOnAction(subEvent -> deleteStage.close());

                            // Gérer l'événement du bouton Delete
                            deleteButton.setOnAction(subEvent -> {
                                ArrayList<Condition> conditionsArrayList = conditionPane.getConditions();
                                Condition[] conditions = new Condition[conditionsArrayList.size()];
                                for (int i = 0; i < conditionsArrayList.size(); i++){
                                    conditions[i] = conditionsArrayList.get(i);
                                }
                                ArrayList<ArrayList<Object>> rows = table.getRows();
                                TreeSet<String> fields = new TreeSet<>();
                                fields.add("*");
                                rows.removeAll(table.select(fields, conditions).getRows());

                                for (int i = 0; i < table.getNumberOfColumns(); i++){
                                    Column col = table.getColumn(i);
                                    col.clear();
                                    for (ArrayList<Object> row : rows){
                                        col.addValue(row.get(i));
                                    }
                                }
                                HBoxMain.updateTable(table);
                                deleteStage.close();
                            });

                            HBox boxBoutons = new HBox(cancelButton, deleteButton);

                            // Créer la mise en page de la fenêtre
                            VBox deleteLayout = new VBox(10);
                            deleteLayout.setPadding(new Insets(10));
                            deleteLayout.getChildren().addAll(
                                    new Label("Choose conditions under which to delete rows :"), new Separator(),
                                    new ScrollPane(conditionPane), new Separator(), boxBoutons
                            );

                            // Créer la scène et afficher la fenêtre
                            Scene scene = new Scene(deleteLayout);
                            scene.getStylesheets().add((new File("css"+File.separator+"style.css")).toURI().toString());
                            deleteStage.setScene(scene);
                            deleteStage.showAndWait();
                        }
                    }
                }
            }
            case "Name" -> changeTableName();
        }
    }

    /**
     * Cette méthode permet de changer le nom de la table.
     */
    public static void changeTableName(){
        DataTable table = VBoxTable.table;

        // Créer la fenêtre de modification du nom
        Stage nameStage = new Stage();
        nameStage.initModality(Modality.APPLICATION_MODAL);
        nameStage.setTitle("Change Name");
        nameStage.setResizable(false);

        // Créer les éléments de la fenêtre
        Label currentNameLabel = new Label("Table Name : " + table.getName());
        TextField newName = new TextField(table.getName());
        Button cancelButton = new Button("Cancel");
        Button applyButton = new Button("Apply");

        // Gérer l'événement du bouton Cancel
        cancelButton.setOnAction(subEvent -> nameStage.close());

        // Gérer l'événement du bouton Apply
        applyButton.setOnAction(subEvent -> {
            if (newName.getText().length() > 0) {
                // Changer le Nom
                nameStage.close();
                if (!table.getName().equals(newName.getText())) {
                    table.setName(newName.getText());
                    HBoxMain.updateTable(table);
                }
            } else {
                Alert dateDialog = new Alert(Alert.AlertType.ERROR);
                dateDialog.setTitle("Invalid Name");
                dateDialog.setHeaderText("The table's name cannot be empty.");
                dateDialog.getButtonTypes().setAll(ButtonType.OK);
                dateDialog.showAndWait();
            }
        });

        HBox boxBoutons = new HBox(cancelButton, applyButton);

        // Créer la mise en page de la fenêtre
        VBox nameLayout = new VBox(10);
        nameLayout.setPadding(new Insets(10));
        nameLayout.getChildren().addAll(currentNameLabel, new Separator(), new Label("New name :"),
                newName, boxBoutons);

        // Créer la scène et afficher la fenêtre
        nameStage.setScene(new Scene(nameLayout));
        nameStage.showAndWait();
    }
}
