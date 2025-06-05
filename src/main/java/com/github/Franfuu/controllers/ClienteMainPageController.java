package com.github.Franfuu.controllers;

import com.github.Franfuu.App;
import com.github.Franfuu.model.entities.Cliente;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class ClienteMainPageController extends Controller implements Initializable {

    @FXML
    private BorderPane anchorPane;

    @FXML
    private Text clienteNombreText;

    @FXML
    private Text clienteInfoText;

    @FXML
    private Text estadoMembresiaText;

    @FXML
    private ImageView clienteFotoView;

    @FXML
    private Button cerrarSesionButton;

    @FXML
    private Button crearInscripcionButton;

    private Cliente clienteActual;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            if (anchorPane.getScene() != null) {
                String cssUrl = getClass().getResource("/css/empleado-mainpage.css").toExternalForm();
                anchorPane.getScene().getStylesheets().add(cssUrl);

                Stage stage = (Stage) anchorPane.getScene().getWindow();
                stage.setMaximized(true);
            }
        });
    }

    @Override
    public void onOpen(Object input) {
        if (input instanceof Cliente) {
            clienteActual = (Cliente) input;
            clienteNombreText.setText(clienteActual.getNombre() + " " + clienteActual.getApellido());

            // Configurar información del cliente
            clienteInfoText.setText("Email: " + clienteActual.getEmail() + " | Teléfono: " + clienteActual.getTelefono());
            estadoMembresiaText.setText("Estado de Membresía: " + clienteActual.getEstadoMembresia());

            // Configurar foto del cliente
            if (clienteActual.getFoto() != null) {
                Image imagen = new Image(new ByteArrayInputStream(clienteActual.getFoto()));
                clienteFotoView.setImage(imagen);

                // Crear clip circular
                Circle clip = new Circle(
                    clienteFotoView.getFitWidth() / 2,
                    clienteFotoView.getFitHeight() / 2,
                    Math.min(clienteFotoView.getFitWidth(), clienteFotoView.getFitHeight()) / 2
                );
                clienteFotoView.setClip(clip);
            }
        }
    }

    @FXML
    void handleCerrarSesion() throws Exception {
        App.currentController.changeScene(Scenes.WELCOME, null);
    }

    @FXML
    void handleCrearInscripcion() {
        // Verificar el estado de la membresía antes de permitir la inscripción
        if ("suspendido".equalsIgnoreCase(clienteActual.getEstadoMembresia())) {
            showAlert(Alert.AlertType.WARNING, "Membresía Suspendida",
                    "No puedes inscribirte a clases",
                    "Tu membresía está suspendida. Por favor, pasa por la oficina para realizar tu pago.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/github/Franfuu/view/CrearInscripcionView.fxml"));
            Parent root = loader.load();

            // Obtener el controlador y pasarle el cliente actual
            CrearInscripcionController controller = loader.getController();
            controller.setClienteActual(clienteActual);

            // Configurar el escenario
            Stage stage = new Stage();
            stage.setTitle("Inscribirse a Clase");
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/empleado-mainpage.css").toExternalForm());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);

            // Al cerrar actualizamos la información si es necesario
            stage.showAndWait();

            // Actualizar estado de membresía por si ha cambiado
            estadoMembresiaText.setText("Estado de Membresía: " + clienteActual.getEstadoMembresia());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al abrir el formulario de inscripción", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleVerRutinas() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/github/Franfuu/view/ClienteVeRutinas.fxml"));
            Parent root = loader.load();

            // Obtener el controlador correcto
            ClienteVeRutinasController controller = loader.getController();
            controller.setClienteActual(clienteActual);

            // Configurar la escena
            Stage stage = new Stage();
            stage.setTitle("Mis Rutinas");
            Scene scene = new Scene(root);

            // Aplicar el CSS correcto para esta vista
            String cssPath = "/css/CrearView.css";
            URL cssResource = getClass().getResource(cssPath);
            if (cssResource != null) {
                scene.getStylesheets().add(cssResource.toExternalForm());
            }

            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(true);
            stage.showAndWait();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al abrir tus rutinas", e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public void onClose(Object output) {
        // Limpieza al cerrar
    }
}