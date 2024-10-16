# SceneManager

This is the release of my Library SceneManager and CustomAlerts.

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

Tu use the SceneManager, you need to create two classes:

### SceneManager.java and Scenes.java
```
Please see the SceneManager and Scenes.java files, and copy everything inside into the created file.
```

### To start using it, please use like this:
```java
public class Starter extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneManager.getInstance().showScene(Scenes.MAIN_MENU);
    }
}
```

# Test if scene/popup is open

To test, which scene or popup, is open
Or to test if the desired scene/popup is open, use:

```java
if (getSceneStack().peek().equals(Scenes.GAME) && popUpSceneStack.peek().equals(Scenes.ESCMENU)) {
            GameController.resume();
}
```
This is just for example, to show that soemthing like this would be possible.

<br>
<br>

The CustomAlerts, are rather intutive and don't really need an explanation.

In the Scenes class, each scene is managed like this:
```java
    MAIN_MENU("hello-view.fxml", "Menu")
```
The Scenes class is an enum class, that just holds all the used scenes, that are openable windows.

The path to the fxmls starts from the ressource folder of the project


Please tell me if i should add more functionalities to this small helper Programm.
