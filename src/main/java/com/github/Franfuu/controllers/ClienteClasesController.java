package com.github.Franfuu.controllers;

import com.github.Franfuu.model.entities.InscripcionClaseDTO;
import com.github.Franfuu.model.entities.Cliente;
import com.github.Franfuu.model.entities.InscripcionClaseDTO;
import com.github.Franfuu.model.entities.InscripcionesClase;
import com.github.Franfuu.services.InscripcionesClaseService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class ClienteClasesController {

    @FXML
    private Label tituloLabel;

    @FXML
    private TableView<com.github.Franfuu.model.entities.InscripcionClaseDTO> clasesTableView;

    @FXML
    private TableColumn<InscripcionClaseDTO, String> nombreClaseColumn;

    @FXML
    private TableColumn<InscripcionClaseDTO, String> descripcionColumn;

    @FXML
    private TableColumn<InscripcionClaseDTO, String> fechaInscripcionColumn;

    @FXML
    private Button cerrarButton;

    private InscripcionesClaseService inscripcionesClaseService;
    private Cliente clienteActual;

    public void inicializarDatos(Cliente cliente) {
        this.clienteActual = cliente;
        tituloLabel.setText("Clases de: " + cliente.getNombre() + " " + cliente.getApellido());

        inscripcionesClaseService = new InscripcionesClaseService();

        // Configurar columnas
        nombreClaseColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNombreClase()));

        descripcionColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDescripcionClase()));

        fechaInscripcionColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFechaInscripcion()));

        // Cargar datos
        cargarInscripciones();
    }

    private void cargarInscripciones() {
        List<InscripcionesClase> inscripciones = inscripcionesClaseService.obtenerInscripcionesCompletasPorCliente(clienteActual.getId());

        // Convertir a DTOs para evitar problemas con lazy loading
        List<InscripcionClaseDTO> dtos = inscripciones.stream()
                .map(i -> new InscripcionClaseDTO(
                        i.getIdClase().getNombre(),
                        i.getIdClase().getDescripcion(),
                        i.getFechaInscripcion().toString()))
                .collect(Collectors.toList());

        clasesTableView.setItems(FXCollections.observableArrayList(dtos));
    }

    @FXML
    private void handleCerrar() {
        Stage stage = (Stage) cerrarButton.getScene().getWindow();
        stage.close();
    }
}