package com.github.Franfuu.controllers;

import com.github.Franfuu.model.entities.Clase;
import com.github.Franfuu.model.entities.Empleado;
import com.github.Franfuu.model.entities.Sala;
import com.github.Franfuu.services.ClaseService;
import com.github.Franfuu.services.EmpleadoService;
import com.github.Franfuu.services.SalaService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalTime;
import java.util.*;

public class CrearClaseController implements Initializable {

    @FXML private TextField nombreField;
    @FXML private TextArea descripcionArea;
    @FXML private ComboBox<Empleado> instructorCombo;
    @FXML private ComboBox<Sala> salaCombo;
    @FXML private ComboBox<Integer> horaInicioCombo;
    @FXML private ComboBox<Integer> minutoInicioCombo;
    @FXML private ComboBox<Integer> horaFinCombo;
    @FXML private ComboBox<Integer> minutoFinCombo;
    @FXML private CheckBox lunesCheck;
    @FXML private CheckBox martesCheck;
    @FXML private CheckBox miercolesCheck;
    @FXML private CheckBox juevesCheck;
    @FXML private CheckBox viernesCheck;
    @FXML private CheckBox sabadoCheck;
    @FXML private CheckBox domingoCheck;
    @FXML private Button guardarButton;
    @FXML private Button cancelarButton;

    private ClaseService claseService;
    private EmpleadoService empleadoService;
    private SalaService salaService;
    private Empleado empleadoActual;

    // Función para recibir el empleado actual
    public void setEmpleadoActual(Empleado empleado) {
        this.empleadoActual = empleado;
        cargarDatos();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        claseService = new ClaseService();
        empleadoService = new EmpleadoService();
        salaService = new SalaService();

        configurarHorarios();
        configurarCombos();
    }

    private void cargarDatos() {
        // Cargar instructores
        List<Empleado> instructores = empleadoService.obtenerTodosLosEmpleados();
        instructorCombo.setItems(FXCollections.observableArrayList(instructores));
        instructorCombo.setValue(empleadoActual); // Seleccionar el empleado actual por defecto

        // Cargar salas
        List<Sala> salas = salaService.obtenerTodasLasSalas();
        salaCombo.setItems(FXCollections.observableArrayList(salas));
        if (!salas.isEmpty()) {
            salaCombo.setValue(salas.get(0));
        }
    }



    private void configurarHorarios() {
        // Configurar comboboxes de horas (0-23)
        Integer[] horas = new Integer[24];
        for (int i = 0; i < 24; i++) {
            horas[i] = i;
        }
        horaInicioCombo.setItems(FXCollections.observableArrayList(horas));
        horaFinCombo.setItems(FXCollections.observableArrayList(horas));

        // Configurar comboboxes de minutos (0, 15, 30, 45)
        Integer[] minutos = {0, 15, 30, 45};
        minutoInicioCombo.setItems(FXCollections.observableArrayList(minutos));
        minutoFinCombo.setItems(FXCollections.observableArrayList(minutos));

        // Valores por defecto
        horaInicioCombo.setValue(9);
        minutoInicioCombo.setValue(0);
        horaFinCombo.setValue(10);
        minutoFinCombo.setValue(0);
    }

    private void configurarCombos() {
        // Configurar cómo se muestran los empleados
        instructorCombo.setConverter(new StringConverter<Empleado>() {
            @Override
            public String toString(Empleado empleado) {
                return empleado == null ? "" : empleado.getNombre() + " " + empleado.getApellido();
            }

            @Override
            public Empleado fromString(String string) {
                return null; // No es necesario para este caso
            }
        });

        // Configurar cómo se muestran las salas
        salaCombo.setConverter(new StringConverter<Sala>() {
            @Override
            public String toString(Sala sala) {
                return sala == null ? "" : sala.getNombre() + " (Cap: " + sala.getCapacidadMaxima() + ")";
            }

            @Override
            public Sala fromString(String string) {
                return null; // No es necesario para este caso
            }
        });
    }

    @FXML
    private void handleGuardar() {
        try {
            if (!validarCampos()) {
                return;
            }

            Clase nuevaClase = new Clase();
            nuevaClase.setNombre(nombreField.getText());
            nuevaClase.setDescripcion(descripcionArea.getText());
            nuevaClase.setIdEmpleado(instructorCombo.getValue());
            nuevaClase.setSala(salaCombo.getValue());

            // Configurar hora de inicio
            LocalTime horaInicio = LocalTime.of(
                horaInicioCombo.getValue(),
                minutoInicioCombo.getValue()
            );
            nuevaClase.setHoraInicio(horaInicio);

            // Configurar hora de fin
            LocalTime horaFin = LocalTime.of(
                horaFinCombo.getValue(),
                minutoFinCombo.getValue()
            );
            nuevaClase.setHoraFin(horaFin);

            // Configurar días de la semana seleccionados
            nuevaClase.setDiasSemana(getDiasSeleccionados());

            // Guardar clase
            claseService.guardarClase(nuevaClase);

            // Mostrar mensaje de éxito
            mostrarMensaje(Alert.AlertType.INFORMATION, "Clase creada",
                          "La clase ha sido creada correctamente.");

            // Cerrar la ventana
            Stage stage = (Stage) guardarButton.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            mostrarMensaje(Alert.AlertType.ERROR, "Error",
                          "No se pudo crear la clase: " + e.getMessage());
        }
    }

