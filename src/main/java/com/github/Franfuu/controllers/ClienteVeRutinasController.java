package com.github.Franfuu.controllers;

import com.github.Franfuu.model.dao.RutinaDAO;
import com.github.Franfuu.model.dao.RutinaEjercicioDAO;
import com.github.Franfuu.model.entities.Cliente;
import com.github.Franfuu.model.entities.Rutina;
import com.github.Franfuu.model.entities.RutinaEjercicio;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class ClienteVeRutinasController implements Initializable {

    @FXML
    private TableView<Rutina> tablasRutinas;

    @FXML
    private TableColumn<Rutina, String> nombreRutinaColumn;

    @FXML
    private TableColumn<Rutina, String> descripcionRutinaColumn;

    @FXML
    private TableColumn<Rutina, LocalDate> fechaRutinaColumn;

    @FXML
    private TableView<RutinaEjercicio> tablaEjercicios;

    @FXML
    private TableColumn<RutinaEjercicio, String> nombreEjercicioColumn;

    @FXML
    private TableColumn<RutinaEjercicio, String> grupoMuscularColumn;

    @FXML
    private TableColumn<RutinaEjercicio, Integer> seriesColumn;

    @FXML
    private TableColumn<RutinaEjercicio, Integer> repeticionesColumn;

    @FXML
    private TableColumn<RutinaEjercicio, Integer> descansoColumn;

    @FXML
    private Button exportarExcelButton;

    @FXML
    private Button cerrarButton;

    private Cliente clienteActual;
    private RutinaDAO rutinaDAO;
    private RutinaEjercicioDAO rutinaEjercicioDAO;
    private ObservableList<Rutina> rutinasData;
    private ObservableList<RutinaEjercicio> ejerciciosData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rutinaDAO = new RutinaDAO();
        rutinaEjercicioDAO = new RutinaEjercicioDAO();

        rutinasData = FXCollections.observableArrayList();
        ejerciciosData = FXCollections.observableArrayList();

        // Configurar columnas de la tabla de rutinas
        nombreRutinaColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        descripcionRutinaColumn.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        fechaRutinaColumn.setCellValueFactory(new PropertyValueFactory<>("fechaCreacion"));

        // Configurar columnas de la tabla de ejercicios
        nombreEjercicioColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEjercicio().getNombre()));

        grupoMuscularColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEjercicio().getGrupoMuscular()));

        seriesColumn.setCellValueFactory(new PropertyValueFactory<>("series"));
        repeticionesColumn.setCellValueFactory(new PropertyValueFactory<>("repeticiones"));
        descansoColumn.setCellValueFactory(new PropertyValueFactory<>("descansoSegundos"));

        // Asignar datos a las tablas
        tablasRutinas.setItems(rutinasData);
        tablaEjercicios.setItems(ejerciciosData);

        // Listener para cargar ejercicios cuando se selecciona una rutina
        tablasRutinas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cargarEjerciciosDeRutina(newSelection);
                exportarExcelButton.setDisable(false);
            } else {
                ejerciciosData.clear();
                exportarExcelButton.setDisable(true);
            }
        });

        // Inicialmente deshabilitamos el botón de exportar
        exportarExcelButton.setDisable(true);
    }

    public void setClienteActual(Cliente cliente) {
        this.clienteActual = cliente;
        cargarRutinasCliente();
    }

    private void cargarRutinasCliente() {
        if (clienteActual != null) {
            try {
                List<Rutina> rutinas = rutinaDAO.findByClienteId(clienteActual.getId());
                rutinasData.clear();
                rutinasData.addAll(rutinas);
            } catch (Exception e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al cargar rutinas",
                        "No se pudieron cargar tus rutinas: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void cargarEjerciciosDeRutina(Rutina rutina) {
        try {
            List<RutinaEjercicio> ejercicios = rutinaEjercicioDAO.findByRutina(rutina);
            ejerciciosData.clear();
            ejerciciosData.addAll(ejercicios);
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al cargar ejercicios",
                    "No se pudieron cargar los ejercicios: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void exportarEjercicios() {
        Rutina rutinaSeleccionada = tablasRutinas.getSelectionModel().getSelectedItem();
        if (rutinaSeleccionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Aviso", "Selecciona una rutina",
                    "Debes seleccionar una rutina para exportar sus ejercicios");
            return;
        }

        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Guardar ejercicios");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Excel (*.xlsx)", "*.xlsx"));
            fileChooser.setInitialFileName("Rutina_" + rutinaSeleccionada.getNombre() + ".xlsx");

            File file = fileChooser.showSaveDialog(exportarExcelButton.getScene().getWindow());
            if (file != null) {
                exportarEjerciciosAExcel(file, rutinaSeleccionada);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Exportación completada",
                        "Los ejercicios se han exportado correctamente a: " + file.getName());
            }
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al exportar",
                    "No se pudieron exportar los ejercicios: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void exportarEjerciciosAExcel(File file, Rutina rutina) throws Exception {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Ejercicios de " + rutina.getNombre());

            // Estilo para encabezados
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // Información de la rutina
            Row rutinaInfoRow1 = sheet.createRow(0);
            Cell cellTitle = rutinaInfoRow1.createCell(0);
            cellTitle.setCellValue("Rutina: " + rutina.getNombre());
            cellTitle.setCellStyle(headerStyle);

            Row rutinaInfoRow2 = sheet.createRow(1);
            rutinaInfoRow2.createCell(0).setCellValue("Cliente: " + clienteActual.getNombre() + " " + clienteActual.getApellido());

            Row rutinaInfoRow3 = sheet.createRow(2);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            rutinaInfoRow3.createCell(0).setCellValue("Fecha: " + rutina.getFechaCreacion().format(formatter));

            // Fila vacía
            sheet.createRow(3);

            // Encabezados
            Row headerRow = sheet.createRow(4);
            String[] headers = {"Ejercicio", "Grupo muscular", "Series", "Repeticiones", "Descanso (s)"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Datos de ejercicios
            int rowNum = 5;
            for (RutinaEjercicio ejercicio : ejerciciosData) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(ejercicio.getEjercicio().getNombre());
                row.createCell(1).setCellValue(ejercicio.getEjercicio().getGrupoMuscular());
                row.createCell(2).setCellValue(ejercicio.getSeries());
                row.createCell(3).setCellValue(ejercicio.getRepeticiones());
                row.createCell(4).setCellValue(ejercicio.getDescansoSegundos());
            }

            // Ajustar ancho de columnas
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Escribir a archivo
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                workbook.write(outputStream);
            }
        }
    }

    @FXML
    private void cerrar() {
        Stage stage = (Stage) cerrarButton.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String header, String contenido) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}