package com.github.Franfuu.controllers;

import com.github.Franfuu.App;
import com.github.Franfuu.model.entities.Cliente;
import com.github.Franfuu.model.entities.Ejercicio;
import com.github.Franfuu.model.entities.Empleado;
import com.github.Franfuu.model.entities.Rutina;
import com.github.Franfuu.model.entities.RutinaEjercicio;
import com.github.Franfuu.services.ClienteService;
import com.github.Franfuu.services.EjercicioService;
import com.github.Franfuu.services.RutinaEjercicioService;
import com.github.Franfuu.services.RutinaService;

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
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class RutinasController extends Controller implements Initializable {

    @FXML
    private BorderPane anchorPane;

    @FXML
    private Text empleadoNombreText;

    @FXML
    private ImageView empleadoFotoView;

    @FXML
    private TabPane tabPane;

    // Componentes de la pestaña Rutinas
    @FXML
    private TextField txtBuscarRutina;

    @FXML
    private TableView<Rutina> tablasRutinas;

    @FXML
    private TableColumn<Rutina, Integer> colIdRutina;

    @FXML
    private TableColumn<Rutina, String> colNombreRutina;

    @FXML
    private TableColumn<Rutina, String> colClienteRutina;

    @FXML
    private TableColumn<Rutina, String> colEmpleadoRutina;

    @FXML
    private TableColumn<Rutina, String> colDescripcionRutina;

    @FXML
    private TableColumn<Rutina, LocalDate> colFechaCreacionRutina;

    @FXML
    private TableColumn<Rutina, Void> colAccionesRutina;

    // Componentes de la pestaña Ejercicios
    @FXML
    private TextField txtBuscarEjercicio;

    @FXML
    private TableView<Ejercicio> tablaEjercicios;

    @FXML
    private TableColumn<Ejercicio, Integer> colIdEjercicio;

    @FXML
    private TableColumn<Ejercicio, String> colNombreEjercicio;

    @FXML
    private TableColumn<Ejercicio, String> colGrupoMuscular;

    @FXML
    private TableColumn<Ejercicio, String> colDescripcionEjercicio;

    @FXML
    private TableColumn<Ejercicio, Void> colAccionesEjercicio;

    // Servicios
    private RutinaService rutinaService;
    private EjercicioService ejercicioService;
    private RutinaEjercicioService rutinaEjercicioService;
    private ClienteService clienteService;

    // Listas observables
    private ObservableList<Rutina> rutinasList;
    private ObservableList<Ejercicio> ejerciciosList;

    // Empleado actual
    private Empleado empleadoActual;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Inicializar servicios
        rutinaService = new RutinaService();
        ejercicioService = new EjercicioService();
        rutinaEjercicioService = new RutinaEjercicioService();
        clienteService = new ClienteService();

        // Configurar tablas
        configureRutinasTable();
        configureEjerciciosTable();


        // Configurar listeners para búsqueda
        txtBuscarRutina.textProperty().addListener((observable, oldValue, newValue) -> {
            buscarRutinas(newValue);
        });

        txtBuscarEjercicio.textProperty().addListener((observable, oldValue, newValue) -> {
            buscarEjercicios(newValue);
        });

        // Cargar datos
        cargarRutinas();
        cargarEjercicios();

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

    private void configureRutinasTable() {
        // Hacer la tabla editable
        tablasRutinas.setEditable(true);

        colIdRutina.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Columna nombre editable
        colNombreRutina.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombreRutina.setCellFactory(TextFieldTableCell.forTableColumn());
        colNombreRutina.setOnEditCommit(event -> {
            Rutina rutina = event.getRowValue();
            rutina.setNombre(event.getNewValue());
            guardarCambiosRutina(rutina);
        });

        colClienteRutina.setCellValueFactory(cellData -> {
            Cliente cliente = cellData.getValue().getCliente();
            return new SimpleStringProperty(cliente != null ?
                    cliente.getNombre() + " " + cliente.getApellido() : "");
        });

        colEmpleadoRutina.setCellValueFactory(cellData -> {
            Empleado empleado = cellData.getValue().getEmpleado();
            return new SimpleStringProperty(empleado != null ?
                    empleado.getNombre() + " " + empleado.getApellido() : "");
        });

        // Columna descripción editable
        colDescripcionRutina.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colDescripcionRutina.setCellFactory(TextFieldTableCell.forTableColumn());
        colDescripcionRutina.setOnEditCommit(event -> {
            Rutina rutina = event.getRowValue();
            rutina.setDescripcion(event.getNewValue());
            guardarCambiosRutina(rutina);
        });

        colFechaCreacionRutina.setCellValueFactory(new PropertyValueFactory<>("fechaCreacion"));
        colFechaCreacionRutina.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
        configurarColumnasAccionesRutina();

        rutinasList = FXCollections.observableArrayList();
        tablasRutinas.setItems(rutinasList);
    }

    private void guardarCambiosRutina(Rutina rutina) {
        try {
            rutinaService.actualizarRutina(rutina);
            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Rutina actualizada",
                    "La rutina ha sido actualizada correctamente.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al actualizar la rutina",
                    e.getMessage());
            // Recargar la lista para deshacer los cambios en la interfaz
            cargarRutinas();
        }
    }

    private void configureEjerciciosTable() {
        // Hacer la tabla editable
        tablaEjercicios.setEditable(true);

        // Configurar columnas
        colIdEjercicio.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Columna nombre editable
        colNombreEjercicio.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombreEjercicio.setCellFactory(TextFieldTableCell.forTableColumn());
        colNombreEjercicio.setOnEditCommit(event -> {
            Ejercicio ejercicio = event.getRowValue();
            ejercicio.setNombre(event.getNewValue());
            guardarCambiosEjercicio(ejercicio);
        });

        // Columna grupo muscular editable
        colGrupoMuscular.setCellValueFactory(new PropertyValueFactory<>("grupoMuscular"));
        colGrupoMuscular.setCellFactory(TextFieldTableCell.forTableColumn());
        colGrupoMuscular.setOnEditCommit(event -> {
            Ejercicio ejercicio = event.getRowValue();
            ejercicio.setGrupoMuscular(event.getNewValue());
            guardarCambiosEjercicio(ejercicio);
        });

        // Columna descripción editable
        colDescripcionEjercicio.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colDescripcionEjercicio.setCellFactory(TextFieldTableCell.forTableColumn());
        colDescripcionEjercicio.setOnEditCommit(event -> {
            Ejercicio ejercicio = event.getRowValue();
            ejercicio.setDescripcion(event.getNewValue());
            guardarCambiosEjercicio(ejercicio);
        });

        // Configurar columna de acciones (solo eliminar)
        configurarColumnasAccionesEjercicio();

        ejerciciosList = FXCollections.observableArrayList();
        tablaEjercicios.setItems(ejerciciosList);
    }

    private void guardarCambiosEjercicio(Ejercicio ejercicio) {
        try {
            ejercicioService.actualizarEjercicio(ejercicio);
            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Ejercicio actualizado",
                    "El ejercicio ha sido actualizado correctamente.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al actualizar el ejercicio",
                    e.getMessage());
            // Recargar la lista para deshacer cambios en la interfaz
            cargarEjercicios();
        }
    }

    private void configurarColumnasAccionesRutina() {
        colAccionesRutina.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("✖");
            private final HBox container = new HBox(5, deleteButton);

            {
                // Configurar botón eliminar
                deleteButton.setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-background-color: transparent;");
                container.setAlignment(Pos.CENTER);

                deleteButton.setOnAction(event -> {
                    Rutina rutina = getTableView().getItems().get(getIndex());
                    eliminarRutina(rutina);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : container);
            }
        });
    }

    private void configurarColumnasAccionesEjercicio() {
        colAccionesEjercicio.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("✖");
            private final HBox container = new HBox(5, deleteButton);

            {
                // Configurar botón eliminar
                deleteButton.setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-background-color: transparent;");
                container.setAlignment(Pos.CENTER);

                deleteButton.setOnAction(event -> {
                    Ejercicio ejercicio = getTableView().getItems().get(getIndex());
                    eliminarEjercicio(ejercicio);
                });
            }



            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(10, deleteButton);
                    hbox.setAlignment(Pos.CENTER);
                    setGraphic(hbox);
                }
            }
        });
    }

    private void cargarRutinas() {
        try {
            List<Rutina> rutinas = rutinaService.obtenerTodasLasRutinas();
            rutinasList = FXCollections.observableArrayList(rutinas);
            tablasRutinas.setItems(rutinasList);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al cargar las rutinas", e.getMessage());
        }
    }

    private void cargarEjercicios() {
        try {
            List<Ejercicio> ejercicios = ejercicioService.obtenerTodosLosEjercicios();
            ejerciciosList = FXCollections.observableArrayList(ejercicios);
            tablaEjercicios.setItems(ejerciciosList);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al cargar los ejercicios", e.getMessage());
        }
    }

    private void buscarRutinas(String texto) {
        if (texto == null || texto.isEmpty()) {
            cargarRutinas();
        } else {
            try {
                List<Rutina> rutinas = rutinaService.buscarRutinasPorNombre(texto);
                rutinasList = FXCollections.observableArrayList(rutinas);
                tablasRutinas.setItems(rutinasList);
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error al buscar rutinas", e.getMessage());
            }
        }
    }

    private void buscarEjercicios(String texto) {
        if (texto == null || texto.isEmpty()) {
            cargarEjercicios();
        } else {
            try {
                List<Ejercicio> ejercicios = ejercicioService.buscarEjerciciosPorNombre(texto);
                ejerciciosList = FXCollections.observableArrayList(ejercicios);
                tablaEjercicios.setItems(ejerciciosList);
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error al buscar ejercicios", e.getMessage());
            }
        }
    }

    @FXML
    void crearRutina() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/github/Franfuu/view/CrearRutinaView.fxml"));
            Parent root = loader.load();

            // Obtener el controlador y pasarle el empleado actual
            CrearRutinaController controller = loader.getController();
            controller.setEmpleado(empleadoActual);

            // Configurar el escenario
            Stage stage = new Stage();
            stage.setTitle("Crear Rutina");
            Scene scene = new Scene(root);

            // Usar solo el CSS que sabemos que existe
            scene.getStylesheets().add(getClass().getResource("/css/CrearView.css").toExternalForm());

            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);

            // Configurar acción al cerrar para actualizar las tablas
            stage.setOnHidden(e -> {
                cargarRutinas();
                cargarEjercicios();
            });

            stage.showAndWait();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al crear la rutina", e.getMessage());
            e.printStackTrace();
        }
    }


    private void eliminarRutina(Rutina rutina) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Estás seguro de que deseas eliminar esta rutina?");
        alert.setContentText("Esta acción no se puede deshacer y eliminará también los ejercicios asociados a la rutina.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                rutinaService.eliminarRutina(rutina.getId());
                rutinasList.remove(rutina);
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Rutina eliminada", "La rutina ha sido eliminada correctamente.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error al eliminar la rutina", e.getMessage());
            }
        }
    }

    @FXML
    void crearEjercicio() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/github/Franfuu/view/CrearEjercicioView.fxml"));
            Parent root = loader.load();

            // Asegúrate de que el CSS existe y tiene la ruta correcta
            Scene scene = new Scene(root);
            if (getClass().getResource("/css/empleado-mainpage.css") != null) {
                scene.getStylesheets().add(getClass().getResource("/css/empleado-mainpage.css").toExternalForm());
            }

            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initOwner(anchorPane.getScene().getWindow());
            modalStage.setTitle("Crear Nuevo Ejercicio");
            modalStage.setScene(scene);
            modalStage.setResizable(false);

            // Actualizar la tabla de ejercicios cuando se cierre el modal
            modalStage.setOnHidden(e -> cargarEjercicios());

            modalStage.showAndWait();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error",
                     "Error al abrir el formulario de creación",
                     "Detalles: " + e.getMessage());
            e.printStackTrace(); // Para ver el error completo en la consola
        }
    }


    private void eliminarEjercicio(Ejercicio ejercicio) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Estás seguro de que deseas eliminar este ejercicio?");
        alert.setContentText("Esta acción no se puede deshacer. Si el ejercicio está asociado a alguna rutina, será eliminado también de esas rutinas.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                ejercicioService.eliminarEjercicio(ejercicio.getId());
                ejerciciosList.remove(ejercicio);
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Ejercicio eliminado", "El ejercicio ha sido eliminado correctamente.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error al eliminar el ejercicio", e.getMessage());
            }
        }
    }

    @FXML
    void handleCerrarSesion() {
        try {
            App.currentController.changeScene(Scenes.WELCOME, null);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al cerrar sesión", e.getMessage());
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
    void showMaquinas() {
        try {
            App.currentController.changeScene(Scenes.MAQUINAS, empleadoActual);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al cargar la vista de máquinas", e.getMessage());
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
        // Ya estamos en esta vista, podemos recargar los datos
        cargarRutinas();
        cargarEjercicios();
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}