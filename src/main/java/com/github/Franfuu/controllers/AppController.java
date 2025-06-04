package com.github.Franfuu.controllers;

import com.github.Franfuu.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.net.URL;

import java.util.ResourceBundle;

public class AppController extends Controller implements Initializable {


    @FXML
    private BorderPane borderPane;
    private Controller centerController;
    /**
     * Controls the application's main functionality and scene management.
     * Handles loading FXML views, changing scenes within the application,
     * and opening modal dialogs. Initializes the application with a welcome scene.
     *
     * @param input the input data (not used in this implementation)
     * @throws Exception if there's an issue during scene loading or initialization
     */
    @Override
    public void onOpen(Object input) throws Exception {
        changeScene(Scenes.WELCOME, null);
    }

    /**
     * Initializes the controller when the FXML file is loaded.
     *
     * @param url the location used to resolve relative paths for the root object, or null if the location is not known
     * @param rb the resources used to localize the root object, or null if the root object was not localized
     */
    @FXML
    public void initialize(URL url, ResourceBundle rb)  {

    }

    /**
     * Loads an FXML view based on the provided scene enumeration.
     *
     * @param scenes the scene enumeration indicating which FXML view to load
     * @return the loaded View object containing the loaded scene and its corresponding controller
     * @throws Exception if there's an issue during FXML loading
     */
    public static View loadFXML(Scenes scenes) throws Exception {
        String url = scenes.getURL();
        System.out.println(url);
        FXMLLoader loader = new FXMLLoader(App.class.getResource(url));
        Parent p = loader.load();
        Controller c = loader.getController();
        View view = new View();
        view.scene = p;
        view.controller = c;
        return view;
    }

    /**
     * Changes the main center scene of the application to the specified scene.
     * Loads the FXML view, sets it as the center of the BorderPane, and initializes its controller.
     *
     * @param scene the scene enumeration indicating which FXML view to load and display
     * @param data the data object passed to the newly loaded scene's controller
     * @throws Exception if there's an issue during scene loading or controller initialization
     */
    public void changeScene(Scenes scene, Object data) throws Exception {
        View view = loadFXML(scene);
        borderPane.setCenter(view.scene);
        this.centerController = view.controller;
        this.centerController.onOpen(data);
    }

    /**
     * Opens a modal dialog with the specified scene, title, parent controller, and data object.
     * Loads the FXML view, initializes the stage as a modal dialog, and displays it.
     *
     * @param scene the scene enumeration indicating which FXML view to load and display as a modal dialog
     * @param title the title of the modal dialog window
     * @param parent the parent controller that opened the modal dialog
     * @param data the data object passed to the modal dialog's controller
     * @throws Exception if there's an issue during modal dialog loading or controller initialization
     */
    public void openModal(Scenes scene, String title, Controller parent, Object data) throws Exception {
        View view = loadFXML(scene);
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(App.stage);
        Scene _scene = new Scene(view.scene);
        stage.setScene(_scene);
        view.controller.onOpen(parent);
        stage.showAndWait();
    }

    /**
     * Handles the cleanup when the application is closed or the controller is no longer needed.
     *
     * @param output the output data (not used in this implementation)
     */
    @Override
    public void onClose(Object output) {
    }



}



