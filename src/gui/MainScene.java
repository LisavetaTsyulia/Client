package gui;

import connection.Request;
import connection.RequestHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

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
                Group root = new Group();
                Stage stage = new Stage();
                stage.setTitle("Second Stage");
                SecondScene second = new SecondScene(root, 700, 600);
                second.fillScene(date.toString());
                stage.setScene(second);
                stage.show();
                // Hide this current window (if this is what you want)
                ((Node) (event.getSource())).getScene().getWindow().hide();
            }
        });
        btn.getStyleClass().add("button");

        Separator sepH = new Separator();
        sepH.setPrefWidth(600);
        sepH.setOrientation(Orientation.HORIZONTAL);
        sepH.setStyle("-fx-background-color: #4478b6;-fx-background-insets:1;");

        vertical.getChildren().addAll(label, datePicker, btn, sepH);

        HBox hbox = new HBox();
        hbox.setLayoutX(20);
        hbox.setLayoutY(20);
        hbox.setSpacing(40);
        hbox.setPrefHeight(200);
        hbox.setAlignment(Pos.CENTER);

        final PerspectiveTransform pt=new PerspectiveTransform();
        final ImageView[] imv=new ImageView[3];
        /*
        for(int i = 1; i <= 3 ; i++) {
            Image im =
                     new Image(this.getClass().getResource("images/image"+i+".jpg").toString());
            imv[i] = new ImageView(im);
            imv[i].setFitHeight(150);
            imv[i].setFitWidth(150);
            imv[i].setPreserveRatio(true);
            pt.setLlx(imv[i].getX());
            pt.setUlx(imv[i].getX());
            pt.setLly(imv[i].getY()+150.0);
            pt.setUly(imv[i].getY());
            pt.setLrx(imv[i].getX()+180.0);
            pt.setUrx(imv[i].getX()+180.0);
            pt.setLry(imv[i].getY()+130.0);
            pt.setUry(imv[i].getY()+20.0);
            imv[i].setEffect(pt);
            final int l=i;
            imv[i].setOnMouseEntered(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    imv[l].setEffect(null);
                }});
            imv[i].setOnMouseExited(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    imv[l].setEffect(pt);
                }});
            hbox.getChildren().add(imv[i]);
        }
        */
        root.getChildren().addAll(vertical, hbox);

    }
}
