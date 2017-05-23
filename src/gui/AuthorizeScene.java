package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;

public class AuthorizeScene extends Scene{
    private Group root;
    private Stage parentStage;
    private ComponentCreator componentCreator;

    public AuthorizeScene(Parent root, double width, double height, Stage parentStage) {
        super(root, width, height);
        this.root = (Group) root;
        this.parentStage = parentStage;
        this.getStylesheets().add("css/styles.css");
        componentCreator = new ComponentCreator();
    }

    public void fillScene() {
        Button backBtn = componentCreator.backBtnCreator(parentStage);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setPrefSize(500, 275);

        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 30));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("User Name:");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);

        Label email = new Label("Email:");
        grid.add(email, 0, 3);

        TextField emailTextField = new TextField();
        grid.add(emailTextField, 1, 3);

        Button btn = new Button("Sign in");
        btn.setOnAction(event ->  {
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("resources/login.txt"), "utf-8"))) {
                writer.write(userTextField.getText() + "\r\n");
                writer.write(pwBox.getText() + "\r\n");
                writer.write(emailTextField.getText() + "\r\n");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("somethingIsWrongWithFile(");
            }
            btn.setStyle(" -fx-background-color: #e2748c;");
        });
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 5);

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.TOP_LEFT);
        vBox.getChildren().addAll(backBtn,grid);

        root.getChildren().addAll(vBox);
    }
}
