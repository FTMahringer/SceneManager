# SceneManager

This is the first release of my Library SceneManager and CustomAlerts.

It is for managing fxmls in the Scenes Enum
and showing and changing those Scenes.

You need to implement the code for it to function properly. 
In the enum, please put your own scenes infos.


## Authors

- [@FTMahringer](https://github.com/FTMahringer)


## Features

- Managing Scenes in a enum class.
- Switching scenes on the go.
- Creating popup windows, that prevent the closing of the "actual" window.
- Going back to the previous window
- Getting Alerts running and showing easier


# Usage/Examples

To use this library, please create three classes:

### SceneManager.java and Scenes.java
```
Please see the SceneManager and Scenes.java files, and copy everything inside into the created file.
```

### To start using it, please use like this:
```java
public class Starter extends Application {

    static Stage primaryStage;
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Starter.primaryStage = primaryStage;
        SceneManager sceneManager = new SceneManager(primaryStage);
        sceneManager.setScene(Scenes.LOGIN);
        sceneManager.showScene();
    }
}
```
The getPrimaryStage method is for later, when you want to change scenes in the programm, to have it all run on one stage.

It has one problem. The scene doesnt get centered on the screen. It gets the center location from the previous scene. And if that was smaller, then it gets offset.


In the Scenes class, each scene is managed like this:
```java
    MAIN_MENU("hello-view.fxml", "Menu")
```
The Scenes class is an enum class, that just holds al the used scenes, that are openable windows. (not included scenes in fxmls)

The path to the fxmls starts from the ressource folder of the project

### CustomAlerts.java

Please get the code from the file uploaded.



## Used By

This small library is currently used by my class for various javafx related projects, like:

- QuizApp
- Minesweeper
- Checkers

Just some small projects (or games) made in school.


Please tell me if i should add more functionalities to this small helper Programm.
