module at.fmahring.scenemanager {
    requires javafx.controls;
    requires javafx.fxml;


    opens at.fmahring.scenemanager to javafx.fxml;
    exports at.fmahring.scenemanager;
}