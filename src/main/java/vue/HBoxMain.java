package vue;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;

import modele.DataTable;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.TreeSet;

public class HBoxMain extends HBox {
    public static VBoxTable vBoxTable;
    public static ArrayDeque<DataTable> history = new ArrayDeque<>();
    public static ArrayDeque<DataTable> future = new ArrayDeque<>();
    public static DataTable currentTable;

    public HBoxMain(){
        VBoxOptions options = new VBoxOptions();
        Separator separator = new Separator(Orientation.VERTICAL);
        separator.setPadding(new Insets(10, 0, 10, 0));
        currentTable = VBoxRoot.tables.get(0);
        vBoxTable = new VBoxTable(currentTable.copy());
        getChildren().addAll(options, separator, vBoxTable);
        setSpacing(10);
    }

    private static void updateVBoxTable(){
        vBoxTable.getChildren().clear();
        vBoxTable.getChildren().addAll(new VBoxTable(currentTable.copy()).getChildren());
    }

    public static void updateTable(DataTable table){
        future.clear();
        history.add(currentTable.copy());
        currentTable = table;
        updateVBoxTable();
    }

    public static void undo(){
        future.add(currentTable.copy());
        currentTable = history.getFirst();
        history.removeFirst();
        updateVBoxTable();
    }

    public static void redo(){
        history.add(currentTable.copy());
        currentTable = future.getFirst();
        future.removeFirst();
        updateVBoxTable();
    }
}
