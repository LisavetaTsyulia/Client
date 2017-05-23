package gui;

import Seans.Seans;
import connection.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public class CinemaScene extends Scene {
    private Group root;
    private Seans curSeans;
    private Stage parentStage;
    private ComponentCreator componentCreator;
    private CheckBox[] cbs;
    private int rows;
    private int cols;
    private String seats;
    private String seatNums = "";
    private GridPane seatsGridPane;

    public CinemaScene(Parent root, double width, double height, Seans curSeans, Stage parentStage) {
        super(root, width, height);
        this.root = (Group) root;
        this.curSeans = curSeans;
        this.parentStage = parentStage;
        this.getStylesheets().add("css/styles.css");
        componentCreator = new ComponentCreator();
    }

    public void fillScene(Stage stage) {
        seatsGridPane = new GridPane();
        seatsGridPane.setAlignment(Pos.CENTER);
        seatsGridPane.setLayoutX(10);
        seatsGridPane.setLayoutY(10);
        seatsGridPane.setCursor(Cursor.TEXT);
        seatsGridPane.setStyle("-fx-font:bold 14pt Arial;-fx-text-fill:#a0522d;");
        seatsGridPane.setVgap(8);
        seatsGridPane.setHgap(13);
        loadSeats();
        VBox cinemaBox = componentCreator.vboxCreator();
        cinemaBox.setAlignment(Pos.CENTER);
        cinemaBox.setLayoutX(100);

        Button backBtn = componentCreator.backBtnCreator(parentStage);
        Button buyButton = new Button("Заказать");
        buyButton.setId("big-yellow");
        buyButton.setOnAction(event -> {
            String [] seatsChanged = new String[cols * rows];
            for (int i = 0; i < cbs.length ; i++) {
                if (cbs[i].isSelected() || cbs[i].isDisabled()) {
                    if (cbs[i].isSelected()) seatNums += i + ",";
                    seatsChanged[i] = "1";
                } else seatsChanged[i] = "0";
            }
            curSeans.setSeats(String.join("", seatsChanged));
            String answer;
            if (updateSeatsInDB(seatNums)) {
                answer = "Билеты успешно заказаны";
            } else answer = "Извините, но эти билеты больше недоступны";
            seatNums = "";
            createPopupWindow(stage, answer, seatsGridPane);
        });

        HBox backBtnAndBuyBtn = componentCreator.hBoxCreator();
        backBtnAndBuyBtn.setAlignment(Pos.BASELINE_LEFT);
        backBtnAndBuyBtn.setSpacing(100);
        backBtnAndBuyBtn.getChildren().addAll(backBtn, buyButton);

        Image screenImg = new Image("file:resources/screen.png", 500, 100, false, true);
        ImageView imv = new ImageView(screenImg);
        imv.setFitHeight(100);
        imv.setFitWidth(500);

        cinemaBox.getChildren().addAll(backBtnAndBuyBtn, imv, seatsGridPane);
        createGridPane();
        updateCheckBox();
        fillGridPane();

        root.getChildren().addAll(cinemaBox);
    }

    private void createPopupWindow(Stage stage, String answer, GridPane seatsGridPane) {
        final Popup popup = new Popup();
        popup.setWidth(350);
        popup.setHeight(250);
        popup.setX(stage.getX() + stage.getWidth() / 2 - popup.getWidth() / 2);
        popup.setY(stage.getY() + stage.getHeight() / 2 - popup.getHeight() / 2);

        Label lbl = new Label(answer);
        lbl.setWrapText(true);
        lbl.setAlignment(Pos.BASELINE_CENTER);
        Button btn = new Button("OK");
        btn.setOnAction(event -> {
            seatsGridPane.getChildren().clear();
            getSeatsFromDB();
            updateCheckBox();
            fillGridPane();
            popup.hide();

        });
        VBox vertical = componentCreator.vboxCreator();
        vertical.setAlignment(Pos.CENTER);
        vertical.setPrefSize(300, 200);
        vertical.setStyle(" -fx-background-color: #e2e2a0;" +
                            " -fx-background-radius: 20px");
        vertical.getChildren().addAll(lbl, btn);

        popup.getContent().addAll(vertical);
        popup.show(stage);
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

    private boolean updateSeatsInDB(String seatNums) {
        Request request = new Request("send", ">>", "seats",
                "Update seans set seats  = '" + curSeans.getSeats() + "' where cinemaID = " + curSeans.getCinemaID()
                        + " AND time = '" + curSeans.getTime() + "' AND date = '" + curSeans.getDate() + "';/" + seatNums
                        + "/Select seats from seans where cinemaID = " + curSeans.getCinemaID()
                        + " AND time = '" + curSeans.getTime() + "' AND date = '" + curSeans.getDate() + "';");
        RequestHandler.getInstance().sendMes(request);
        return Boolean.parseBoolean(CurrentResponse.getInstance().getCurResponseArray()[0]);
    }

    private void createGridPane() {
        cbs = new CheckBox[rows * cols];
        for (int i = 0; i < rows * cols; i ++) {
            cbs[i] = new CheckBox();
        }
    }

    private void fillGridPane() {
        for (int i = 0; i < rows; i ++){
            for (int j = 0; j < cols; j++) {
                seatsGridPane.add(cbs[i * cols + j], j, i);
            }
        }
    }

    private void updateCheckBox() {
        for (int i = 0; i < rows * cols; i ++) {
            if (String.valueOf(seats.charAt(i)).equals("1")) {
                cbs[i].setDisable(true);
            }
            cbs[i].setSelected(false);
            cbs[i].setStyle("-fx-font-size: 18;\n" +
                    "-fx-font-weight: bold;\n");
        }
    }
}
