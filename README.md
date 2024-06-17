# SceneManager

This is the first release of my Library SceneManager.

It is for managing fxmls in the Scenes Enum
and showing and changing those Scenes.


## Authors

- [@FTMahringer](https://github.com/FTMahringer)


## Features

- Managing Scenes in a enum class.
- Switching scenes on the go.
- Creating popup windows, that prevent the closing of the "actual" window.
- Going back to the previous window


# Usage/Examples

To use this library, please create two classes:

### SceneManager.java and Scenes.java
```
Please see the SceneManager and Scenes.java files, and copy everything inside into the created file.
```

### To start using it, please use like this:
```java
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
```

In the Scenes class, each scene is managed like this:
```java
    MAIN_MENU("hello-view.fxml", "Menu")
```
The Scenes class is an enum class, that just holds al the used scenes, that are openable windows. (not included scenes in fxmls)

## Used By

This small library is currently used by my class for various javafx related projects, like:

- QuizApp
- Minesweeper
- Checkers

Just some small projects (or games) made in school.


Please tell me if i should add more functionalities to this small helper Programm.
