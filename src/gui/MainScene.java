package gui;

import Film.FilmArray;
import Film.Film;
import Seans.Seans;
import connection.CurrentResponse;
import connection.Request;
import connection.RequestHandler;
import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.time.LocalDate;

public class MainScene  extends Scene {
    private Group root;
    private Seans curSeans;
    private ComponentCreator componentCreator;

    public MainScene(Group root, int width, int height) {
        super(root, width, height);
        this.root = root;
        this.getStylesheets().add("css/styles.css");
        componentCreator = new ComponentCreator();
        curSeans = new Seans();
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
            curSeans.setDate(checkOutDatePicker.getValue().toString());
            Group root = new Group();
            Stage stage = new Stage();
            stage.setTitle("Second Stage");
            FilmsOnDateScene second = new FilmsOnDateScene(root, 700, 600, curSeans);
            second.fillScene();
            stage.setScene(second);
            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        });
        Request request = new Request("get", ">>", "filmId",
        "Select distinct filmId from seans where date = '" + LocalDate.now() + "' limit 3;");
        RequestHandler.getInstance().sendMes(request);

        String[] filmID = CurrentResponse.getInstance().getCurResponseArray();

        for (int i = 0; i <= 2; i++) {
            Film curFilm = FilmArray.getInstance().loadFilm(filmID[i]);
            VBox filmBox = FilmArray.getInstance().createFilmVBox(curFilm, curSeans);
            hbox.getChildren().add(filmBox);
        }

        vertical.getChildren().addAll(label, checkOutDatePicker, btn, sepH, hbox);
        root.getChildren().addAll(vertical);

    }
}
