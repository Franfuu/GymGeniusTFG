package com.github.Franfuu.controllers;

import com.github.Franfuu.model.entities.Ejercicio;
import com.github.Franfuu.services.EjercicioService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class CrearEjercicioController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextField txtNombre;

    @FXML
    private ComboBox<String> comboGrupoMuscular;

    @FXML
    private TextArea txtDescripcion;

    private EjercicioService ejercicioService;

    // Lista de grupos musculares comunes
    private final List<String> gruposMusculares = Arrays.asList(
            "Pecho", "Espalda", "Hombros", "Bíceps", "Tríceps",
            "Cuádriceps", "Isquiotibiales", "Glúteos", "Pantorrillas",
            "Abdominales", "Core", "Cuerpo completo"
    );

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ejercicioService = new EjercicioService();

        // Configurar el ComboBox con los grupos musculares
        comboGrupoMuscular.setItems(FXCollections.observableArrayList(gruposMusculares));
    }

    @FXML
    void guardarEjercicio() {
        if (validarFormulario()) {
            try {
                Ejercicio nuevoEjercicio = new Ejercicio();
                nuevoEjercicio.setNombre(txtNombre.getText().trim());
                nuevoEjercicio.setGrupoMuscular(comboGrupoMuscular.getValue());
                nuevoEjercicio.setDescripcion(txtDescripcion.getText().trim());

                ejercicioService.guardarEjercicio(nuevoEjercicio);

                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito",
                        "Ejercicio creado", "El ejercicio ha sido creado correctamente.");

                cerrarVentana();
            } catch (Exception e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error",
                        "Error al guardar el ejercicio", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private boolean validarFormulario() {
        StringBuilder errores = new StringBuilder();

        if (txtNombre.getText() == null || txtNombre.getText().trim().isEmpty()) {
            errores.append("- El nombre del ejercicio es obligatorio\n");
        }

        if (comboGrupoMuscular.getValue() == null) {
            errores.append("- Debe seleccionar un grupo muscular\n");
        }

        if (txtDescripcion.getText() == null || txtDescripcion.getText().trim().isEmpty()) {
            errores.append("- La descripción del ejercicio es obligatoria\n");
        }

        if (errores.length() > 0) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de validación",
                    "Por favor corrija los siguientes errores:", errores.toString());
            return false;
        }
        return true;
    }

    @FXML
    void cancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}