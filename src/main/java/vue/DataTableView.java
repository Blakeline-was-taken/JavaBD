package vue;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import modele.DataTable;

public class DataTableView extends TableView<Object[]> {
    
    public DataTableView(DataTable table){
        // Ajouter les colonnes au TableView
        for (int columnIndex = 0; columnIndex < table.getNumberOfColumns(); columnIndex++) {
            final int index = columnIndex;
            TableColumn<Object[], Object> column = new TableColumn<>(table.getColumnName(columnIndex));
            column.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue()[index]));
            column.setReorderable(false);
            column.setSortable(false);
            getColumns().add(column);
        }

        // Ajouter les lignes de données au TableView
        for (int rowIndex = 0; rowIndex < table.getNumberOfRows(); rowIndex++) {
            Object[] rowData = new Object[table.getNumberOfColumns()];
            for (int columnIndex = 0; columnIndex < table.getNumberOfColumns(); columnIndex++) {
                if (rowIndex < table.getColumn(columnIndex).size()) {
                    rowData[columnIndex] = table.getColumn(columnIndex).getValue(rowIndex);
                } else {
                    rowData[columnIndex] = null;
                }
            }
            getItems().add(rowData);
        }
    }
}
