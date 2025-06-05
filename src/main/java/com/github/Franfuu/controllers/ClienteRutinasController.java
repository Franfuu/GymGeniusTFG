package com.github.Franfuu.controllers;

import com.github.Franfuu.model.entities.Cliente;
import com.github.Franfuu.model.entities.Rutina;
import com.github.Franfuu.services.RutinaService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.util.List;

public class ClienteRutinasController {

    @FXML
    private Label tituloLabel;

    @FXML
    private TableView<Rutina> rutinasTableView;

    @FXML
    private TableColumn<Rutina, String> nombreRutinaColumn;

    @FXML
    private TableColumn<Rutina, String> descripcionColumn;

    @FXML
    private TableColumn<Rutina, String> fechaCreacionColumn;

    @FXML
    private Button cerrarButton;

    private RutinaService rutinaService;
    private Cliente clienteActual;

    public void inicializarDatos(Cliente cliente) {
        this.clienteActual = cliente;
        tituloLabel.setText("Rutinas de: " + cliente.getNombre() + " " + cliente.getApellido());

        rutinaService = new RutinaService();

        // Configurar columnas
        nombreRutinaColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getNombre()));

        descripcionColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getDescripcion()));

        fechaCreacionColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getFechaCreacion().toString()));

        // Cargar datos
        cargarRutinas();
    }

    private void cargarRutinas() {
        List<Rutina> rutinas = rutinaService.obtenerRutinasPorCliente(clienteActual.getId());
        rutinasTableView.setItems(FXCollections.observableArrayList(rutinas));
    }

    @FXML
    private void handleCerrar() {
        Stage stage = (Stage) cerrarButton.getScene().getWindow();
        stage.close();
    }
}