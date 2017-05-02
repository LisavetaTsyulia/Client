package gui;

import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SecondScene extends Scene{
    private Group root;
    public SecondScene(Group root, int width, int height) {
        super(root, width, height);
        this.root = root;
        this.getStylesheets().add("css/styles.css");
    }

    public void fillScene(String dateFromClient) {
        VBox vBox = new VBox();
        vBox.setLayoutX(20);
        vBox.setLayoutY(20);
        vBox.setSpacing(10);

        //DateFormat dateFormat = new SimpleDateFormat("LLLL", Locale.getDefault());
        DateFormat dateFormat = new SimpleDateFormat("dd.MM");
        Date date = new Date();
        //Label label = new Label(dateFormat.format(date));
        Label label = new Label(dateFromClient);
        label.getStyleClass().add("header");

        Separator sepH = new Separator();
        sepH.setPrefWidth(600);
        sepH.setOrientation(Orientation.HORIZONTAL);
        sepH.getStyleClass().add("scroll-pane");


        Label lblFilm1 = new Label("Фильм1");
        lblFilm1.getStyleClass().add("item-title");
        Button btnFilm1 = new Button("Картинка");
        /*
        Image im=new Image(this.getClass().getResource("image.png").toString());
        ImageView imv=new ImageView(im);
        imv.setFitHeight(50);
        imv.setFitWidth(50);
        btnFilm1.setGraphic(imv);
        */
        btnFilm1.getStyleClass().add("button");
        btnFilm1.setPrefSize(150, 200);
        btnFilm1.setTextAlignment(TextAlignment.CENTER);
        VBox filmBox = new VBox();
        filmBox.setLayoutX(2);
        filmBox.setLayoutY(2);
        filmBox.setSpacing(3);
        filmBox.getChildren().addAll(lblFilm1, btnFilm1);

        Label lblFilm2 = new Label("Фильм2");
        lblFilm2.getStyleClass().add("item-title");
        Button btnFilm2 = new Button("Картинка2");
        btnFilm2.getStyleClass().add("button");
        btnFilm2.setPrefSize(150, 200);
        btnFilm2.setTextAlignment(TextAlignment.CENTER);
        VBox filmBox2 = new VBox();
        filmBox.setLayoutX(2);
        filmBox.setLayoutY(2);
        filmBox.setSpacing(3);
        filmBox.getChildren().addAll(lblFilm2, btnFilm2);

        GridPane gridPane = new GridPane();
        gridPane.setLayoutX(20);
        gridPane.setLayoutY(20);
        gridPane.setCursor(Cursor.TEXT);
        gridPane.setStyle("-fx-font:bold 14pt Arial;-fx-text-fill:#a0522d;");
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.add(filmBox, 1, 1);
        gridPane.add(filmBox2, 1, 2);


        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setLayoutX(10);
        scrollPane.setLayoutY(10);
        scrollPane.setCursor(Cursor.CLOSED_HAND);
        scrollPane.setPrefSize(600, 400);
        scrollPane.setTooltip(new Tooltip("Отправка данных"));
        scrollPane.setContent(gridPane);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPannable(true);
        scrollPane.setPrefViewportHeight(400);
        scrollPane.setPrefViewportWidth(600);

        vBox.getChildren().addAll(label, sepH, scrollPane);
        root.getChildren().addAll(vBox);
    }

    public VBox createFilmSet(String lable, String btnName) {
        Label label = new Label(lable);
        label.getStyleClass().add("item-title");

        Button btn = new Button(btnName);
        btn.getStyleClass().add("button");
        btn.setPrefSize(150, 200);
        btn.setTextAlignment(TextAlignment.CENTER);

        VBox vBox = new VBox();
        vBox.setLayoutX(2);
        vBox.setLayoutY(2);
        vBox.setSpacing(3);
        vBox.getChildren().addAll(label, btn);
        return vBox ;
    }
}
