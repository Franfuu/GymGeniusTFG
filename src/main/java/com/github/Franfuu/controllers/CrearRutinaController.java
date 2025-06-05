package com.github.Franfuu.controllers;

import com.github.Franfuu.model.entities.Cliente;
import com.github.Franfuu.model.entities.Ejercicio;
import com.github.Franfuu.model.entities.Rutina;
import com.github.Franfuu.model.entities.RutinaEjercicio;
import com.github.Franfuu.model.entities.Empleado;
import com.github.Franfuu.services.ClienteService;
import com.github.Franfuu.services.EjercicioService;
import com.github.Franfuu.services.RutinaService;
import com.github.Franfuu.services.RutinaEjercicioService;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class CrearRutinaController extends Controller implements Initializable {

    @FXML
    private ComboBox<Cliente> clienteComboBox;

    @FXML
    private TextField nombreField;

    @FXML
    private TextField fechaField;

    @FXML
    private TextArea descripcionArea;

    @FXML
    private ComboBox<Ejercicio> ejerciciosComboBox;

    @FXML
    private TableView<RutinaEjercicio> tablaEjerciciosRutina;

    @FXML
    private TableColumn<RutinaEjercicio, String> colNombreEjercicio;

    @FXML
    private TableColumn<RutinaEjercicio, String> colGrupoMuscular;

    @FXML
    private TableColumn<RutinaEjercicio, Integer> colSeries;

    @FXML
    private TableColumn<RutinaEjercicio, Integer> colRepeticiones;

    @FXML
    private TableColumn<RutinaEjercicio, Integer> colDescanso;

    @FXML
    private TableColumn<RutinaEjercicio, Void> colAcciones;

    @FXML
    private Button guardarBtn;

    @FXML
    private Button cancelarBtn;

    private ClienteService clienteService;
    private EjercicioService ejercicioService;
    private RutinaService rutinaService;
    private RutinaEjercicioService rutinaEjercicioService;

    private ObservableList<RutinaEjercicio> ejerciciosRutina;
    private Empleado empleadoActual;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clienteService = new ClienteService();
        ejercicioService = new EjercicioService();
        rutinaService = new RutinaService();
        rutinaEjercicioService = new RutinaEjercicioService();

        ejerciciosRutina = FXCollections.observableArrayList();
        tablaEjerciciosRutina.setItems(ejerciciosRutina);

        // Configurar fecha actual
        fechaField.setText(LocalDate.now().toString());

        // Habilitar edición en la tabla
        tablaEjerciciosRutina.setEditable(true);

        // Configurar columnas
        colNombreEjercicio.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEjercicio().getNombre()));

        colGrupoMuscular.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEjercicio().getGrupoMuscular()));

        // Columnas editables para configurar la rutina
        colSeries.setCellValueFactory(new PropertyValueFactory<>("series"));
        colSeries.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colSeries.setOnEditCommit(event -> {
            RutinaEjercicio ejercicio = event.getRowValue();
            ejercicio.setSeries(event.getNewValue());
        });

        colRepeticiones.setCellValueFactory(new PropertyValueFactory<>("repeticiones"));
        colRepeticiones.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colRepeticiones.setOnEditCommit(event -> {
            RutinaEjercicio ejercicio = event.getRowValue();
            ejercicio.setRepeticiones(event.getNewValue());
        });

        colDescanso.setCellValueFactory(new PropertyValueFactory<>("descansoSegundos"));
        colDescanso.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colDescanso.setOnEditCommit(event -> {
            RutinaEjercicio ejercicio = event.getRowValue();
            ejercicio.setDescansoSegundos(event.getNewValue());
        });

        // Configurar columna de acciones
        configurarColumnasAcciones();

        // Cargar datos iniciales
        cargarClientes();
        cargarEjercicios();
    }

    public void setEmpleado(Empleado empleado) {
        this.empleadoActual = empleado;
    }

    private void cargarClientes() {
        List<Cliente> clientes = clienteService.obtenerTodosLosClientes();
        clienteComboBox.setItems(FXCollections.observableArrayList(clientes));

        // Configurar texto mostrado en ComboBox
        clienteComboBox.setCellFactory(lv -> new ListCell<Cliente>() {
            @Override
            protected void updateItem(Cliente cliente, boolean empty) {
                super.updateItem(cliente, empty);
                setText(empty ? "" : cliente.getNombre() + " " + cliente.getApellido());
            }
        });

        clienteComboBox.setButtonCell(new ListCell<Cliente>() {
            @Override
            protected void updateItem(Cliente cliente, boolean empty) {
                super.updateItem(cliente, empty);
                setText(empty ? "" : cliente.getNombre() + " " + cliente.getApellido());
            }
        });
    }

    private void cargarEjercicios() {
        List<Ejercicio> ejercicios = ejercicioService.obtenerTodosLosEjercicios();
        ejerciciosComboBox.setItems(FXCollections.observableArrayList(ejercicios));

        // Configurar texto mostrado en ComboBox
        ejerciciosComboBox.setCellFactory(lv -> new ListCell<Ejercicio>() {
            @Override
            protected void updateItem(Ejercicio ejercicio, boolean empty) {
                super.updateItem(ejercicio, empty);
                setText(empty ? "" : ejercicio.getNombre());
            }
        });

        ejerciciosComboBox.setButtonCell(new ListCell<Ejercicio>() {
            @Override
            protected void updateItem(Ejercicio ejercicio, boolean empty) {
                super.updateItem(ejercicio, empty);
                setText(empty ? "" : ejercicio.getNombre());
            }
        });
    }

    private void configurarColumnasAcciones() {
        colAcciones.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("✖");

            {
                deleteButton.setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-background-color: transparent;");

                deleteButton.setOnAction(event -> {
                    RutinaEjercicio ejercicio = getTableRow().getItem();
                    if (ejercicio != null) {
                        eliminarEjercicioDeRutina(ejercicio);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
    }

    @FXML
    private void agregarEjercicio() {
        Ejercicio ejercicio = ejerciciosComboBox.getValue();
        if (ejercicio != null) {
            RutinaEjercicio rutinaEjercicio = new RutinaEjercicio();
            rutinaEjercicio.setEjercicio(ejercicio);
            rutinaEjercicio.setSeries(3);          // Valores por defecto
            rutinaEjercicio.setRepeticiones(10);   // Valores por defecto
            rutinaEjercicio.setDescansoSegundos(60); // Valores por defecto

            // Verificar que no exista el ejercicio ya en la lista
            boolean existe = false;
            for (RutinaEjercicio re : ejerciciosRutina) {
                if (re.getEjercicio().getId().equals(ejercicio.getId())) {
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                ejerciciosRutina.add(rutinaEjercicio);
            } else {
                mostrarAlerta(Alert.AlertType.WARNING,
                        "Ejercicio duplicado",
                        "Este ejercicio ya está en la rutina",
                        "Selecciona un ejercicio diferente o modifica el existente.");
            }
        }
    }

    private void eliminarEjercicioDeRutina(RutinaEjercicio rutinaEjercicio) {
        ejerciciosRutina.remove(rutinaEjercicio);
    }

    @FXML
    public void guardarRutina() {
        if (!validarCampos()) {
            return;
        }

        try {
            // Crear la rutina
            Rutina rutina = new Rutina();
            rutina.setNombre(nombreField.getText().trim());
            rutina.setDescripcion(descripcionArea.getText().trim());
            rutina.setFechaCreacion(LocalDate.now());
            rutina.setCliente(clienteComboBox.getValue());
            rutina.setEmpleado(empleadoActual);

            // Guardar la rutina primero para obtener su ID
            rutinaService.guardarRutina(rutina);

            // Guardar los ejercicios asociados a la rutina
            for (RutinaEjercicio rutinaEjercicio : ejerciciosRutina) {
                rutinaEjercicio.setRutina(rutina);
                rutinaEjercicioService.guardarRutinaEjercicio(rutinaEjercicio);
            }

            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Rutina creada",
                    "La rutina ha sido creada exitosamente.");

            // Cerrar la ventana
            Stage stage = (Stage) nombreField.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al guardar la rutina",
                    "Detalles: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validarCampos() {
        if (clienteComboBox.getValue() == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Cliente no seleccionado",
                    "Debes seleccionar un cliente para la rutina.");
            return false;
        }

        if (nombreField.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Nombre vacío",
                    "Debes ingresar un nombre para la rutina.");
            return false;
        }

        if (ejerciciosRutina.isEmpty()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Sin ejercicios",
                    "Debes agregar al menos un ejercicio a la rutina.");
            return false;
        }

        return true;
    }

    @FXML
    public void cancelar() {
        // Cerrar la ventana
        Stage stage = (Stage) nombreField.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String header, String contenido) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    @Override
    public void onOpen(Object input) throws Exception {

    }

    @Override
    public void onClose(Object output) {

    }
}