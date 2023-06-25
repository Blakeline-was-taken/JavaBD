package vue;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import modele.DataTable;

public class VBoxTable extends VBox {
    public static DataTableView tableView;
    public static DataTable table;

    public VBoxTable(DataTable givenTable) {
        table = givenTable;

        GridPane fullTable = new GridPane();
        Button nameButton = new Button(table.getName());
        nameButton.setUserData("Name");
        nameButton.setOnAction(VBoxRoot.tableController);
        nameButton.getStyleClass().add("button-oth");

        tableView = new DataTableView(table);
        tableView.setOnMouseClicked(VBoxRoot.cellController);

        fullTable.add(new Label("Table Name (click to change) : "), 0, 0);
        fullTable.add(nameButton, 1, 0);
        fullTable.add(tableView, 0, 1, 2, 1);
        fullTable.add(new Label("Number of rows : " + table.getNumberOfRows()), 0, 2);
        fullTable.add(new Label("Number of columns : " + table.getNumberOfColumns()), 1, 2);

        getChildren().addAll(fullTable);
    }
}
