package com.github.Franfuu.controllers;

import com.github.Franfuu.App;
import com.github.Franfuu.model.entities.Cliente;
import com.github.Franfuu.model.utils.PasswordUtils;
import com.github.Franfuu.services.ClienteService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ClienteLoginController extends Controller implements Initializable {

    @FXML private TextField loginEmail;
    @FXML private PasswordField loginPassword;

    @FXML private TextField registerName;
    @FXML private TextField registerLastName;
    @FXML private TextField registerEmail;
    @FXML private TextField registerPhone;
    @FXML private DatePicker registerBirthDate;
    @FXML private PasswordField registerPassword;
    @FXML private PasswordField registerConfirmPassword;
    @FXML private Label selectedImageLabel;

    private ClienteService clienteService;
    private File selectedImageFile;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clienteService = new ClienteService();

        Platform.runLater(() -> {
            if (loginEmail.getScene() != null) {
                // Cargar CSS
                String cssUrl = getClass().getResource("/css/cliente-login.css").toExternalForm();
                loginEmail.getScene().getStylesheets().add(cssUrl);

                // Configurar pantalla completa
                Stage stage = (Stage) loginEmail.getScene().getWindow();
                stage.setMaximized(true);
            }
        });

    }

    @Override
    public void onOpen(Object input) {
        // Inicialización cuando se abre la vista
    }

    @Override
    public void onClose(Object output) {
        // Limpieza al cerrar la vista
    }

    @FXML
    private void volverAWelcome(ActionEvent event) throws Exception {
        App.currentController.changeScene(Scenes.WELCOME, null);
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String email = loginEmail.getText().trim();
        String password = loginPassword.getText();

        if (email.isEmpty() || password.isEmpty()) {
            mostrarAlerta("Error de inicio de sesión", "Por favor, completa todos los campos.");
            return;
        }

        try {
            // Validar credenciales
            Cliente cliente = clienteService.autenticarCliente(email, password);

            if (cliente != null) {
                // Autenticación exitosa - cambiar a la pantalla principal
                mostrarAlerta("Inicio de sesión exitoso", "Bienvenido, " + cliente.getNombre() + "!");
                App.currentController.changeScene(Scenes.MAINPAGE_CLIENTE, cliente);
            } else {
                // Credenciales incorrectas
                mostrarAlerta("Error de inicio de sesión", "Email o contraseña incorrectos.");
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un problema al iniciar sesión: " + e.getMessage());
        }
    }


    @FXML
    private void handleSelectImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen de perfil");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        selectedImageFile = fileChooser.showOpenDialog(selectedImageLabel.getScene().getWindow());

        if (selectedImageFile != null) {
            selectedImageLabel.setText(selectedImageFile.getName());
        }
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        String nombre = registerName.getText().trim();
        String apellido = registerLastName.getText().trim();
        String email = registerEmail.getText().trim();
        String telefono = registerPhone.getText().trim();
        LocalDate fechaNacimiento = registerBirthDate.getValue();
        String password = registerPassword.getText();
        String confirmPassword = registerConfirmPassword.getText();

        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() ||
                telefono.isEmpty() || fechaNacimiento == null || password.isEmpty()) {
            mostrarAlerta("Error de registro", "Por favor, completa todos los campos obligatorios.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            mostrarAlerta("Error de registro", "Las contraseñas no coinciden.");
            return;
        }

        try {
            // Crear nuevo cliente
            Cliente cliente = new Cliente();
            cliente.setNombre(nombre);
            cliente.setApellido(apellido);
            cliente.setEmail(email);
            cliente.setTelefono(telefono);
            cliente.setFechaNacimiento(java.sql.Date.valueOf(fechaNacimiento));
            cliente.setContraseña(PasswordUtils.hashPassword(password));            cliente.setEstadoMembresia("activo");

            // Procesar la foto si se seleccionó
            if (selectedImageFile != null) {
                try (FileInputStream fis = new FileInputStream(selectedImageFile)) {
                    byte[] fotoBytes = new byte[(int) selectedImageFile.length()];
                    fis.read(fotoBytes);
                    cliente.setFoto(fotoBytes);
                }
            }

            clienteService.guardarCliente(cliente);
            mostrarAlerta("Registro exitoso", "¡Bienvenido a GymGenius, " + nombre + "!");
            limpiarCamposRegistro();

        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo completar el registro: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        // Guardar estado de pantalla completa antes de mostrar el aviso
        Stage stage = (Stage) loginEmail.getScene().getWindow();
        boolean estabaEnPantallaCompleta = stage.isFullScreen();

        // Mostrar el aviso
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();

        // Restaurar pantalla completa si estaba en ese modo
        if (estabaEnPantallaCompleta) {
            Platform.runLater(() -> stage.setFullScreen(true));
        }
    }

    private void limpiarCamposRegistro() {
        registerName.clear();
        registerLastName.clear();
        registerEmail.clear();
        registerPhone.clear();
        registerBirthDate.setValue(null);
        registerPassword.clear();
        registerConfirmPassword.clear();
        selectedImageFile = null;
        selectedImageLabel.setText("Ningún archivo seleccionado");
    }
}