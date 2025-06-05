package com.github.Franfuu.controllers;

import com.github.Franfuu.App;
import com.github.Franfuu.model.entities.Clase;
import com.github.Franfuu.model.entities.Empleado;
import com.github.Franfuu.model.entities.Sala;
import com.github.Franfuu.controllers.Controller;
import com.github.Franfuu.controllers.Scenes;
import com.github.Franfuu.services.ClaseService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ClasesController extends Controller implements Initializable {

    @FXML
    private BorderPane anchorPane;

    @FXML
    private Text empleadoNombreText;

    @FXML
    private ImageView empleadoFotoView;

    @FXML
    private TableView<Clase> clasesTable;

    @FXML
    private TableColumn<Clase, Integer> idColumn;

    @FXML
    private TableColumn<Clase, String> nombreColumn;

    @FXML
    private TableColumn<Clase, String> descripcionColumn;

    @FXML
    private TableColumn<Clase, Empleado> empleadoColumn;

    @FXML
    private TableColumn<Clase, Sala> salaColumn;

    @FXML
    private TableColumn<Clase, LocalTime> horaInicioColumn;

    @FXML
    private TableColumn<Clase, LocalTime> horaFinColumn;

    @FXML
    private TableColumn<Clase, String> diasSemanaColumn;

    @FXML
    private TableColumn<Clase, Void> accionesColumn;

    private Empleado empleadoActual;
    private ClaseService claseService;
    private ObservableList<Clase> clasesList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        claseService = new ClaseService();

        configureTable();
        loadClasesData();
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

            loadClasesData();
        }
    }

    private void configureTable() {
        clasesTable.setEditable(true);

        // Columna no editable
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Columnas editables
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        nombreColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nombreColumn.setOnEditCommit(event -> {
            Clase clase = event.getRowValue();
            clase.setNombre(event.getNewValue());
            updateClase(clase);
        });

        descripcionColumn.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        descripcionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        descripcionColumn.setOnEditCommit(event -> {
            Clase clase = event.getRowValue();
            clase.setDescripcion(event.getNewValue());
            updateClase(clase);
        });

        // Configuración de las columnas restantes
        empleadoColumn.setCellValueFactory(new PropertyValueFactory<>("idEmpleado"));
        empleadoColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Empleado empleado, boolean empty) {
                super.updateItem(empleado, empty);
                if (empty || empleado == null) {
                    setText(null);
                } else {
                    try {
                        setText(empleado.getNombre() + " " + empleado.getApellido());
                    } catch (Exception e) {
                        setText("No disponible");
                    }
                }
            }
        });

        salaColumn.setCellValueFactory(new PropertyValueFactory<>("sala"));
        salaColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Sala sala, boolean empty) {
                super.updateItem(sala, empty);
                if (empty || sala == null) {
                    setText(null);
                } else {
                    try {
                        setText(sala.getNombre());
                    } catch (Exception e) {
                        setText("No disponible");
                    }
                }
            }
        });

        // Configuración para días de semana
        diasSemanaColumn.setCellValueFactory(new PropertyValueFactory<>("diasSemana"));
        diasSemanaColumn.setCellFactory(column -> {
            return new TableCell<Clase, String>() {
                private final Button button = new Button("Seleccionar días");
                private final String[] diasSemana = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
                private boolean[] selectedDays = new boolean[7];

                {
                    button.setMaxWidth(Double.MAX_VALUE);
                    button.setOnAction(e -> showDaysPopup());
                }

                private void showDaysPopup() {
                    Popup popup = new Popup();
                    VBox vbox = new VBox(5);
                    vbox.setStyle("-fx-background-color: white; -fx-border-color: #cccccc; -fx-padding: 10;");

                    CheckBox[] checkBoxes = new CheckBox[diasSemana.length];
                    for (int i = 0; i < diasSemana.length; i++) {
                        checkBoxes[i] = new CheckBox(diasSemana[i]);
                        checkBoxes[i].setSelected(selectedDays[i]);
                        final int index = i;
                        checkBoxes[i].selectedProperty().addListener((obs, oldVal, newVal) -> {
                            selectedDays[index] = newVal;
                        });
                        vbox.getChildren().add(checkBoxes[i]);
                    }

                    Button saveButton = new Button("Guardar");
                    saveButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
                    saveButton.setPrefWidth(100);
                    saveButton.setOnAction(event -> {
                        updateButtonText();

                        if (getTableRow() != null && getTableRow().getItem() != null) {
                            Clase clase = getTableView().getItems().get(getIndex());
                            String diasStr = getSelectedDaysAsString();
                            clase.setDiasSemana(diasStr);
                            updateClase(clase);
                        }

                        popup.hide();
                    });

                    vbox.getChildren().add(saveButton);
                    popup.getContent().add(vbox);

                    Bounds bounds = button.localToScreen(button.getBoundsInLocal());
                    popup.show(button, bounds.getMinX(), bounds.getMaxY());
                }

                private void updateButtonText() {
                    StringBuilder text = new StringBuilder();
                    for (int i = 0; i < diasSemana.length; i++) {
                        if (selectedDays[i]) {
                            if (text.length() > 0) {
                                text.append(", ");
                            }
                            text.append(diasSemana[i]);
                        }
                    }
                    button.setText(text.length() > 0 ? text.toString() : "Seleccionar días");
                }

                private String getSelectedDaysAsString() {
                    String[] valoresPermitidos = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
                    StringBuilder result = new StringBuilder();

                    for (int i = 0; i < diasSemana.length; i++) {
                        if (selectedDays[i]) {
                            if (result.length() > 0) {
                                result.append(",");
                            }
                            // Asegurarse de usar exactamente el mismo valor que en la definición SQL
                            result.append(valoresPermitidos[i]);
                        }
                    }

                    // Depuración
                    return result.toString();
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                    } else {
                        // Limpiar selecciones previas
                        for (int i = 0; i < selectedDays.length; i++) {
                            selectedDays[i] = false;
                        }

                        // Procesar los días actuales
                        if (item != null && !item.isEmpty()) {
                            String[] selectedDiasArray = item.split(",");
                            for (String diaStr : selectedDiasArray) {
                                String dia = diaStr.trim();
                                for (int i = 0; i < diasSemana.length; i++) {
                                    if (diasSemana[i].equals(dia)) {
                                        selectedDays[i] = true;
                                        break;
                                    }
                                }
                            }
                        }

                        updateButtonText();
                        setGraphic(button);
                    }
                }
            };
        });

        horaInicioColumn.setCellValueFactory(new PropertyValueFactory<>("horaInicio"));
        horaFinColumn.setCellValueFactory(new PropertyValueFactory<>("horaFin"));
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
                    Clase clase = getTableView().getItems().get(getIndex());
                    deleteClase(clase);
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

    private void loadClasesData() {
        try {
            List<Clase> clases = claseService.obtenerTodasLasClases();
            clasesList = FXCollections.observableArrayList(clases);
            clasesTable.setItems(clasesList);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al cargar las clases", e.getMessage());
        }
    }

    private void updateClase(Clase clase) {
        try {
            // Imprimir información de depuración
            System.out.println("Días a guardar: '" + clase.getDiasSemana() + "'");

            // Delegar al servicio
            claseService.actualizarClase(clase);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al actualizar la clase", e.getMessage());
            e.printStackTrace();
        }
    }

    private void deleteClase(Clase clase) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Estás seguro de que deseas eliminar esta clase?");
        alert.setContentText("Esta acción no se puede deshacer.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                claseService.eliminarClase(clase.getId());
                clasesList.remove(clase);
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error al eliminar la clase", e.getMessage());
            }
        }
    }

   @FXML
    void crearClase() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/github/Franfuu/view/CrearClaseView.fxml"));
            Parent root = loader.load();

            // Obtener el controlador y pasarle el empleado actual
            CrearClaseController controller = loader.getController();
            controller.setEmpleadoActual(empleadoActual);

            // Configurar el escenario
            Stage stage = new Stage();
            stage.setTitle("Crear Clase");
            Scene scene = new Scene(root);

            // Usar solo el CSS que sabemos que existe
            scene.getStylesheets().add(getClass().getResource("/css/CrearView.css").toExternalForm());

            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);

            // Configurar acción al cerrar para actualizar la tabla
            stage.setOnHidden(e -> loadClasesData());

            stage.showAndWait();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al crear la clase", e.getMessage());
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
        try {
            App.currentController.changeScene(Scenes.SALAS, empleadoActual);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al cargar la vista de salas", e.getMessage());
        }
    }

    @FXML
    void showClases() {
        // Ya estamos en esta vista, podemos recargar los datos
        loadClasesData();
    }

    @Override
    public void onClose(Object output) {
        // Limpieza al cerrar
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