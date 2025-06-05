package com.github.Franfuu.controllers;

import com.github.Franfuu.App;
import com.github.Franfuu.model.entities.Cliente;
import com.github.Franfuu.model.entities.Empleado;
import com.github.Franfuu.model.utils.ToggleSwitch;
import com.github.Franfuu.services.ClienteService;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.io.ByteArrayInputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class EmpleadoMainPageController extends Controller implements Initializable {

    @FXML
    private BorderPane anchorPane;

    @FXML
    private Text empleadoNombreText;

    @FXML
    private ImageView empleadoFotoView;

    @FXML
    private TableView<Cliente> clientesTable;

    @FXML
    private TableColumn<Cliente, Integer> idColumn;

    @FXML
    private TableColumn<Cliente, String> nombreColumn;

    @FXML
    private TableColumn<Cliente, String> apellidoColumn;

    @FXML
    private TableColumn<Cliente, String> emailColumn;

    @FXML
    private TableColumn<Cliente, String> telefonoColumn;

    @FXML
    private TableColumn<Cliente, LocalDate> fechaNacimientoColumn;

    @FXML
    private TableColumn<Cliente, LocalDate> fechaRegistroColumn;

    @FXML
    private TableColumn<Cliente, String> estadoMembresiaColumn;

    @FXML
    private TableColumn<Cliente, Void> accionesColumn;

    private ClienteService clienteService;
    private ObservableList<Cliente> clientesList;

    private Empleado empleadoActual;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clienteService = new ClienteService();

        // Configurar columnas de la tabla
        configureTable();

        // Cargar datos de clientes
        loadClientesData();

        configureActionsColumn();

        Platform.runLater(() -> {
            if (anchorPane.getScene() != null) {
                String cssUrl = getClass().getResource("/css/empleado-mainpage.css").toExternalForm();
                anchorPane.getScene().getStylesheets().add(cssUrl);

                // Configurar pantalla completa
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
        // Configurar la tabla de clientes
        clientesTable.setEditable(true);

        // Crear y agregar columna de foto al principio
        TableColumn<Cliente, byte[]> fotoColumn = new TableColumn<>("Foto");
        fotoColumn.setPrefWidth(100);
        clientesTable.getColumns().add(0, fotoColumn);

        fotoColumn.setCellValueFactory(new PropertyValueFactory<>("foto"));
        fotoColumn.setCellFactory(column -> new TableCell<>() {
            private final ImageView imageView = new ImageView();

            {
                imageView.setFitHeight(100);
                imageView.setFitWidth(100);
                imageView.setPreserveRatio(true);
            }

            @Override
            protected void updateItem(byte[] item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    try {
                        imageView.setImage(new Image(new ByteArrayInputStream(item)));
                        setGraphic(imageView);
                    } catch (Exception e) {
                        setGraphic(null);
                    }
                }
                setAlignment(Pos.CENTER);
            }
        });

        // Configurar columnas NO editables
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(70);
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailColumn.setPrefWidth(200);
        fechaNacimientoColumn.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));
        fechaNacimientoColumn.setPrefWidth(120);
        fechaRegistroColumn.setCellValueFactory(new PropertyValueFactory<>("fechaRegistro"));
        fechaNacimientoColumn.setPrefWidth(120);
        // Configurar columnas editables
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        nombreColumn.setPrefWidth(100);
        nombreColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nombreColumn.setOnEditCommit(event -> {
            Cliente cliente = event.getRowValue();
            cliente.setNombre(event.getNewValue());
            updateCliente(cliente);
        });

        apellidoColumn.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        apellidoColumn.setPrefWidth(120);
        apellidoColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        apellidoColumn.setOnEditCommit(event -> {
            Cliente cliente = event.getRowValue();
            cliente.setApellido(event.getNewValue());
            updateCliente(cliente);
        });

        telefonoColumn.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        telefonoColumn.setPrefWidth(100);
        telefonoColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        telefonoColumn.setOnEditCommit(event -> {
            Cliente cliente = event.getRowValue();
            cliente.setTelefono(event.getNewValue());
            updateCliente(cliente);
        });

        // Reemplazar la columna de estado con un Toggle Switch
        estadoMembresiaColumn.setCellValueFactory(new PropertyValueFactory<>("estadoMembresia"));
        estadoMembresiaColumn.setPrefWidth(70);
        estadoMembresiaColumn.setCellFactory(column -> new TableCell<>() {
            private final ToggleSwitch toggleSwitch = new ToggleSwitch();

            {
                toggleSwitch.selectedProperty().addListener((obs, oldVal, newVal) -> {
                    if (getTableRow() != null && getTableRow().getItem() != null) {
                        Cliente cliente = getTableRow().getItem();
                        cliente.setEstadoMembresia(newVal ? "activo" : "suspendido");
                        updateCliente(cliente);
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    toggleSwitch.setSelected("activo".equals(item));
                    setGraphic(toggleSwitch);
                }
                setAlignment(Pos.CENTER);
            }
        });
    }

    private void configureActionsColumn() {
        accionesColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("X");

            {
                // Configurar botón de eliminar con texto y centrado
                deleteButton.getStyleClass().add("delete-button");
                deleteButton.setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-background-color: transparent;");
                deleteButton.setMaxWidth(Double.MAX_VALUE);
                deleteButton.setAlignment(Pos.CENTER);

                // Acción al hacer clic
                deleteButton.setOnAction(event -> {
                    Cliente cliente = getTableView().getItems().get(getIndex());
                    deleteCliente(cliente);
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

    private void loadClientesData() {
        try {
            List<Cliente> clientes = clienteService.obtenerTodosLosClientes();
            clientesList = FXCollections.observableArrayList(clientes);
            clientesTable.setItems(clientesList);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al cargar los clientes", e.getMessage());
        }
    }

    private void updateCliente(Cliente cliente) {
        try {
            clienteService.actualizarCliente(cliente);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al actualizar el cliente", e.getMessage());
        }
    }

    private void deleteCliente(Cliente cliente) {
        // Confirmar eliminación
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Estás seguro de que deseas eliminar este cliente?");
        alert.setContentText("Esta acción no se puede deshacer.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                clienteService.eliminarCliente(cliente.getId());
                clientesList.remove(cliente);
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error al eliminar el cliente", e.getMessage());
            }
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
            showAlert(Alert.AlertType.ERROR, "Error", "Error al cargar la vista de maquinas", e.getMessage());
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
        try {
            App.currentController.changeScene(Scenes.SALAS, empleadoActual);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al cargar la vista de salas", e.getMessage());
        }
    }

    @FXML
    void showClases() {
        try {
            App.currentController.changeScene(Scenes.CLASES, empleadoActual);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al cargar la vista de clases", e.getMessage());
        }
    }

    @FXML
    void showRutinas() {
        try {
            App.currentController.changeScene(Scenes.RUTINAS, empleadoActual);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al cargar la vista de rutinas", e.getMessage());
        }
    }
}