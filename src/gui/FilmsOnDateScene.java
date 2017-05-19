package gui;

import Film.*;
import Seans.Seans;
import connection.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;

public class FilmsOnDateScene extends Scene {
    private Group root;
    private Seans curSeans;
    private GridPane gPFilms;
    private ComponentCreator componentCreator;
    private int curRow = 1;
    private int curCol = 0;

    public FilmsOnDateScene(Group root, int width, int height, Seans curSeans) {
        super(root, width, height);
        this.root = root;
        this.getStylesheets().add("css/styles.css");
        this.curSeans = curSeans;
        componentCreator = new ComponentCreator();
    }

    public void fillScene() {
        createFilm();
        Button backBtn = new Button();
        Image im = new Image("file:resources/arr.png", 30, 30, false, true);
        ImageView imv = new ImageView(im);
        imv.setFitHeight(30);
        imv.setFitWidth(30);
        backBtn.setGraphic(imv);
        backBtn.setStyle("-fx-background-color: white");
        backBtn.setOnAction(event -> {
            Group root = new Group();
            Stage stage = new Stage();
            stage.setTitle("First Stage");
            MainScene mainScene = new MainScene(root, 700, 600);
            mainScene.fillScene();
            stage.setScene(mainScene);
            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();

        });
        backBtn.setPrefSize(30, 30);

        Label lblDate = componentCreator.labelCreator(curSeans.getDate());
        Separator sepH = componentCreator.sepHCreator(600);

        gPFilms = new GridPane();
        gPFilms.setLayoutX(20);
        gPFilms.setLayoutY(20);
        gPFilms.setCursor(Cursor.TEXT);
        gPFilms.setStyle("-fx-font:bold 14pt Arial;-fx-text-fill:#a0522d;");
        gPFilms.setVgap(10);
        gPFilms.setHgap(10);

        ScrollPane scrollPane = componentCreator.scrollPaneCreator();
        scrollPane.setPrefSize(650, 450);
        scrollPane.setContent(gPFilms);
        scrollPane.setPrefViewportHeight(450);
        scrollPane.setPrefViewportWidth(650);

        VBox vBox = componentCreator.vboxCreator();
        vBox.getChildren().addAll(backBtn, lblDate, sepH, scrollPane);
        root.getChildren().addAll(vBox);

        fillFilmGrid();

    }

    private void fillFilmGrid() {

        Set set = FilmArray.getInstance().getFilmHashMap().entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry) iterator.next();
            gPFilms.add(FilmArray.getInstance().createFilmVBox( (Film) mentry.getValue(), curSeans), curCol++, curRow);
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
