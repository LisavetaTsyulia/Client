package gui;

import connection.Request;
import connection.RequestHandler;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.net.URISyntaxException;
import java.time.LocalDate;

public class MainScene  extends Scene {
    Group root;
    LocalDate date;
    public MainScene(Group root, int width, int height) {
        super(root, width, height);
        this.root = root;
        this.getStylesheets().add("css/styles.css");
    }

    public void fillScene() {
        VBox vertical = new VBox();
        vertical.setLayoutX(20);
        vertical.setLayoutY(20);
        vertical.setSpacing(10);

        Label label = new Label("Выберите дату");
        label.getStyleClass().add("header");

        DatePicker datePicker = new DatePicker();
        datePicker.setOnAction(event -> {
            date = datePicker.getValue();
            System.out.println("Selected date: " + date);
        });

        Button btn = new Button("Выбрать");
        btn.setOnAction(event -> {
            if (date != null) {
                Request request = new Request("send", ">>", "date", date.toString());
                RequestHandler.getInstance().sendMes(request);
                //request = new Request("get", ">>", "filmId",
                //        "Select distinct filmId from seans where date = " + date.toString() + ";");
                //RequestHandler.getInstance().sendMes(request);
                Group root = new Group();
                Stage stage = new Stage();
                stage.setTitle("Second Stage");
                SecondScene second = new SecondScene(root, 700, 600);
                second.fillScene(date.toString());
                stage.setScene(second);
                stage.show();
                ((Node) (event.getSource())).getScene().getWindow().hide();
            }
        });
        btn.getStyleClass().add("button");

        Separator sepH = new Separator();
        sepH.setPrefWidth(600);
        sepH.setOrientation(Orientation.HORIZONTAL);
        sepH.setStyle("-fx-background-color: #4478b6;-fx-background-insets:1;");

        HBox hbox = new HBox();
        hbox.setLayoutX(20);
        hbox.setLayoutY(20);
        hbox.setSpacing(40);
        hbox.setPrefHeight(200);
        hbox.setAlignment(Pos.CENTER);

        final PerspectiveTransform pt = new PerspectiveTransform();
        final ImageView[] imv = new ImageView[3];

        for(int i = 0; i <= 2 ; i++) {
            //Image im = new Image(getClass().getResource("file:resources/image1.jpg").toString());
            Image im = new Image("file:resources/image1.jpg",160,160,false,true);
            imv[i] = new ImageView(im);
            hbox.getChildren().add(imv[i]);
        }

        vertical.getChildren().addAll(label, datePicker, btn, sepH, hbox);
        root.getChildren().addAll(vertical);

    }
}
