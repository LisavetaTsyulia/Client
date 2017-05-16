package gui;

import Film.Film;
import connection.CurrentResponse;
import connection.Request;
import connection.RequestHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class ThirdScene extends Scene {
    private Film film;
    private ComponentCreator componentCreator;
    private Group root;
    private String dateFromClient;

    public ThirdScene(Parent root, double width, double height, Film film, String dateFromClient) {
        super(root, width, height);
        this.getStylesheets().add("css/styles.css");
        this.film = film;
        this.dateFromClient = dateFromClient;
        this.root = (Group) root;
        componentCreator = new ComponentCreator();
    }

    public void fillScene() {
        VBox pageBox = componentCreator.vboxCreator();
        Label filmName = componentCreator.labelCreator(film.getFilmName());
        Separator separator = componentCreator.sepHCreator(600);

        GridPane seansGridPane = new GridPane();
        ScrollPane seansScrollPane = componentCreator.scrollPaneCreator();
        seansScrollPane.setPrefSize(650, 150);
        seansScrollPane.setContent(seansGridPane);
        seansScrollPane.setPrefViewportHeight(150);
        seansScrollPane.setPrefViewportWidth(650);

        Request request = new Request("get", ">>", "id/name",
                "Select * from Cinemas ;");
        RequestHandler.getInstance().sendMes(request);

        String[] cinemas = null;
        boolean isTrue = true;
        while (isTrue)
            if (CurrentResponse.getInstance().getCurrentResponse() != null) {
                cinemas = CurrentResponse.getInstance().getCurrentResponse().getArray();
                isTrue = false;
            }
        CurrentResponse.getInstance().setCurrentResponse(null);
        HashMap<Integer, String> cinemasHmap = new HashMap<>();

        for(int i = 0 ; i < cinemas.length; i += 2) {
            cinemasHmap.put(Integer.parseInt(cinemas[i]), cinemas[i+1]);
        }

        Set set = cinemasHmap.entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry)iterator.next();
            int curColIndex = 1;

            Request request2 = new Request("get", ">>", "time",
                    "Select time from seans where date = '" + dateFromClient +
                            "' AND filmId = " + film.getFilmID() + " AND cinemaID = " + mentry.getKey() + ";");
            RequestHandler.getInstance().sendMes(request2);

            isTrue = true;
            while (isTrue) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                }
                if (CurrentResponse.getInstance().getCurrentResponse() != null) {
                    if (!(";".equals(CurrentResponse.getInstance().getCurrentResponse().toString()))) {
                        seansGridPane.add(new Label((String) mentry.getValue()), curColIndex, (Integer) mentry.getKey());
                        String[] seans = CurrentResponse.getInstance().getCurrentResponse().getArray();
                        if (seans.length != 0)
                            for (String time :
                                    seans) {
                                Button btn = new Button(time);
                                btn.getStyleClass().add("button");
                                seansGridPane.add(btn, ++curColIndex, (Integer) mentry.getKey());
                                System.out.println(time);
                            }
                    }
                    isTrue = false;
                }
            }
            CurrentResponse.getInstance().setCurrentResponse(null);
        }

        pageBox.getChildren().addAll(filmName, createFilmInfo(), separator, seansScrollPane);
        root.getChildren().addAll(pageBox);
    }

    private HBox createFilmInfo() {
        HBox filmBox = componentCreator.hBoxCreator();
        Label filmContent = new Label(film.getFilmContent());
        filmContent.setWrapText(true);
        filmContent.setPrefWidth(250);

        Label content = new Label("Content: ");
        filmContent.setAlignment(Pos.TOP_RIGHT);

        GridPane gridPaneInfo = new GridPane();
        gridPaneInfo.setLayoutX(20);
        gridPaneInfo.setLayoutY(20);
        gridPaneInfo.setCursor(Cursor.TEXT);
        gridPaneInfo.setStyle("-fx-font:bold 14pt Arial;-fx-text-fill:#a0522d;");
        gridPaneInfo.setVgap(10);
        gridPaneInfo.setHgap(10);

        gridPaneInfo.add(new Label("Country: "), 1, 1);
        gridPaneInfo.add(new Label("Length: "), 1, 2);
        gridPaneInfo.add(new Label("Lang: "), 1, 3);
        gridPaneInfo.add(content, 1, 4);

        gridPaneInfo.add(new Label(film.getFilmCountry()), 2, 1);
        gridPaneInfo.add(new Label(film.getFilmLength()), 2, 2);
        gridPaneInfo.add(new Label(film.getFilmLang()), 2, 3);
        gridPaneInfo.add(filmContent, 2, 4);

        Image im = new Image("file:resources/" + film.getFilmImagePath() + ".jpg", 200, 300, false, true);
        ImageView imv = new ImageView(im);

        filmBox.getChildren().addAll(imv, gridPaneInfo);
        return filmBox;
    }
}
