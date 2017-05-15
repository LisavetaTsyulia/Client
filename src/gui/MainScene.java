package gui;

import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class MainScene  extends Scene {
    private Group root;
    private LocalDate date;
    private ComponentCreator componentCreator;


    public MainScene(Group root, int width, int height) {
        super(root, width, height);
        this.root = root;
        this.getStylesheets().add("css/styles.css");
        componentCreator = new ComponentCreator();
        Platform.setImplicitExit(false);
    }

    public void fillScene() {
        VBox vertical = componentCreator.vboxCreator();
        Label label = componentCreator.labelCreator("Выберите дату");
        Separator sepH = componentCreator.sepHCreator(600);
        HBox hbox = componentCreator.hBoxCreator();
        hbox.setPrefHeight(200);
        hbox.setAlignment(Pos.CENTER);

        DatePicker checkInDatePicker = new DatePicker();
        DatePicker checkOutDatePicker = new DatePicker();
        checkInDatePicker.setValue(LocalDate.now());
        final Callback<DatePicker, DateCell> dayCellFactory =
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item.isBefore(
                                        checkInDatePicker.getValue())
                                        ) {
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                                }
                            }
                        };
                    }
                };

        checkOutDatePicker.setDayCellFactory(dayCellFactory);
        checkOutDatePicker.setValue(checkInDatePicker.getValue());

        Button btn = new Button("Выбрать");
        btn.getStyleClass().add("button");
        btn.setOnAction(event -> {
            date = checkOutDatePicker.getValue();
            System.out.println(date);
            Group root = new Group();
            Stage stage = new Stage();
            stage.setTitle("Second Stage");
            SecondScene second = new SecondScene(root, 700, 600, date.toString());
            second.fillScene();
            stage.setScene(second);
            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        });

        final ImageView[] imv = new ImageView[3];

        for (int i = 0; i <= 2; i++) {
            Image im = new Image("file:resources/image"+ (i+1)  +".jpg", 160, 160, false, true);
            imv[i] = new ImageView(im);
            hbox.getChildren().add(imv[i]);
        }

        vertical.getChildren().addAll(label, checkOutDatePicker, btn, sepH, hbox);
        root.getChildren().addAll(vertical);

    }
}
