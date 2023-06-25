package controleur;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modele.Condition;
import modele.DataTable;
import outils.ReaderWriter;
import vue.*;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Cette classe est le contrôleur des options de l'application.
 * Elle implémente l'interface EventHandler ActionEvent pour gérer les événements des boutons et des menus.
 */
public class OptionsController implements EventHandler<ActionEvent> {

    /**
     * Cette méthode gère les événements déclenchés par les boutons et les menus.
     *
     * @param event l'événement déclenché
     */
    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() instanceof Button btn){
            switch (btn.getText()) {
                case "◀" -> {
                    if (HBoxMain.history.size() > 0) {
                        HBoxMain.undo();
                    }
                }
                case "▶" -> {
                    if (HBoxMain.future.size() > 0) {
                        HBoxMain.redo();
                    }
                }
                case "💾" -> saveTable();
                case "SELECT" -> {
                    if (VBoxTable.table.getRows().size() == 0){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Cannot select");
                        alert.setHeaderText(null);
                        alert.setContentText("You don't have any rows to select.");
                        alert.showAndWait();
                    } else {
                        DataTable table = VBoxTable.table;

                        // Créer la fenêtre de selection
                        Stage selectStage = new Stage();
                        selectStage.initModality(Modality.APPLICATION_MODAL);
                        selectStage.setTitle("Select");
                        selectStage.setResizable(false);
                        selectStage.setHeight(300);

                        ColumnGridPane columnsPane = new ColumnGridPane(table);
                        VBoxCondition conditionPane = new VBoxCondition(table);

                        Button cancelButton = new Button("Cancel");
                        Button selectButton = new Button("Select");

                        // Gérer l'événement du bouton Cancel
                        cancelButton.setOnAction(subEvent -> selectStage.close());

                        // Gérer l'événement du bouton Select
                        selectButton.setOnAction(subEvent -> {
                            ArrayList<Condition> conditionsArrayList = conditionPane.getConditions();
                            Condition[] conditions = new Condition[conditionsArrayList.size()];
                            for (int i = 0; i < conditionsArrayList.size(); i++){
                                conditions[i] = conditionsArrayList.get(i);
                            }

                            TreeSet<String> fields = new TreeSet<>();
                            for (CheckBox box : columnsPane.getListColumns()){
                                if (box.isSelected()){
                                    fields.add(box.getText());
                                }
                            }
                            if (fields.size() == 0){
                                fields.add("*");
                            }
                            HBoxMain.updateTable(table.select(fields, conditions));
                            selectStage.close();
                        });

                        HBox boxBoutons = new HBox(cancelButton, selectButton);

                        // Créer la mise en page de la fenêtre
                        VBox deleteLayout = new VBox(10);
                        deleteLayout.setPadding(new Insets(10));
                        deleteLayout.getChildren().addAll(
                                new Label("Columns to select (leave unchecked if you want them all) :"), columnsPane, new Separator(),
                                new Label("Conditions :"), new ScrollPane(conditionPane), new Separator(), boxBoutons
                        );

                        // Créer la scène et afficher la fenêtre
                        Scene scene = new Scene(deleteLayout);
                        scene.getStylesheets().add((new File("css"+File.separator+"style.css")).toURI().toString());
                        selectStage.setScene(scene);
                        selectStage.showAndWait();
                    }
                }
                case "JOIN" -> {
                    if (VBoxRoot.tables.size() == 0){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Cannot join");
                        alert.setHeaderText(null);
                        alert.setContentText("You don't have any tables to join yours with.");
                        alert.showAndWait();
                    } else {
                        DataTable table1 = VBoxTable.table;
                        AtomicReference<DataTable> table2 = new AtomicReference<>();

                        // Créer la fenêtre de choix de table
                        Stage joinStage = new Stage();
                        joinStage.initModality(Modality.APPLICATION_MODAL);
                        joinStage.setTitle("Join Tables");
                        joinStage.setMinHeight(300);
                        joinStage.setMinWidth(600);

                        ComboBox<String> tables = new ComboBox<>();
                        for (DataTable table : VBoxRoot.tables){
                            tables.getItems().add(table.getName());
                        }

                        ComboBox<String> columns1 = new ComboBox<>();
                        columns1.getItems().addAll(table1.getColumnNames());
                        ComboBox<String> columns2 = new ComboBox<>();
                        HBox hBoxColumn1 = new HBox(new Label(table1.getName() + "'s column : "), columns1);
                        HBox hBoxColumn2 = new HBox();

                        Button cancelButton = new Button("Cancel");
                        Button joinButton = new Button("Join");

                        HBox boxBoutons = new HBox(cancelButton, joinButton);
                        VBox vBoxJoin = new VBox(
                                new HBox(new Label("Table to join yours to : "), tables), new Separator(),
                                new Label("Columns that will be joined together :"), hBoxColumn1, hBoxColumn2,
                                new Separator(), boxBoutons
                        );

                        VBox vBoxTable = new VBox(new Label("No table selected."));

                        // Gérer l'événement du bouton Cancel
                        cancelButton.setOnAction(subEvent -> joinStage.close());

                        // Gérer l'événement du choix de la table
                        tables.setOnAction(comboEvent -> {
                            table2.set(VBoxRoot.tables.get(tables.getSelectionModel().getSelectedIndex()));
                            columns2.getItems().clear();
                            columns2.getItems().addAll(table2.get().getColumnNames());
                            hBoxColumn2.getChildren().addAll(new Label(table2.get().getName() + "'s column : "), columns2);
                            vBoxTable.getChildren().clear();
                            vBoxTable.getChildren().add(new DataTableView(table2.get()));
                        });

                        // Gérer l'évènement du bouton Join
                        joinButton.setOnAction(subEvent -> {
                            if (tables.getSelectionModel().getSelectedItem() == null){
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("No table selected");
                                alert.setHeaderText("You need to select a table to join yours to.");
                                alert.showAndWait();
                                return;
                            }
                            String columnName1 = columns1.getSelectionModel().getSelectedItem();
                            String columnName2 = columns2.getSelectionModel().getSelectedItem();
                            if (columnName1 == null || columnName2 == null){
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("No column selected");
                                alert.setHeaderText("You need to select both columns for the join to be possible.");
                                alert.showAndWait();
                                return;
                            }
                            Class<?> type1 = table1.getColumn(columnName1).getType();
                            Class<?> type2 = table2.get().getColumn(columnName2).getType();
                            if (!type1.equals(type2) &&
                                    !((type1.equals(Integer.class) && type2.equals(Double.class)) ||
                                            (type1.equals(Double.class) && type2.equals(Integer.class)))
                            ){
                                Alert columnDialog = new Alert(Alert.AlertType.ERROR);
                                columnDialog.setTitle("Non-matching columns");
                                columnDialog.setHeaderText("The type of these columns do not match.");
                                columnDialog.setContentText("They need to match in order to be properly joined.");
                                columnDialog.getButtonTypes().setAll(ButtonType.OK);
                                columnDialog.showAndWait();
                                return;
                            }
                            HBoxMain.updateTable(table1.join(table2.get(), columnName1, columnName2));
                            joinStage.close();
                        });

                        // Créer la mise en page de la fenêtre
                        HBox joinLayout = new HBox(10);
                        joinLayout.setPadding(new Insets(10));
                        joinLayout.getChildren().addAll(vBoxJoin, new Separator(Orientation.VERTICAL), vBoxTable);

                        // Créer la scène et afficher la fenêtre
                        joinStage.setScene(new Scene(joinLayout));
                        joinStage.showAndWait();
                    }
                }
            }
        } else {
            MenuItem menuItem = (MenuItem) event.getSource();
            switch (menuItem.getText()) {
                case "➕ New Table" -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("New Table");
                    alert.setHeaderText(null);
                    alert.setContentText("Are you sure you want to create a new table ?");
                    alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                    alert.showAndWait().ifPresent(buttonType -> {
                        if (buttonType == ButtonType.YES) {
                            HBoxMain.updateTable(new DataTable("New Table"));
                        }
                    });
                }
                case "➖ Delete Table" -> {
                    if (VBoxRoot.tables.size() == 0){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Cannot delete");
                        alert.setHeaderText(null);
                        alert.setContentText("You don't have any tables to delete.");
                        alert.showAndWait();
                    } else {
                        // Créer la fenêtre de suppression de tables
                        Stage deleteStage = new Stage();
                        deleteStage.initModality(Modality.APPLICATION_MODAL);
                        deleteStage.setTitle("Delete Table");
                        deleteStage.setResizable(false);

                        // Initialisation de la GridPane
                        GridPane tablesPane = new GridPane();
                        ArrayList<CheckBox> listTables = new ArrayList<>();
                        int datatableIndex = 0;
                        int columnIndex = 0;
                        int rowIndex = 0;
                        while (datatableIndex < VBoxRoot.tables.size()){
                            if (rowIndex > 2){
                                rowIndex = 0;
                                columnIndex++;
                            }
                            CheckBox checkBox = new CheckBox(VBoxRoot.tables.get(datatableIndex).getName());
                            checkBox.setUserData(datatableIndex);
                            listTables.add(checkBox);
                            tablesPane.add(checkBox, rowIndex, columnIndex);
                            rowIndex++;
                            datatableIndex++;
                        }

                        Button cancelButton = new Button("Cancel");
                        Button deleteButton = new Button("Delete");

                        // Gérer l'événement du bouton Cancel
                        cancelButton.setOnAction(subEvent -> deleteStage.close());

                        // Gérer l'événement du bouton Delete
                        deleteButton.setOnAction(subEvent -> {
                            boolean successfulDelete = true;
                            ArrayList<DataTable> listSelectedTables = new ArrayList<>();
                            ArrayList<MenuItem> listSelectedMenus = new ArrayList<>();
                            for (CheckBox box : listTables){
                                if (box.isSelected()){
                                    listSelectedTables.add(VBoxRoot.tables.get((int) box.getUserData()));
                                    listSelectedMenus.add(VBoxRoot.menuTables.getItems().get((int) box.getUserData()));
                                }
                            }
                            for (int i = 0; i < listSelectedTables.size(); i++){
                                File saveFile = new File("saved_data"+ File.separator + listSelectedTables.get(i).getName().toLowerCase() + ".ser");
                                if (!saveFile.delete()) {
                                    successfulDelete = false;
                                } else {
                                    VBoxRoot.tables.remove(listSelectedTables.get(i));
                                    VBoxRoot.menuTables.getItems().remove(listSelectedMenus.get(i));
                                }
                            }
                            if (!successfulDelete){
                                // Créer une alerte d'erreur
                                Alert errorDialog = new Alert(Alert.AlertType.ERROR);
                                errorDialog.setTitle("Error");
                                errorDialog.setHeaderText(null);
                                errorDialog.setContentText("Some tables have not been deleted properly.");
                                errorDialog.getButtonTypes().setAll(ButtonType.OK);
                                errorDialog.showAndWait();
                            } else {
                                // Créer une boîte d'information
                                Alert errorDialog = new Alert(Alert.AlertType.INFORMATION);
                                errorDialog.setTitle("Successful delete");
                                errorDialog.setHeaderText("All selected tables have been deleted successfully.");
                                errorDialog.setContentText("This is not reversible.");
                                errorDialog.getButtonTypes().setAll(ButtonType.OK);
                                errorDialog.showAndWait();
                            }
                            deleteStage.close();
                        });

                        HBox boxBoutons = new HBox(cancelButton, deleteButton);

                        // Créer la mise en page de la fenêtre
                        VBox deleteLayout = new VBox(10);
                        deleteLayout.setPadding(new Insets(10));
                        deleteLayout.getChildren().addAll(new Label("What table(s) do you want to delete ?"), new Separator(),
                                tablesPane, new Separator(), boxBoutons);

                        // Créer la scène et afficher la fenêtre
                        deleteStage.setScene(new Scene(deleteLayout));
                        deleteStage.showAndWait();
                    }
                }
                case "💾 Save Table" -> saveTable();
                case "❌ Quit" -> {
                    // Créer une boîte de dialogue de confirmation
                    Alert confirmationDialog = new Alert(Alert.AlertType.WARNING);
                    confirmationDialog.setTitle("Warning");
                    confirmationDialog.setHeaderText("Are you sure you want to quit ?");
                    confirmationDialog.setContentText("All unsaved progress will be lost.");

                    // Ajouter les boutons OK et Cancel à la boîte de dialogue
                    confirmationDialog.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

                    // Gérer l'action de l'utilisateur en fonction du bouton cliqué
                    confirmationDialog.showAndWait().ifPresent(buttonType -> {
                        if (buttonType == ButtonType.OK) {
                            Platform.exit();
                        }
                    });
                }
                default -> {
                    // Il s'agit d'un MenuItem dans le menu TABLES.
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Change Table");
                    alert.setHeaderText(null);
                    alert.setContentText("Are you sure you want to load another table ?");
                    alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                    alert.showAndWait().ifPresent(buttonType -> {
                        if (buttonType == ButtonType.YES) {
                            HBoxMain.updateTable(VBoxRoot.tables.get((Integer) menuItem.getUserData()));
                        }
                    });
                }
            }
        }
    }

    /**
     * Sauvegarde la table actuelle avec un nom spécifié par l'utilisateur.
     * Vérifie si un fichier de sauvegarde existe déjà et propose de l'écraser.
     * Ajoute la table sauvegardée au menu des tables.
     */
    public static void saveTable(){
        String currentName = VBoxTable.table.getName();
        File saveFile = new File("saved_data"+ File.separator + currentName.toLowerCase() + ".ser");
        AtomicBoolean changeName = new AtomicBoolean(true);
        AtomicBoolean overwrite = new AtomicBoolean(false);
        if (saveFile.exists()){
            // Créer une boîte de dialogue de mise en garde
            Alert confirmationDialog = new Alert(Alert.AlertType.WARNING);
            confirmationDialog.setTitle("Warning");
            confirmationDialog.setHeaderText("A table already exists with that name.");
            confirmationDialog.setContentText("Do you want to overwrite that table ?");

            // Ajouter les boutons OK et Cancel à la boîte de dialogue
            confirmationDialog.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);

            // Gérer l'action de l'utilisateur en fonction du bouton cliqué
            confirmationDialog.showAndWait().ifPresent(buttonType -> {
                if (buttonType == ButtonType.NO) {
                    TableController.changeTableName();
                    changeName.set(!currentName.equals(VBoxTable.table.getName()));
                } else if (buttonType == ButtonType.CANCEL) {
                    changeName.set(false);
                } else if (buttonType == ButtonType.YES){
                    overwrite.set(true);
                }
            });
        }
        if (changeName.get()) {
            DataTable table = VBoxTable.table;
            saveFile = new File("saved_data"+ File.separator + table.getName().toLowerCase() + ".ser");

            // Sauvegarder la table
            ReaderWriter.write(saveFile, table);
            if (!overwrite.get()) {
                MenuItem item = new MenuItem("▶ " + table.getName());
                item.setOnAction(VBoxRoot.optionsController);
                item.setUserData(VBoxRoot.tables.size());
                VBoxRoot.tables.add(table);
                VBoxRoot.menuTables.getItems().add(item);
            }

            // On confirme que la table a été sauvegardée
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successful save");
            alert.setHeaderText("Your table has been saved successfully.");
            alert.setContentText("You can access it within the \"Tables\" menu.");
            alert.getButtonTypes().setAll(ButtonType.OK);
            alert.showAndWait();
        }
    }
}
