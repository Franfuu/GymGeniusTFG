package com.github.Franfuu.controllers;

import com.github.Franfuu.model.entities.Empleado;
import com.github.Franfuu.model.entities.Maquina;
import com.github.Franfuu.model.entities.Sala;
import com.github.Franfuu.services.MaquinaService;
import com.github.Franfuu.services.SalaService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.ResourceBundle;

public class CrearMaquinaController implements Initializable {

    @FXML
    private TextField nombreField;

    @FXML
    private TextField marcaField;

    @FXML
    private TextField modeloField;

    @FXML
    private ComboBox<Sala> salaCombo;

    @FXML
    private TextField fotoPathField;

    @FXML
    private Button seleccionarFotoButton;

    @FXML
    private Button guardarButton;

    @FXML
    private Button cancelarButton;

    private MaquinaService maquinaService;
    private SalaService salaService;
    private Empleado empleadoActual;
    private File selectedImageFile;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        maquinaService = new MaquinaService();
        salaService = new SalaService();

        cargarSalas();
    }

    private void cargarSalas() {
        try {
            List<Sala> salas = salaService.obtenerTodasLasSalas();
            salaCombo.getItems().clear();
            salaCombo.getItems().addAll(salas);

            // Configurar cómo se muestran las salas en el ComboBox
            salaCombo.setCellFactory(cell -> new ListCell<Sala>() {
                @Override
                protected void updateItem(Sala sala, boolean empty) {
                    super.updateItem(sala, empty);
                    if (empty || sala == null) {
                        setText(null);
                    } else {
                        setText(sala.getNombre());
                    }
                }
            });

            salaCombo.setButtonCell(new ListCell<Sala>() {
                @Override
                protected void updateItem(Sala sala, boolean empty) {
                    super.updateItem(sala, empty);
                    if (empty || sala == null) {
                        setText(null);
                    } else {
                        setText(sala.getNombre());
                    }
                }
            });
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al cargar las salas", e.getMessage());
        }
    }

    @FXML
    void seleccionarFoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        Stage stage = (Stage) nombreField.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            selectedImageFile = selectedFile;
            fotoPathField.setText(selectedFile.getName());
        }
    }

    @FXML
    void handleGuardar() {
        if (!validarCampos()) {
            return;
        }

        try {
            Maquina maquina = new Maquina();
            maquina.setNombre(nombreField.getText().trim());
            maquina.setMarca(marcaField.getText().trim());
            maquina.setModelo(modeloField.getText().trim());
            maquina.setSala(salaCombo.getValue());

            // Cargar la imagen si fue seleccionada
            if (selectedImageFile != null) {
                try {
                    byte[] imageData = Files.readAllBytes(selectedImageFile.toPath());
                    maquina.setFoto(imageData);
                } catch (IOException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Error al cargar la imagen", e.getMessage());
                    return;
                }
            }

            maquinaService.guardarMaquina(maquina);

            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Máquina creada",
                    "La máquina se ha creado correctamente.");

            // Cerrar la ventana
            Stage stage = (Stage) guardarButton.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al guardar la máquina", e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validarCampos() {
        if (nombreField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Campo requerido", "El nombre de la máquina es obligatorio.");
            return false;
        }

        if (marcaField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Campo requerido", "La marca de la máquina es obligatoria.");
            return false;
        }

        if (salaCombo.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Campo requerido", "Debe seleccionar una sala para la máquina.");
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