    private String getDiasSeleccionados() {
        StringBuilder sb = new StringBuilder();

        if (lunesCheck.isSelected()) sb.append("Lunes,");
        if (martesCheck.isSelected()) sb.append("Martes,");
        if (miercolesCheck.isSelected()) sb.append("Miercoles,"); // é en Unicode
        if (juevesCheck.isSelected()) sb.append("Jueves,");
        if (viernesCheck.isSelected()) sb.append("Viernes,");
        if (sabadoCheck.isSelected()) sb.append("Sabado,"); // á en Unicode
        if (domingoCheck.isSelected()) sb.append("Domingo,");

        // Eliminar la última coma si existe
        String resultado = sb.toString();
        if (resultado.endsWith(",")) {
            resultado = resultado.substring(0, resultado.length() - 1);
        }

        return resultado;
    }

    private boolean validarCampos() {
        // Validaciones existentes
        if (nombreField.getText().trim().isEmpty()) {
            mostrarMensaje(Alert.AlertType.WARNING, "Campos incompletos", "El nombre es obligatorio");
            return false;
        }

        if (instructorCombo.getValue() == null) {
            mostrarMensaje(Alert.AlertType.WARNING, "Campos incompletos", "Debe seleccionar un instructor");
            return false;
        }

        if (salaCombo.getValue() == null) {
            mostrarMensaje(Alert.AlertType.WARNING, "Campos incompletos", "Debe seleccionar una sala");
            return false;
        }

        if (!lunesCheck.isSelected() && !martesCheck.isSelected() && !miercolesCheck.isSelected() &&
                !juevesCheck.isSelected() && !viernesCheck.isSelected() && !sabadoCheck.isSelected() &&
                !domingoCheck.isSelected()) {
            mostrarMensaje(Alert.AlertType.WARNING, "Campos incompletos", "Debe seleccionar al menos un día");
            return false;
        }

        // Validar que hora inicio y fin no sean iguales
        LocalTime horaInicio = LocalTime.of(horaInicioCombo.getValue(), minutoInicioCombo.getValue());
        LocalTime horaFin = LocalTime.of(horaFinCombo.getValue(), minutoFinCombo.getValue());

        if (horaInicio.equals(horaFin)) {
            mostrarMensaje(Alert.AlertType.WARNING, "Horario inválido",
                    "La hora de inicio y fin no pueden ser iguales");
            return false;
        }

        // Validar que no haya solapamiento con otras clases en la misma sala
        Sala salaSeleccionada = salaCombo.getValue();
        String diasSeleccionados = getDiasSeleccionados();

        // Obtener todas las clases existentes
        List<Clase> clasesExistentes = claseService.obtenerTodasLasClases();

        for (Clase claseExistente : clasesExistentes) {
            // Verificar si es la misma sala
            if (claseExistente.getSala().getId().equals(salaSeleccionada.getId())) {
                // Verificar si hay días en común
                if (hayDiasEnComun(claseExistente.getDiasSemana(), diasSeleccionados)) {
                    // Verificar si hay solapamiento de horarios
                    LocalTime inicioExistente = claseExistente.getHoraInicio();
                    LocalTime finExistente = claseExistente.getHoraFin();

                    // Condición de solapamiento: (inicio1 <= fin2) Y (fin1 >= inicio2)
                    if ((horaInicio.compareTo(finExistente) <= 0) && (horaFin.compareTo(inicioExistente) >= 0)) {
                        mostrarMensaje(Alert.AlertType.WARNING, "Conflicto de horario",
                                "Ya existe una clase programada en la sala " +
                                        salaSeleccionada.getNombre() + " que se solapa con el horario seleccionado.");
                        return false;
                    }
                }
            }
        }

        return true;
    }

    // Método auxiliar para verificar si hay días en común entre dos cadenas de días
    private boolean hayDiasEnComun(String dias1, String dias2) {
        if (dias1 == null || dias2 == null) {
            return false;
        }

        String[] diasArray1 = dias1.split(",");
        String[] diasArray2 = dias2.split(",");

        Set<String> conjuntoDias1 = new HashSet<>(Arrays.asList(diasArray1));

        for (String dia : diasArray2) {
            if (conjuntoDias1.contains(dia)) {
                return true;
            }
        }

        return false;
    }

    @FXML
    private void handleCancelar() {
        Stage stage = (Stage) cancelarButton.getScene().getWindow();
        stage.close();
    }

    private void mostrarMensaje(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}