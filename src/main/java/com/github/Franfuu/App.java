package com.github.Franfuu;

import com.github.Franfuu.controllers.AppController;
import com.github.Franfuu.controllers.Scenes;
import com.github.Franfuu.controllers.View;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class App extends Application {

    public static Scene scene;
    public static Stage stage;
    public static AppController currentController;


    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        View view = AppController.loadFXML(Scenes.ROOT);
        scene = new Scene(view.scene,  900, 600);
        currentController = (AppController) view.controller;
        currentController.onOpen( null);
        stage.setScene(scene);
        stage.show();

    }
}