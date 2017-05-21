package gui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.stage.Stage;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Group root = new Group();
        primaryStage.setTitle("First");
        MainScene first = new MainScene(root, 750, 650, primaryStage);
        first.fillScene();
        primaryStage.setScene(first);
        primaryStage.show();
    }


    public void launchMain() {
        launch();
    }
}
