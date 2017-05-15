package gui;

import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ComponentCreator {

    public VBox vboxCreator() {
        VBox vertical = new VBox();
        vertical.setLayoutX(20);
        vertical.setLayoutY(20);
        vertical.setSpacing(10);
        return vertical;
    }

    public HBox hBoxCreator() {
        HBox hbox = new HBox();
        hbox.setLayoutX(20);
        hbox.setLayoutY(20);
        hbox.setSpacing(40);
        return hbox;
    }

    public Label labelCreator(String title) {
        Label label = new Label(title);
        label.getStyleClass().add("header");
        return label;
    }

    public Separator sepHCreator(int width) {
        Separator sepH = new Separator();
        sepH.setPrefWidth(width);
        sepH.setOrientation(Orientation.HORIZONTAL);
        sepH.setStyle("-fx-background-color: #4478b6;-fx-background-insets:1;");
        return sepH;
    }

    public ScrollPane scrollPaneCreator() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setLayoutX(10);
        scrollPane.setLayoutY(10);
        scrollPane.setCursor(Cursor.CLOSED_HAND);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPannable(true);
        return scrollPane;
    }

     /*
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LOCAL_DATE("03-05-2017"));
        datePicker.setOnAction(event -> {
            date = datePicker.getValue();
            System.out.println("Selected date: " + date);
        });
        */

}
