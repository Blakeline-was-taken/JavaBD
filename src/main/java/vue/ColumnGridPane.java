package vue;

import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;
import modele.DataTable;

import java.util.ArrayList;

public class ColumnGridPane extends GridPane {
    private final ArrayList<CheckBox> listColumns = new ArrayList<>();

    public ColumnGridPane(DataTable table){
        int columnIndex = 0;
        int gridColIndex = 0;
        int gridRowIndex = 0;
        while (columnIndex < table.getColumns().size()){
            if (gridRowIndex > 4){
                gridRowIndex = 0;
                gridColIndex++;
            }
            CheckBox checkBox = new CheckBox(table.getColumnNames().get(columnIndex));
            listColumns.add(checkBox);
            add(checkBox, gridRowIndex, gridColIndex);
            gridRowIndex++;
            columnIndex++;
        }
    }

    public ArrayList<CheckBox> getListColumns(){
        return listColumns;
    }
}
