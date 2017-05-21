package Film;

import Seans.Seans;
import connection.CurrentResponse;
import connection.Request;
import connection.RequestHandler;
import gui.FilmInfoScene;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.HashMap;

public class FilmArray {
    private static FilmArray ourInstance = new FilmArray();
    private HashMap<Integer, Film> filmHashMap;
    public static FilmArray getInstance() {
        return ourInstance;
    }

    private FilmArray() {
        filmHashMap = new HashMap<Integer, Film>();
    }

    public void addFilm(Film film, Integer key) {
        filmHashMap.put(key, film);
    }

    public boolean hasFilm(Integer key) {
        if (filmHashMap.containsKey(key))
            return true;
        return false;
    }

    public Film getFilm(Integer key) {
        if (hasFilm(key)) return filmHashMap.get(key);
        return null;
    }

    public int getAmountFilms() {
        return filmHashMap.size();
    }

    public HashMap<Integer, Film> getFilmHashMap() {
        return filmHashMap;
    }

    public Film loadFilm(String filmId) {
        if (!filmHashMap.containsKey(Integer.parseInt(filmId))) {
            Request request3 = new Request("get", ">>",
                    "name/country/length/lang/image/content",
                    "Select * from Films where id = " + filmId + ";");
            RequestHandler.getInstance().sendMes(request3);
            String[] list = CurrentResponse.getInstance().getCurResponseArray();
            Film newFilm = new Film();
            newFilm.setFilmID(Integer.parseInt(filmId));
            newFilm.setFilmName(list[0]);
            newFilm.setFilmCountry(list[1]);
            newFilm.setFilmLength(list[2]);
            newFilm.setFilmLang(list[3]);
            newFilm.setFilmImagePath(list[4]);
            newFilm.setFilmContent(list[5]);
            FilmArray.getInstance().addFilm(newFilm, newFilm.getFilmID());
        }
        return filmHashMap.get(Integer.parseInt(filmId));
    }

    public VBox createFilmVBox(Film film, Seans curSeans, Stage parentStage) {

        if (curSeans.getDate() == null)
            curSeans.setDate(String.valueOf(LocalDate.now()));
        Label filmName = new Label(film.getFilmName());
        filmName.setId("new-orange");

        Button btn = new Button();
        Image im = new Image("file:resources/" + film.getFilmImagePath() + ".jpg", 150, 200, false, true);
        ImageView imv = new ImageView(im);
        imv.setFitHeight(200);
        imv.setFitWidth(150);
        btn.setGraphic(imv);
        btn.getStyleClass().add("button");
        btn.setPrefSize(150, 200);
        btn.setTextAlignment(TextAlignment.CENTER);
        btn.setOnAction(event -> {
            curSeans.setFilmID(film.getFilmID());
            Group root = new Group();
            Stage stage = new Stage();
            stage.setTitle("Third Stage");
            FilmInfoScene third = new FilmInfoScene(root, 750, 650, film, curSeans, parentStage, stage);
            third.fillScene();
            stage.setScene(third);
            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        });

        Label country = new Label(film.getFilmCountry());
        country.getStyleClass().add("item-title");

        VBox vBox = new VBox();
        vBox.setLayoutX(2);
        vBox.setLayoutY(2);
        vBox.setSpacing(3);
        vBox.getChildren().addAll(filmName, btn, country);

        return vBox;
    }
}
