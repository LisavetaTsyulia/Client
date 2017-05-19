package gui;

import Seans.Seans;
import connection.CurrentResponse;
import connection.Request;
import connection.RequestHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.Arrays;

public class CinemaScene extends Scene {
    private Group root;
    private ComponentCreator componentCreator;
    private CheckBox[] cbs;
    private Seans curSeans;
    private int rows;
    private int cols;
    private String seats;
    
    public CinemaScene(Parent root, double width, double height, Seans curSeans) {
        super(root, width, height);
        this.getStylesheets().add("css/styles.css");
        this.root = (Group) root;
        this.curSeans = curSeans;
        componentCreator = new ComponentCreator();
    }

    public void fillScene() {
        loadSeats();
        VBox cinemaBox = componentCreator.vboxCreator();
        cinemaBox.setAlignment(Pos.CENTER);
        cinemaBox.setLayoutX(100);
        Button buyButton = new Button("Заказать");
        buyButton.setId("big-yellow");
        buyButton.setOnAction(event -> {
            String [] seatsChanged = new String[cols * rows];
            for (int i = 0; i < cbs.length ; i++) {
                if (cbs[i].isSelected() || cbs[i].isDisabled()) {
                    seatsChanged[i] = "1";
                } else seatsChanged[i] = "0";
            }
            curSeans.setSeats(String.join("", seatsChanged));
            updateSeatsInDB();
        });
        Image screenImg = new Image("file:resources/screen.png", 500, 100, false, true);
        ImageView imv = new ImageView(screenImg);
        imv.setFitHeight(100);
        imv.setFitWidth(500);
        GridPane seatsGridPane = new GridPane();
        seatsGridPane.setAlignment(Pos.CENTER);
        seatsGridPane.setLayoutX(10);
        seatsGridPane.setLayoutY(10);
        seatsGridPane.setCursor(Cursor.TEXT);
        seatsGridPane.setStyle("-fx-font:bold 14pt Arial;-fx-text-fill:#a0522d;");
        seatsGridPane.setVgap(8);
        seatsGridPane.setHgap(13);
        cinemaBox.getChildren().addAll(buyButton, imv, seatsGridPane);
        cbs = new CheckBox[rows * cols];
        for (int i = 0; i < rows * cols; i ++) {
            cbs[i] = new CheckBox();
            if (String.valueOf(seats.charAt(i)).equals("1")) {
                cbs[i].setDisable(true);
            }
            cbs[i].setSelected(false);
            cbs[i].setStyle("-fx-font-size: 18;\n" +
                            "-fx-font-weight: bold;\n");
        }
        for (int i = 0; i < rows; i ++){
            for (int j = 0; j < cols; j++) {
                seatsGridPane.add(cbs[i * cols + j], j, i);
            }
        }

        root.getChildren().addAll(cinemaBox);
    }

    private void loadSeats() {
        Request request = new Request("get", ">>", "rows/cols",
                "Select rows, cols from Cinemas where id = " + curSeans.getCinemaID() + ";");
        RequestHandler.getInstance().sendMes(request);

        String[] seatsArr = CurrentResponse.getInstance().getCurResponseArray();
        rows = Integer.parseInt(seatsArr[0]);
        cols = Integer.parseInt(seatsArr[1]);
        getSeatsFromDB();
    }

    private void getSeatsFromDB() {
        Request request = new Request("get", ">>", "seats",
                "Select seats from seans where cinemaID = " + curSeans.getCinemaID() +
                        " AND time = '" + curSeans.getTime() + "' AND date = '" + curSeans.getDate() + "' ;");
        RequestHandler.getInstance().sendMes(request);
        seats = CurrentResponse.getInstance().getCurResponseArray()[0];
    }

    private void updateSeatsInDB() {
        Request request = new Request("send", ">>", "seats",
                "Update seans set seats  = '" + curSeans.getSeats() + "' where cinemaID = " + curSeans.getCinemaID()
                        + " AND time = '" + curSeans.getTime() + "' AND date = '" + curSeans.getDate() + "';");
        RequestHandler.getInstance().sendMes(request);
    }
}
