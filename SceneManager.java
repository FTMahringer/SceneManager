import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * SceneManager is a singleton class that manages scene transitions and pop-up windows in the application.
 * It handles the showing, hiding, and moving between different scenes and pop-ups, and keeps track of the scene
 * stack for both main scenes and pop-up scenes.
 */
public class SceneManager {

    // Singleton instance of the SceneManager
    private static SceneManager instance = null;

    // Main application window (primary stage)
    private Stage primaryStage;

    // Stack to manage the history of scenes shown in the main stage
    private final Deque<Scenes> sceneStack = new ArrayDeque<>();

    // Stage for pop-up windows
    private Stage popUpStage;

    // Stack to manage the history of pop-up scenes
    private final Deque<Scenes> popUpSceneStack = new ArrayDeque<>();

    // Private constructor to enforce the singleton pattern
    private SceneManager() {
    }

    // Method to get the singleton instance of SceneManager
    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    /**
     * Displays a new scene in the primary stage.
     * @param fxml The enum representing the scene to be displayed.
     */
    public void showScene(Scenes fxml) {
        if (primaryStage == null) {
            primaryStage = new Stage();  // Create primary stage if it doesn't exist
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Starter.class.getResource(fxml.getPath()));  // Load the FXML file for the scene
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root);  // Create a new Scene object
            primaryStage.setTitle(fxml.getTitle());  // Set the window title
            sceneStack.push(fxml);  // Add the scene to the stack

            // Handle key press events for the scene
            scene.setOnKeyPressed(keyEvent -> {
                KeyHandler.getInstance().keyPressed(keyEvent);
            });

            primaryStage.setScene(scene);  // Set the scene to the stage
            primaryStage.centerOnScreen();  // Center the stage on the screen
            primaryStage.show();  // Show the stage
        } catch (IOException e) {
            throw new RuntimeException(e);  // Handle any loading errors
        }
    }

    /**
     * Goes back to the previous scene in the main scene stack.
     */
    public void goBack() {
        if (sceneStack.size() > 1) {
            sceneStack.pop();  // Remove the current scene from the stack
            showScene(sceneStack.peek());  // Show the previous scene
        }
    }

    /**
     * Gets the current scene from the scene stack.
     * @return The current scene.
     */
    public Scenes getCurrentScene() {
        return sceneStack.peek();
    }

    /**
     * Displays a pop-up window with the given scene.
     * @param fxml The enum representing the pop-up scene to be displayed.
     * @throws IOException If the FXML file can't be loaded.
     */
    public void showPopUp(Scenes fxml) throws IOException {
        showPopUp(fxml, primaryStage);  // Show the pop-up with the primary stage as the parent
    }

    /**
     * Displays a pop-up window with the given scene and a specified parent window.
     * @param fxml The enum representing the pop-up scene to be displayed.
     * @param parentWindow The parent window for the pop-up.
     * @throws IOException If the FXML file can't be loaded.
     */
    public void showPopUp(Scenes fxml, Window parentWindow) throws IOException {
        if (popUpStage == null) {
            popUpStage = new Stage();  // Create pop-up stage if it doesn't exist
            popUpStage.initModality(Modality.APPLICATION_MODAL);  // Make it modal (blocking input to parent)
            popUpStage.initOwner(parentWindow);  // Set the parent window
        }

        setPopUpScene(fxml);  // Set the scene for the pop-up
        popUpStage.centerOnScreen();  // Center the pop-up on the screen
        popUpStage.showAndWait();  // Show the pop-up and wait until it's closed
    }

    /**
     * Goes back to the previous pop-up scene or closes the pop-up if there's no previous scene.
     */
    public void goBackPopUp() {
        if (getCurrentScene().equals(Scenes.MAIN_MENU)) {
            closePopUpWindow();  // Close the pop-up if the current scene is MAIN_MENU
            popUpSceneStack.clear();  // Clear the pop-up stack
            return;
        }

        if (popUpSceneStack.size() > 1) {
            popUpStage.hide();  // Hide the current pop-up
            popUpSceneStack.pop();  // Remove the current pop-up scene from the stack
            assert popUpSceneStack.peek() != null;
            setPopUpScene(popUpSceneStack.peek());  // Set the previous pop-up scene
            popUpStage.show();  // Show the new pop-up
        } else {
            closePopUpWindow();  // Close the pop-up if there's no previous scene
        }
    }

    /**
     * Closes the pop-up window and clears the pop-up stack.
     */
    public void closePopUpWindow() {
        if (popUpStage != null) {
            popUpStage.close();  // Close the pop-up stage
            popUpStage = null;
        }
        popUpSceneStack.clear();  // Clear the pop-up stack
    }

    /**
     * Sets the pop-up scene and handles its display.
     * @param fxml The enum representing the pop-up scene.
     */
    public void setPopUpScene(Scenes fxml) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Starter.class.getResource(fxml.getPath()));
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root);  // Create a new Scene for the pop-up
            popUpStage.setTitle(fxml.getTitle());  // Set the title of the pop-up
            popUpStage.setScene(scene);  // Set the scene

            root.setOnKeyPressed(keyEvent -> {
                KeyHandler.getInstance().keyPressed(keyEvent);  // Handle key presses
            });

            // Handle closing the pop-up
            popUpStage.setOnCloseRequest(e -> {
                goBackPopUp();  // Go back to the previous pop-up scene
                e.consume();  // Prevent default close behavior
            });

            popUpSceneStack.push(fxml);  // Add the pop-up scene to the stack
        } catch (IOException e) {
            throw new RuntimeException(e);  // Handle loading errors
        }
    }

    /**
     * Gets the title of the current scene in the main stack.
     * @return The title of the current scene.
     */
    public String getCurrentSceneTitle() {
        return sceneStack.peek().getTitle();
    }

    /**
     * Gets the current pop-up scene.
     * @return The current pop-up scene.
     */
    public Scenes getCurrentPopUpScene() {
        return popUpSceneStack.peek();
    }

    /**
     * Returns the number of pop-up scenes in the stack.
     * @return The size of the pop-up scene stack.
     */
    public int getPopUpSceneStackSize() {
        return popUpSceneStack.size();
    }

    /**
     * Closes the primary stage.
     */
    public void close() {
        primaryStage.close();
    }

    /**
     * Returns the main scene stack.
     * @return The main scene stack.
     */
    public Deque<Scenes> getSceneStack() {
        return sceneStack;
    }

    /**
     * Returns the pop-up scene stack.
     * @return The pop-up scene stack.
     */
    public Deque<Scenes> getPopUpSceneStack() {
        return popUpSceneStack;
    }
}
