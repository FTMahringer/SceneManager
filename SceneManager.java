import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class SceneManager {

    private static volatile SceneManager instance = null; // Thread-safe Singleton

    private Stage primaryStage;
    private final Deque<Scenes> sceneStack = new ArrayDeque<>();
    private Stage popUpStage;
    private final Deque<Scenes> popUpSceneStack = new ArrayDeque<>();

    // Cache for scenes and pop-ups
    private final Map<Scenes, Parent> sceneCache = new HashMap<>();
    private final Map<Scenes, Parent> popUpCache = new HashMap<>(); // Cache for pop-ups

    private SceneManager() {}

    // Thread-safe Singleton
    public static SceneManager getInstance() {
        if (instance == null) {
            synchronized (SceneManager.class) {
                if (instance == null) {
                    instance = new SceneManager();
                }
            }
        }
        return instance;
    }

    // Method to show scenes (includes lazy loading and transition)
    public void showScene(Scenes fxml) {
        if (primaryStage == null) {
            primaryStage = new Stage();
        }
        try {
            Parent root = loadScene(fxml); // Lazy load the scene
            Scene scene = new Scene(root);
            applySceneTransition(scene); // Apply scene transition

            primaryStage.setTitle(fxml.getTitle());
            sceneStack.push(fxml);

            scene.setOnKeyPressed(keyEvent -> KeyHandler.getInstance().keyPressed(keyEvent));
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
            primaryStage.show();
        } catch (IOException e) {
            showErrorDialog("Error loading scene", e.getMessage());
        }
    }

    // Lazy load FXML scenes and cache them
    private Parent loadScene(Scenes scene) throws IOException {
        if (!sceneCache.containsKey(scene)) {
            FXMLLoader fxmlLoader = new FXMLLoader(Starter.class.getResource(scene.getPath()));
            Parent root = fxmlLoader.load();
            sceneCache.put(scene, root); // Cache the loaded scene
        }
        return sceneCache.get(scene); // Return the cached scene
    }

    // Apply scene transitions (e.g., fade in effect)
    private void applySceneTransition(Scene scene) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), scene.getRoot());
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }

    // Method to show pop-ups (with lazy loading)
    public void showPopUp(Scenes fxml) {
        showPopUp(fxml, primaryStage);
    }

    public void showPopUp(Scenes fxml, Window parentWindow) {
        if (popUpStage == null) {
            popUpStage = new Stage();
            popUpStage.initModality(Modality.APPLICATION_MODAL);
            popUpStage.initOwner(parentWindow);
        }
        setPopUpScene(fxml); // Lazy load pop-up scene
        popUpStage.centerOnScreen();
        popUpStage.showAndWait();
    }

    // Lazy load pop-up FXML and cache it (similar to scenes)
    private void setPopUpScene(Scenes fxml) {
        try {
            Parent root = loadPopUp(fxml); // Lazy load the pop-up
            popUpStage.setTitle(fxml.getTitle());
            popUpStage.setScene(new Scene(root));

            root.setOnKeyPressed(keyEvent -> KeyHandler.getInstance().keyPressed(keyEvent));

            popUpStage.setOnCloseRequest(e -> {
                goBackPopUp();
                e.consume();
            });

            popUpSceneStack.push(fxml);
        } catch (IOException e) {
            showErrorDialog("Error loading pop-up", e.getMessage());
        }
    }

    // Lazy load pop-ups and cache them
    private Parent loadPopUp(Scenes scene) throws IOException {
        if (!popUpCache.containsKey(scene)) {
            FXMLLoader fxmlLoader = new FXMLLoader(Starter.class.getResource(scene.getPath()));
            Parent root = fxmlLoader.load();
            popUpCache.put(scene, root); // Cache the loaded pop-up
        }
        return popUpCache.get(scene); // Return the cached pop-up
    }

    // Error dialog for scene/pop-up loading issues
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Go back to the previous scene (unchanged)
    public void goBack() {
        if (sceneStack.size() > 1) {
            sceneStack.pop();
            showScene(sceneStack.peek());
        }
    }

    public Scenes getCurrentScene() {
        return sceneStack.peek();
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

    public String getCurrentSceneTitle() {
        assert sceneStack.peek() != null;
        return sceneStack.peek().getTitle();
    }

    public Scenes getCurrentPopUpScene() {
        return popUpSceneStack.peek();
    }

    public int getPopUpSceneStackSize() {
        return popUpSceneStack.size();
    }

    public Deque<Scenes> getSceneStack() {
        return sceneStack;
    }

    public Deque<Scenes> getPopUpSceneStack() {
        return popUpSceneStack;
    }

    // Close all pop-ups when main window is closed
    public void closeAllPopUps() {
        if (popUpStage != null) {
            popUpStage.close();
            popUpStage = null;
        }
        popUpSceneStack.clear();
    }

    // Close both main stage and all pop-ups
    public void close() {
        if (primaryStage != null) {
            primaryStage.close();
        }
        closeAllPopUps();
    }
}
