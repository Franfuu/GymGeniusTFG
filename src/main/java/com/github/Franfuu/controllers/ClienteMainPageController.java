package com.github.Franfuu.controllers;

import com.github.Franfuu.model.entities.Cliente;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class ClienteMainPageController extends Controller implements Initializable {

    @FXML
    private Label welcomeLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Inicialización del controlador
    }

    @Override
    public void onOpen(Object input) {
        if (input instanceof Cliente) {
            Cliente cliente = (Cliente) input;
            welcomeLabel.setText("Bienvenido/a, " + cliente.getNombre() + " " + cliente.getApellido());
        }
    }

    @Override
    public void onClose(Object output) {
        // Limpieza al cerrar
    }
}