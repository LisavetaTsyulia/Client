package gui;

import Seans.Seans;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
        hbox.setSpacing(10);
        return hbox;
    }

    public Label labelCreator(String title) {
        Label label = new Label(title);
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

    public Button backBtnCreator(Stage prevStage) {
        Button backBtn = new Button();
        Image im = new Image("file:resources/arr.png",
                30, 30, false, true);
        ImageView imv = new ImageView(im);
        imv.setFitHeight(30);
        imv.setFitWidth(30);
        backBtn.setGraphic(imv);
        backBtn.setStyle("-fx-background-color: white");
        backBtn.setOnAction(event -> {
            prevStage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        });
        backBtn.setPrefSize(30, 30);
        return backBtn;
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
