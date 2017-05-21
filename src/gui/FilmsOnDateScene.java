package gui;

import Film.*;
import Seans.Seans;
import connection.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class FilmsOnDateScene extends Scene {
    private Group root;
    private Seans curSeans;
    private Stage parentStage;
    private Stage thisStage;
    private GridPane gPFilms;
    private ComponentCreator componentCreator;
    private int curRow = 1;
    private int curCol = 0;

    public FilmsOnDateScene(Group root, int width, int height, Seans curSeans, Stage parentStage, Stage thisStage) {
        super(root, width, height);
        this.root = root;
        this.curSeans = curSeans;
        this.parentStage = parentStage;
        this.thisStage = thisStage;
        this.getStylesheets().add("css/styles.css");
        componentCreator = new ComponentCreator();
    }

    public void fillScene() {
        createFilm();
        Button backBtn = componentCreator.backBtnCreator(parentStage);
        backBtn.setPrefSize(30, 30);
        Label lblDate = componentCreator.labelCreator(covertDateFormat(curSeans.getDate()));
        lblDate.setId("beautiful");
        HBox nameAndBackBtn = componentCreator.hBoxCreator();
        nameAndBackBtn.setAlignment(Pos.BASELINE_LEFT);
        nameAndBackBtn.setSpacing(100);
        nameAndBackBtn.getChildren().addAll(backBtn, lblDate);

        Separator sepH = componentCreator.sepHCreator(650);

        gPFilms = new GridPane();
        gPFilms.setLayoutX(20);
        gPFilms.setLayoutY(20);
        gPFilms.setCursor(Cursor.TEXT);
        gPFilms.setStyle("-fx-font:bold 14pt Arial;-fx-text-fill:#a0522d;");
        gPFilms.setVgap(10);
        gPFilms.setHgap(10);

        ScrollPane scrollPane = componentCreator.scrollPaneCreator();
        scrollPane.setPrefSize(700, 500);
        scrollPane.setContent(gPFilms);
        scrollPane.setPrefViewportHeight(450);
        scrollPane.setPrefViewportWidth(650);

        VBox vBox = componentCreator.vboxCreator();
        vBox.getChildren().addAll(nameAndBackBtn, sepH, scrollPane);
        root.getChildren().addAll(vBox);

        fillFilmGrid();

    }

    private String covertDateFormat(String oldDateString) {
        final String OLD_FORMAT = "yyyy-MM-dd";
        final String NEW_FORMAT = "dd.MM.yyyy";
        String newDateString;

        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
        Date d = null;
        try {
            d = sdf.parse(oldDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf.applyPattern(NEW_FORMAT);
        newDateString = sdf.format(d);
        return newDateString;
    }

    private void fillFilmGrid() {

        Set set = FilmArray.getInstance().getFilmHashMap().entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry) iterator.next();
            gPFilms.add(FilmArray.getInstance().createFilmVBox( (Film) mentry.getValue(), curSeans, thisStage), curCol++, curRow);
            positionControl();
        }
    }

    private void positionControl() {
        if (curCol == 3){
            curCol = 0;
            curRow++;
        }
    }

    private void createFilm() {
        if (curSeans.getDate() != null) {

            Request request = new Request("send", ">>", "date", curSeans.getDate());
            RequestHandler.getInstance().sendMes(request);

            Request request2 = new Request("get", ">>", "filmId",
                    "Select distinct filmId from seans where date = '" + curSeans.getDate() + "';");
            RequestHandler.getInstance().sendMes(request2);

            String[] filmID = CurrentResponse.getInstance().getCurResponseArray();
            for (String str : filmID) {
                FilmArray.getInstance().loadFilm(str);
            }
        }
    }
}
