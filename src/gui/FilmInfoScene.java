package gui;

import Film.Film;
import Seans.Seans;
import connection.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;


public class FilmInfoScene extends Scene {
    private Group root;
    private Seans curSeans;
    private Stage parentStage;
    private Stage thisStage;
    private ComponentCreator componentCreator;
    private Film film;

    public FilmInfoScene(Parent root, double width, double height, Film film, Seans curSeans, Stage parentStage, Stage thisStage) {
        super(root, width, height);
        this.film = film;
        this.curSeans = curSeans;
        this.root = (Group) root;
        this.parentStage = parentStage;
        this.thisStage = thisStage;
        this.getStylesheets().add("css/styles.css");
        componentCreator = new ComponentCreator();
    }

    public void fillScene() {
        Button backBtn = componentCreator.backBtnCreator(parentStage);
        Label filmName = componentCreator.labelCreator(film.getFilmName());
        filmName.setId("beautiful");

        HBox nameAndBackBtn = componentCreator.hBoxCreator();
        nameAndBackBtn.setSpacing(100);
        nameAndBackBtn.setAlignment(Pos.BASELINE_LEFT);
        nameAndBackBtn.getChildren().addAll(backBtn, filmName);

        Separator separator = componentCreator.sepHCreator(650);

        GridPane seansGridPane = new GridPane();
        seansGridPane.setLayoutX(20);
        seansGridPane.setLayoutY(20);
        seansGridPane.setCursor(Cursor.TEXT);
        seansGridPane.setStyle("-fx-font:bold 14pt Arial;-fx-text-fill:#a0522d;");
        seansGridPane.setVgap(10);
        seansGridPane.setHgap(10);

        ScrollPane seansScrollPane = componentCreator.scrollPaneCreator();
        seansScrollPane.setPrefSize(650, 150);
        seansScrollPane.setContent(seansGridPane);
        seansScrollPane.setPrefViewportHeight(150);
        seansScrollPane.setPrefViewportWidth(650);

        Request request = new Request("get", ">>", "id/name",
                "Select * from Cinemas ;");
        RequestHandler.getInstance().sendMes(request);

        String[] cinemas = CurrentResponse.getInstance().getCurResponseArray();
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
                    "Select time from seans where date = '" + curSeans.getDate() +
                            "' AND filmId = " + film.getFilmID() + " AND cinemaID = " + mentry.getKey() + " order by time;");
            RequestHandler.getInstance().sendMes(request2);

            boolean isTrue = true;
            while (isTrue) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                }
                if (CurrentResponse.getInstance().getCurrentResponse() != null) {
                    if (!(";".equals(CurrentResponse.getInstance().getCurrentResponse().toString()))) {
                        HBox cinemaBox = componentCreator.hBoxCreator();
                        seansGridPane.add(new Label((String) mentry.getValue()), curColIndex, (Integer) mentry.getKey());
                        String[] seans = CurrentResponse.getInstance().getCurrentResponse().getArray();
                        for (String time :
                                seans) {
                            Button btn = new Button(time);
                            btn.setOnAction(event -> {
                                File f = new File("resources/login.txt");
                                if(f.exists() && !f.isDirectory()) {
                                    curSeans.setTime(time);
                                    Group root = new Group();
                                    Stage stage = new Stage();
                                    stage.setTitle("Seans Stage");
                                    Integer cinemaID = (Integer) mentry.getKey();
                                    curSeans.setCinemaID(cinemaID);
                                    CinemaScene cinemaScene = new CinemaScene(root, 750, 650, curSeans, thisStage);
                                    cinemaScene.fillScene(stage);
                                    stage.setScene(cinemaScene);
                                    stage.show();
                                    ((Node) (event.getSource())).getScene().getWindow().hide();
                                } else {
                                    Group root = new Group();
                                    Stage stage = new Stage();
                                    stage.setTitle("Authorize Stage");
                                    AuthorizeScene authorizeScene = new AuthorizeScene(root, 750, 650, thisStage);
                                    authorizeScene.fillScene();
                                    stage.setScene(authorizeScene);
                                    stage.show();
                                    ((Node) (event.getSource())).getScene().getWindow().hide();
                                }

                            });
                            btn.getStyleClass().add("button");
                            if (curSeans.getDate().equals(String.valueOf(LocalDate.now()))) {
                                if (!compareTime(time)) {
                                    btn.setDisable(true);
                                }
                            }
                            cinemaBox.getChildren().add(btn);
                        }
                        seansGridPane.add(cinemaBox, ++curColIndex, (Integer) mentry.getKey());
                    }
                    isTrue = false;
                }
            }
            CurrentResponse.getInstance().setCurrentResponse(null);
        }

        VBox pageBox = componentCreator.vboxCreator();
        pageBox.getChildren().addAll(nameAndBackBtn, createFilmInfo(), separator, seansScrollPane);
        root.getChildren().addAll(pageBox);
    }

    private boolean compareTime(String time) {
        String startTime = time;
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        String endTime = new SimpleDateFormat("HH:mm:ss").format(calendar.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        try {
            Date d1 = sdf.parse(startTime);
            Date d2 = sdf.parse(endTime);
            long elapsed = d2.getTime() - d1.getTime();
            if (elapsed < 0)
                return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
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
