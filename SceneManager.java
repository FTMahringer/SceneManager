import at.ftmahringer.frameworkfirst.utils.Scenes;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

public class SceneManager {
    private final Stage primaryStage;
    private final BorderPane rootLayout;
    private static final Deque<Parent> sceneStack = new ArrayDeque<>();

    private static String PopUpStageTitle;

    public SceneManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
        //primaryStage.initStyle(StageStyle.UNDECORATED);
        rootLayout = new BorderPane();
    }

    public void setScene(Scenes fxmlPath) {
        // test if the fxmlPath is valid
        if (fxmlPath == null) {
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath.getPath()));
            Parent scene = loader.load();
            primaryStage.setTitle(fxmlPath.getTitle());
            sceneStack.push(scene);
            rootLayout.setCenter(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goBack() {
        if (sceneStack.size() > 1) {
            sceneStack.pop();
            rootLayout.setCenter(sceneStack.peek());
        }
    }

    public void showScene() {
        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setTitle(String title) {
        primaryStage.setTitle(title);
    }

    public void setSceneSize(double width, double height) {
        primaryStage.setWidth(width);
        primaryStage.setHeight(height);
    }

    public void setSceneMinSize(double minWidth, double minHeight) {
        primaryStage.setMinWidth(minWidth);
        primaryStage.setMinHeight(minHeight);
    }

    public void setSceneMaxSize(double maxWidth, double maxHeight) {
        primaryStage.setMaxWidth(maxWidth);
        primaryStage.setMaxHeight(maxHeight);
    }

    public void setSceneResizable(boolean resizable) {
        primaryStage.setResizable(resizable);
    }

    public void closeScene() {
        primaryStage.close();
    }

    public static void PopUpWindow(Scenes fxml, String title, Window parentWindow) throws IOException {
        Stage stage = new Stage();
        stage.setTitle(title);
        PopUpStageTitle = title;
        stage.initModality(Modality.APPLICATION_MODAL); // or Modality.WINDOW_MODAL
        stage.initOwner(parentWindow);
        FXMLLoader fxmlLoader = new FXMLLoader(Starter.class.getResource(fxml.getPath()));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.showAndWait();
    }

    public static String getPopUpStageTitle() {
        return PopUpStageTitle;
    }
}
