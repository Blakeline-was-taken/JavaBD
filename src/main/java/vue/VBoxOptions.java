package vue;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class VBoxOptions extends VBox {

    private HBox centeredHBox(){
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }

    public VBoxOptions (){
        setSpacing(10);
        setAlignment(Pos.CENTER);

        HBox navBoutons = centeredHBox();
        String[] textBoutons = {"◀", "▶", "💾"};
        for (String textBouton : textBoutons){
            Button bouton = new Button(textBouton);
            bouton.setOnAction(VBoxRoot.mainOptionsController);
            navBoutons.getChildren().add(bouton);
        }
        getChildren().add(navBoutons);

        getChildren().add(new Separator());

        Button select = new Button("SELECT");
        select.setOnAction(VBoxRoot.mainOptionsController);
        getChildren().add(select);

        getChildren().add(new Separator());
        String[] addOrRemove = {"➕", "➖"};

        getChildren().add(new Label("Columns"));
        HBox columnBoutons = centeredHBox();
        for (String textBouton : addOrRemove){
            Button bouton = new Button(textBouton);
            bouton.setOnAction(VBoxRoot.tableController);
            columnBoutons.getChildren().add(bouton);
        }
        getChildren().add(columnBoutons);

        getChildren().add(new Label("Rows"));
        HBox rowsBoutons = centeredHBox();
        for (String textBouton : addOrRemove){
            Button bouton = new Button(textBouton);
            bouton.setOnAction(VBoxRoot.tableController);
            rowsBoutons.getChildren().add(bouton);
        }
        getChildren().add(rowsBoutons);

        getChildren().add(new Separator());

        Button join = new Button("JOIN");
        join.setOnAction(VBoxRoot.mainOptionsController);
        getChildren().add(join);
    }
}
