package gui;

import Film.Film;
import connection.CurrentResponse;
import connection.Request;
import connection.RequestHandler;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SecondScene extends Scene {
    private Group root;
    private String dateFromClient;
    private GridPane gPFilms;
    private ComponentCreator componentCreator;
    private List<Film> filmList;
    private int curRow = 1;
    private int curCol = 0;

    public SecondScene(Group root, int width, int height, String date) {
        super(root, width, height);
        this.root = root;
        this.getStylesheets().add("css/styles.css");
        dateFromClient = date;
        componentCreator = new ComponentCreator();
        createFilm();
    }

    public void fillScene() {

        Label lblDate = componentCreator.labelCreator(dateFromClient);
        Separator sepH = componentCreator.sepHCreator(600);

        gPFilms = new GridPane();
        gPFilms.setLayoutX(20);
        gPFilms.setLayoutY(20);
        gPFilms.setCursor(Cursor.TEXT);
        gPFilms.setStyle("-fx-font:bold 14pt Arial;-fx-text-fill:#a0522d;");
        gPFilms.setVgap(10);
        gPFilms.setHgap(10);

        ScrollPane scrollPane = componentCreator.scrollPaneCreator();
        scrollPane.setPrefSize(600, 400);
        scrollPane.setContent(gPFilms);
        scrollPane.setPrefViewportHeight(400);
        scrollPane.setPrefViewportWidth(600);

        VBox vBox = componentCreator.vboxCreator();
        vBox.getChildren().addAll(lblDate, sepH, scrollPane);
        root.getChildren().addAll(vBox);

        fillFilmGrid();

    }

    private void fillFilmGrid() {
        for (Film film:
             filmList) {
            gPFilms.add(createFilmVBox(film), curCol++, curRow);
            positionControl();
        }
    }

    private void positionControl() {
        if (curCol == 3){
            curCol = 0;
            curRow++;
        }
    }

    private VBox createFilmVBox(Film film) {

        Label filmName = new Label(film.getFilmName());
        filmName.getStyleClass().add("item-title");

        Button btn = new Button();
        Image im = new Image("file:resources/" + film.getFilmImagePath() + ".jpg", 160, 160, false, true);
        ImageView imv = new ImageView(im);
        imv.setFitHeight(200);
        imv.setFitWidth(150);
        btn.setGraphic(imv);
        btn.getStyleClass().add("button");
        btn.setPrefSize(150, 200);
        btn.setTextAlignment(TextAlignment.CENTER);

        Label country = new Label(film.getFilmCountry());
        country.getStyleClass().add("item-title");

        VBox vBox = new VBox();
        vBox.setLayoutX(2);
        vBox.setLayoutY(2);
        vBox.setSpacing(3);
        vBox.getChildren().addAll(filmName, btn, country);

        return vBox;
    }

    private void createFilm() {
        filmList = new ArrayList<Film>();
        if (dateFromClient != null) {

            Request request = new Request("send", ">>", "date", dateFromClient);
            RequestHandler.getInstance().sendMes(request);

            Request request2 = new Request("get", ">>", "filmId",
                    "Select distinct filmId from seans where date = '" + dateFromClient + "';");
            RequestHandler.getInstance().sendMes(request2);

            String[] filmID = null;
            boolean isTrue = true;
            while (isTrue)
                if (CurrentResponse.getInstance().getCurrentResponse() != null) {
                    filmID = CurrentResponse.getInstance().getCurrentResponse().getArray();
                    isTrue = false;
                }
            CurrentResponse.getInstance().setCurrentResponse(null);

            for (String str : filmID) {
                Request request3 = new Request("get", ">>",
                        "name/countryID/length/lang/image/content",
                        "Select * from Films where id = " + str + ";");
                RequestHandler.getInstance().sendMes(request3);
                String[] list = null;
                isTrue = true;
                while (isTrue) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ignored) {
                    }
                    if (CurrentResponse.getInstance().getCurrentResponse() != null) {
                        list = CurrentResponse.getInstance().getCurrentResponse().getArray();
                        isTrue = false;
                    }
                }
                CurrentResponse.getInstance().setCurrentResponse(null);
                Film newFilm = new Film();
                newFilm.setFilmID(Integer.parseInt(str));
                newFilm.setFilmName(list[0]);
                newFilm.setFilmCountry(list[1]);
                newFilm.setFilmLength(list[2]);
                newFilm.setFilmLang(list[3]);
                newFilm.setFilmImagePath(list[4]);
                newFilm.setFilmContent(list[5]);
                filmList.add(newFilm);
            }
        }
    }
}
