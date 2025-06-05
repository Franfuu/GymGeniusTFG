package com.github.Franfuu.controllers;

import com.github.Franfuu.model.entities.Clase;
import com.github.Franfuu.model.entities.Cliente;
import com.github.Franfuu.model.entities.InscripcionesClase;
import com.github.Franfuu.services.ClaseService;
import com.github.Franfuu.services.InscripcionesClaseService;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class CrearInscripcionController implements Initializable {

    @FXML
    private Label clienteNombreLabel;

    @FXML
    private TextField filtroTextField;

    @FXML
    private TableView<Clase> clasesTableView;

    @FXML
    private TableColumn<Clase, Integer> idColumn;

    @FXML
    private TableColumn<Clase, String> nombreColumn;

    @FXML
    private TableColumn<Clase, String> instructorColumn;

    @FXML
    private TableColumn<Clase, String> salaColumn;

    @FXML
    private TableColumn<Clase, String> horarioColumn;

    @FXML
    private TableColumn<Clase, String> diasColumn;

    private Cliente clienteActual;
    private ClaseService claseService;
    private InscripcionesClaseService inscripcionesClaseService;
    private ObservableList<Clase> clasesObservableList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        claseService = new ClaseService();
        inscripcionesClaseService = new InscripcionesClaseService();

        // Configurar columnas
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        instructorColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getIdEmpleado() != null) {
                return new SimpleStringProperty(
                        cellData.getValue().getIdEmpleado().getNombre() + " " +
                                cellData.getValue().getIdEmpleado().getApellido()
                );
            }
            return new SimpleStringProperty("N/A");
        });

        salaColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getSala() != null) {
                return new SimpleStringProperty(cellData.getValue().getSala().getNombre());
            }
            return new SimpleStringProperty("N/A");
        });

        horarioColumn.setCellValueFactory(cellData -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            String inicio = cellData.getValue().getHoraInicio().format(formatter);
            String fin = cellData.getValue().getHoraFin().format(formatter);
            return new SimpleStringProperty(inicio + " - " + fin);
        });

        diasColumn.setCellValueFactory(new PropertyValueFactory<>("diasSemana"));

        // Permitir selección única
        clasesTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Cargar datos iniciales
        cargarClases();
    }

    public void setClienteActual(Cliente cliente) {
        this.clienteActual = cliente;
        clienteNombreLabel.setText(cliente.getNombre() + " " + cliente.getApellido());
    }

    private void cargarClases() {
        try {
            List<Clase> clases = claseService.obtenerTodasLasClases();
            clasesObservableList = FXCollections.observableArrayList(clases);
            clasesTableView.setItems(clasesObservableList);
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al cargar las clases", e.getMessage());
        }
    }

    @FXML
    void filtrarClases() {
        String filtro = filtroTextField.getText().trim().toLowerCase();
        if (filtro.isEmpty()) {
            cargarClases();
        } else {
            try {
                List<Clase> clasesFiltradas = claseService.buscarClasesPorNombre(filtro);
                clasesObservableList = FXCollections.observableArrayList(clasesFiltradas);
                clasesTableView.setItems(clasesObservableList);
            } catch (Exception e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al filtrar clases", e.getMessage());
            }
        }
    }

    @FXML
    void inscribirse() {
        Clase claseSeleccionada = clasesTableView.getSelectionModel().getSelectedItem();

        if (claseSeleccionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección requerida",
                    "No hay clase seleccionada", "Por favor, selecciona una clase para inscribirte.");
            return;
        }

        try {
            // Verificar si ya está inscrito
            if (inscripcionesClaseService.clienteYaInscrito(clienteActual, claseSeleccionada)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Información",
                        "Ya estás inscrito", "Ya estás inscrito en esta clase.");
                return;
            }

            // Crear nueva inscripción
            InscripcionesClase inscripcion = new InscripcionesClase();
            inscripcion.setIdCliente(clienteActual);
            inscripcion.setIdClase(claseSeleccionada);
            inscripcion.setFechaInscripcion(LocalDate.now());

            // Guardar
            inscripcionesClaseService.guardarInscripcion(inscripcion);

            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito",
                    "Inscripción realizada", "Te has inscrito correctamente a la clase: " +
                            claseSeleccionada.getNombre());

            // Cerrar ventana
            cerrarVentana();

        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error",
                    "Error al procesar la inscripción", e.getMessage());
        }
    }

    @FXML
    void cancelarInscripcion() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) clienteNombreLabel.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String cabecera, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}