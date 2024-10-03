import at.htl.snake.game.GameController;
import at.htl.snake.utils.KeyHandler;
import at.htl.snake.utils.Scenes;
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

    private static Stage popUpStage;
    private static final Deque<Scenes> popUpSceneStack = new ArrayDeque<>();

    public SceneManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.centerOnScreen();
        rootLayout = new BorderPane();
    }

    public void setScene(Scenes fxmlPath) {
        if (fxmlPath == null) {
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath.getPath()));
            Parent scene = loader.load();
            primaryStage.setTitle(fxmlPath.getTitle());
            sceneStack.push(scene);
            rootLayout.setCenter(scene);

            scene.setFocusTraversable(true);
            scene.setOnKeyPressed(keyEvent -> {
                KeyHandler.getInstance().keyPressed(keyEvent);
            });
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
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setSceneResizable(boolean resizable) {
        primaryStage.setResizable(resizable);
    }

    public void closeScene() {
        primaryStage.close();
    }

    public static void showPopUp(Scenes fxml) throws IOException {
        showPopUp(fxml, Starter.getPrimaryStage());
    }

    public static void showPopUp(Scenes fxml, Window parentWindow) throws IOException {
        if (popUpStage == null) {
            popUpStage = new Stage();
            popUpStage.initModality(Modality.APPLICATION_MODAL);
            popUpStage.initOwner(parentWindow);
        }

        setPopUpScene(fxml);
        popUpStage.centerOnScreen();
        popUpStage.showAndWait();
    }

    public static void goBackPopUp() throws IOException {
        if (popUpSceneStack.size() > 1) {
            popUpStage.hide(); // Hide the current pop-up stage
            popUpSceneStack.pop();
            setPopUpScene(popUpSceneStack.peek());
            popUpStage.show(); // Show the new pop-up stage
        } else {
            closePopUpWindow(Scenes.ESCMENU);
        }
    }

    public static void closePopUpWindow(Scenes escmenu) {
        if (popUpStage != null) {
            popUpStage.close();
            popUpStage = null;
        }

        if (escmenu.equals(Scenes.ESCMENU)) {
            GameController.resume();
        }
    }

    public static void setPopUpScene(Scenes fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Starter.class.getResource(fxml.getPath()));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root); // Erstellen Sie hier eine neue Scene
        popUpStage.setTitle(fxml.getTitle());
        popUpStage.setScene(scene);

        root.setOnKeyPressed(keyEvent -> {
            KeyHandler.getInstance().keyPressed(keyEvent);
        });

        // on close Request
        popUpStage.setOnCloseRequest(e -> {
            try {
                // this if for checking wich scene is there as a popup
                if (fxml != Scenes.ESCMENU) {
                    goBackPopUp();
                    e.consume();
                } else {
                    closePopUpWindow(Scenes.ESCMENU);
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        popUpSceneStack.push(fxml);
    }

    public static String getCurrentSceneTitle() {
        return Starter.getPrimaryStage().getTitle();
    }

    public static Scenes getCurrentScene() {
        return popUpSceneStack.peek();
    }
}
