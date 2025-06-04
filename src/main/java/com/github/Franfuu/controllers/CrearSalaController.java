package com.github.Franfuu.controllers;

import com.github.Franfuu.model.entities.Empleado;
import com.github.Franfuu.model.entities.Sala;
import com.github.Franfuu.services.SalaService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class CrearSalaController implements Initializable {

    @FXML
    private TextField nombreField;

    @FXML
    private TextField capacidadMaximaField;

    @FXML
    private TextField ubicacionField;

    @FXML
    private Button guardarButton;

    @FXML
    private Button cancelarButton;

    private Empleado empleadoActual;
    private SalaService salaService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        salaService = new SalaService();
    }

    @FXML
    void handleGuardar() {
        if (!validarCampos()) {
            return;
        }

        try {
            Sala sala = new Sala();
            sala.setNombre(nombreField.getText().trim());

            // Convertir capacidad máxima a entero
            try {
                int capacidadMaxima = Integer.parseInt(capacidadMaximaField.getText().trim());
                sala.setCapacidadMaxima(capacidadMaxima);
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Capacidad máxima inválida",
                        "La capacidad máxima debe ser un número entero.");
                return;
            }

            sala.setUbicacion(ubicacionField.getText().trim());

            salaService.guardarSala(sala);

            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Sala creada",
                    "La sala se ha creado correctamente.");

            // Cerrar la ventana
            Stage stage = (Stage) guardarButton.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al guardar la sala", e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validarCampos() {
        if (nombreField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Campo requerido", "El nombre de la sala es obligatorio.");
            return false;
        }

        if (capacidadMaximaField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Campo requerido", "La capacidad máxima es obligatoria.");
            return false;
        }

        if (ubicacionField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Campo requerido", "La ubicación de la sala es obligatoria.");
            return false;
        }

        return true;
    }

    @FXML
    void handleCancelar() {
        Stage stage = (Stage) cancelarButton.getScene().getWindow();
        stage.close();
    }

    public void setEmpleadoActual(Empleado empleado) {
        this.empleadoActual = empleado;
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}