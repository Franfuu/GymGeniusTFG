package com.github.Franfuu.controllers;

import com.github.Franfuu.App;
import com.github.Franfuu.model.entities.Empleado;
import com.github.Franfuu.model.entities.Sala;
import com.github.Franfuu.services.SalaService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class SalasController extends Controller implements Initializable {

    @FXML
    private BorderPane anchorPane;

    @FXML
    private Text empleadoNombreText;

    @FXML
    private ImageView empleadoFotoView;

    @FXML
    private TableView<Sala> salasTable;

    @FXML
    private TableColumn<Sala, Integer> idColumn;

    @FXML
    private TableColumn<Sala, String> nombreColumn;

    @FXML
    private TableColumn<Sala, Integer> capacidadMaximaColumn;

    @FXML
    private TableColumn<Sala, String> ubicacionColumn;

    @FXML
    private TableColumn<Sala, Void> accionesColumn;

    private Empleado empleadoActual;

    private SalaService salaService;
    private ObservableList<Sala> salasList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        salaService = new SalaService();

        configureTable();
        loadSalasData();
        configureActionsColumn();

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
        if (input instanceof Empleado) {
            empleadoActual = (Empleado) input;
            empleadoNombreText.setText(empleadoActual.getNombre() + " " + empleadoActual.getApellido());

            if (empleadoActual.getFoto() != null) {
                Image imagen = new Image(new ByteArrayInputStream(empleadoActual.getFoto()));
                empleadoFotoView.setImage(imagen);

                // Crear clip circular
                Circle clip = new Circle(45, 45, 45);
                empleadoFotoView.setClip(clip);
            }
        }
    }

    @Override
    public void onClose(Object output) {
        // Limpieza al cerrar
    }

    private void configureTable() {
        salasTable.setEditable(true);

        // Columna no editable
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Columnas editables
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        nombreColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nombreColumn.setOnEditCommit(event -> {
            Sala sala = event.getRowValue();
            sala.setNombre(event.getNewValue());
            updateSala(sala);
        });

        capacidadMaximaColumn.setCellValueFactory(new PropertyValueFactory<>("capacidadMaxima"));
        capacidadMaximaColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        capacidadMaximaColumn.setOnEditCommit(event -> {
            Sala sala = event.getRowValue();
            sala.setCapacidadMaxima(event.getNewValue());
            updateSala(sala);
        });

        ubicacionColumn.setCellValueFactory(new PropertyValueFactory<>("ubicacion"));
        ubicacionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        ubicacionColumn.setOnEditCommit(event -> {
            Sala sala = event.getRowValue();
            sala.setUbicacion(event.getNewValue());
            updateSala(sala);
        });
    }

    private void configureActionsColumn() {
        accionesColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("X");

            {
                deleteButton.getStyleClass().add("delete-button");
                deleteButton.setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-background-color: transparent;");
                deleteButton.setMaxWidth(Double.MAX_VALUE);
                deleteButton.setAlignment(Pos.CENTER);

                deleteButton.setOnAction(event -> {
                    Sala sala = getTableView().getItems().get(getIndex());
                    deleteSala(sala);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                    setAlignment(Pos.CENTER);
                }
            }
        });
    }

    private void loadSalasData() {
        try {
            List<Sala> salas = salaService.obtenerTodasLasSalas();
            salasList = FXCollections.observableArrayList(salas);
            salasTable.setItems(salasList);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al cargar las salas", e.getMessage());
        }
    }

    private void updateSala(Sala sala) {
        try {
            salaService.actualizarSala(sala);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al actualizar la sala", e.getMessage());
        }
    }

    private void deleteSala(Sala sala) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Estás seguro de que deseas eliminar esta sala?");
        alert.setContentText("Esta acción no se puede deshacer.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                salaService.eliminarSala(sala);
                salasList.remove(sala);
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error al eliminar la sala", e.getMessage());
            }
        }
    }

    @FXML
    void crearSala() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/github/Franfuu/view/CrearSalaView.fxml"));
            Parent root = loader.load();

            // Obtener el controlador
            CrearSalaController controller = loader.getController();
            controller.setEmpleadoActual(empleadoActual);

            // Configurar el escenario
            Stage stage = new Stage();
            stage.setTitle("Crear Sala");
            Scene scene = new Scene(root);

            // Usar el CSS
            scene.getStylesheets().add(getClass().getResource("/css/CrearView.css").toExternalForm());

            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);

            // Configurar acción al cerrar para actualizar la tabla
            stage.setOnHidden(e -> loadSalasData());

            stage.showAndWait();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al crear la sala", e.getMessage());
            e.printStackTrace();
        }
    }
    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void handleCerrarSesion() throws Exception {
        App.currentController.changeScene(Scenes.WELCOME, null);
    }

    @FXML
    void showMaquinas() {
        try {
            App.currentController.changeScene(Scenes.MAQUINAS, empleadoActual);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al cargar la vista de máquinas", e.getMessage());
        }
    }

    @FXML
    void showClientes() {
        try {
            App.currentController.changeScene(Scenes.MAINPAGE_EMPLEADO, empleadoActual);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al cargar la vista de clientes", e.getMessage());
        }
    }

    @FXML
    void showSalas() {
        // Ya estamos en esta vista, podemos recargar los datos
        loadSalasData();
    }

    @FXML
    void showClases() {
        try {
            App.currentController.changeScene(Scenes.CLASES, empleadoActual);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al cargar la vista de clases", e.getMessage());
        }
    }

}