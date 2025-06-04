package com.github.Franfuu.controllers;

import com.github.Franfuu.App;
import com.github.Franfuu.model.entities.Empleado;
import com.github.Franfuu.model.entities.Maquina;
import com.github.Franfuu.model.entities.Sala;
import com.github.Franfuu.services.MaquinaService;
import com.github.Franfuu.services.SalaService;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
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

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MaquinasController extends Controller implements Initializable {

    @FXML
    private BorderPane anchorPane;

    @FXML
    private Text empleadoNombreText;

    @FXML
    private ImageView empleadoFotoView;

    @FXML
    private TableView<Maquina> maquinasTable;

    @FXML
    private TableColumn<Maquina, byte[]> fotoColumn;

    @FXML
    private TableColumn<Maquina, Integer> idColumn;

    @FXML
    private TableColumn<Maquina, String> nombreColumn;

    @FXML
    private TableColumn<Maquina, String> marcaColumn;

    @FXML
    private TableColumn<Maquina, String> modeloColumn;

    @FXML
    private TableColumn<Maquina, String> salaColumn;

    @FXML
    private TableColumn<Maquina, Void> accionesColumn;

    private Empleado empleadoActual;

    private MaquinaService maquinaService;
    private SalaService salaService;
    private ObservableList<Maquina> maquinasList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        maquinaService = new MaquinaService();
        salaService = new SalaService();

        configureTable();
        loadMaquinasData();
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
        maquinasTable.setEditable(true);

        fotoColumn.setCellValueFactory(new PropertyValueFactory<>("foto"));
        fotoColumn.setCellFactory(column -> new TableCell<>() {
            private final ImageView imageView = new ImageView();

            {
                imageView.setFitHeight(90);
                imageView.setFitWidth(90);
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

        // Columna no editable
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Columnas editables
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        nombreColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nombreColumn.setOnEditCommit(event -> {
            Maquina maquina = event.getRowValue();
            maquina.setNombre(event.getNewValue());
            updateMaquina(maquina);
        });

        marcaColumn.setCellValueFactory(new PropertyValueFactory<>("marca"));
        marcaColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        marcaColumn.setOnEditCommit(event -> {
            Maquina maquina = event.getRowValue();
            maquina.setMarca(event.getNewValue());
            updateMaquina(maquina);
        });

        modeloColumn.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        modeloColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        modeloColumn.setOnEditCommit(event -> {
            Maquina maquina = event.getRowValue();
            maquina.setModelo(event.getNewValue());
            updateMaquina(maquina);
        });

        // Columna de sala
        salaColumn.setCellValueFactory(new PropertyValueFactory<>("sala"));
        salaColumn.setCellValueFactory(cellData -> {
            Sala sala = cellData.getValue().getSala();
            if (sala != null) {
                return new SimpleStringProperty(sala.getNombre());
            }
            return new SimpleStringProperty("");
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
                    Maquina maquina = getTableView().getItems().get(getIndex());
                    deleteMaquina(maquina);
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

    private void loadMaquinasData() {
        try {
            List<Maquina> maquinas = maquinaService.obtenerTodasLasMaquinas();
            maquinasList = FXCollections.observableArrayList(maquinas);
            maquinasTable.setItems(maquinasList);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al cargar las máquinas", e.getMessage());
        }
    }

    private void updateMaquina(Maquina maquina) {
        try {
            maquinaService.actualizarMaquina(maquina);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al actualizar la máquina", e.getMessage());
        }
    }

    private void deleteMaquina(Maquina maquina) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Estás seguro de que deseas eliminar esta máquina?");
        alert.setContentText("Esta acción no se puede deshacer.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                maquinaService.eliminarMaquina(maquina);
                maquinasList.remove(maquina);
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error al eliminar la máquina", e.getMessage());
            }
        }
    }

    @FXML
    void crearMaquina() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/github/Franfuu/view/CrearMaquinaView.fxml"));
            Parent root = loader.load();

            // Obtener el controlador correcto - aquí está el cambio principal
            CrearMaquinaController controller = loader.getController();
            controller.setEmpleadoActual(empleadoActual);

            // Configurar el escenario
            Stage stage = new Stage();
            stage.setTitle("Crear Máquina");
            Scene scene = new Scene(root);

            // Usar el CSS
            scene.getStylesheets().add(getClass().getResource("/css/CrearView.css").toExternalForm());

            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);

            // Configurar acción al cerrar para actualizar la tabla
            stage.setOnHidden(e -> loadMaquinasData());

            stage.showAndWait();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al crear la máquina", e.getMessage());
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
        // Ya estamos en esta vista, podemos recargar los datos si es necesario
        try {
            loadMaquinasData();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al recargar las máquinas", e.getMessage());
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
}