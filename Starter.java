package at.fmahring.scenemanager;

import javafx.application.Application;
import javafx.stage.Stage;

public class Starter extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        SceneManager sceneManager = new SceneManager(stage);
        sceneManager.setScene(Scenes.MAIN_MENU);
        sceneManager.setSceneSize(800, 600);
        sceneManager.showScene();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
