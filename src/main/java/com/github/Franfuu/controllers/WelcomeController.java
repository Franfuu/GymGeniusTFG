package com.github.Franfuu.controllers;

import com.github.Franfuu.App;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeController extends Controller implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button registerButton;
    @FXML
    private Button loginButton;

    @Override
    public void onOpen(Object input) throws Exception {

    }

    @Override
    public void onClose(Object output) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configurar pantalla completa
        Platform.runLater(() -> {
            if (anchorPane.getScene() != null) {
                // Aplicar CSS si es necesario
                String cssUrl = getClass().getResource("/css/welcome.css").toExternalForm();
                anchorPane.getScene().getStylesheets().add(cssUrl);

                // Configurar pantalla completa
                Stage stage = (Stage) anchorPane.getScene().getWindow();
                stage.setMaximized(true);
            }
        });
    }

    public void setClientButton() throws Exception {
        App.currentController.changeScene(Scenes.LOGINCLIENTE, null);
    }
    public void setEmployButton() throws Exception {
        App.currentController.changeScene(Scenes.LOGINEMPLEADO, null);
    }

}
