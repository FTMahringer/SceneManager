import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

public class SceneManager {

    private static SceneManager instance = null;

    private Stage primaryStage;
    private final Deque<Scenes> sceneStack = new ArrayDeque<>();

    private Stage popUpStage;
    private final Deque<Scenes> popUpSceneStack = new ArrayDeque<>();

    private SceneManager() {
    }

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    public void showScene(Scenes fxml) {
        if (primaryStage == null) {
            primaryStage = new Stage();
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Starter.class.getResource(fxml.getPath()));
            
            Scene scene = new Scene(fxmlLoader.load());
            primaryStage.setTitle(fxml.getTitle());
            sceneStack.push(fxml);
            scene.setOnKeyPressed(keyEvent -> KeyHandler.getInstance().keyPressed(keyEvent));
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void goBack() {
        if (sceneStack.size() > 1) {
            sceneStack.pop();
            showScene(sceneStack.peek());
        }
    }

    public Scenes getCurrentScene() {
        return sceneStack.peek();
    }


    public void showPopUp(Scenes fxml) throws IOException {
        showPopUp(fxml, primaryStage);
    }

    public void showPopUp(Scenes fxml, Window parentWindow) throws IOException {
        if (popUpStage == null) {
            popUpStage = new Stage();
            popUpStage.initModality(Modality.APPLICATION_MODAL);
            popUpStage.initOwner(parentWindow);
        }
        setPopUpScene(fxml);
        popUpStage.centerOnScreen();
        popUpStage.showAndWait();
    }

    public void goBackPopUp() {
        if (getCurrentScene().equals(Scenes.MAIN_MENU)) {
            closePopUpWindow();
            popUpSceneStack.clear();
            return;
        }
        if (popUpSceneStack.size() > 1) {
            popUpStage.hide(); // Hide the current pop-up stage
            popUpSceneStack.pop();
            assert popUpSceneStack.peek() != null;
            setPopUpScene(popUpSceneStack.peek());
            popUpStage.show(); // Show the new pop-up stage
        } else {
            closePopUpWindow();
        }
    }

    public void closePopUpWindow() {
        if (popUpStage != null) {
            popUpStage.close();
            popUpStage = null;
        }
        popUpSceneStack.clear();
    }

    public void setPopUpScene(Scenes fxml) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Starter.class.getResource(fxml.getPath()));
            Parent root = fxmlLoader.load();

            popUpStage.setTitle(fxml.getTitle());
            popUpStage.setScene(new Scene(root));

            root.setOnKeyPressed(keyEvent -> KeyHandler.getInstance().keyPressed(keyEvent));

            // on close Request
            popUpStage.setOnCloseRequest(e -> {
                goBackPopUp();
                e.consume();
            });

            popUpSceneStack.push(fxml);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getCurrentSceneTitle() {
        return sceneStack.peek().getTitle();
    }

    public Scenes getCurrentPopUpScene() {
        return popUpSceneStack.peek();
    }

    public int getPopUpSceneStackSize() {
        return popUpSceneStack.size();
    }

    public void close() {
        primaryStage.close();
    }

    public Deque<Scenes> getSceneStack() {
        return sceneStack;
    }

    public Deque<Scenes> getPopUpSceneStack() {
        return popUpSceneStack;
    }
}

