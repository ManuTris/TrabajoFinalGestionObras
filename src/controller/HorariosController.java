package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.*;
import javafx.beans.property.SimpleStringProperty;

import util.FirebaseService;

import java.util.*;

public class HorariosController {

    @FXML private ComboBox<String> comboEmpleados;
    @FXML private TableView<Map.Entry<String, String>> tablaHorarios;
    @FXML private TableColumn<Map.Entry<String, String>, String> colDia;
    @FXML private TableColumn<Map.Entry<String, String>, String> colHorario;

    private Map<String, String> empleadosMap = new HashMap<>();

    @FXML
    public void initialize() {
        // Cargar empleados
        empleadosMap = FirebaseService.obtenerEmpleados();
        empleadosMap.forEach((uid, nombre) -> {
            comboEmpleados.getItems().add(nombre + " (" + uid + ")");
        });

        // Configurar columnas
        colDia.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey()));
        colHorario.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue()));
    }

    @FXML
    private void cargarHorario() {
        String seleccionado = comboEmpleados.getValue();
        if (seleccionado == null) return;

        // Obtener UID desde ComboBox
        String uid = seleccionado.substring(seleccionado.indexOf("(") + 1, seleccionado.indexOf(")"));

        // Obtener mapa de horarios
        Map<String, String> horarios = FirebaseService.obtenerHorariosPorUID(uid);

        // Convertir a lista observable
        ObservableList<Map.Entry<String, String>> lista = FXCollections.observableArrayList(horarios.entrySet());
        tablaHorarios.setItems(lista);
    }
}
