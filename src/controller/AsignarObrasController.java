package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.util.Map;
import java.util.HashMap;

import util.FirebaseService;

public class AsignarObrasController {

    @FXML private ComboBox<String> comboEmpleados;
    @FXML private ComboBox<String> comboObras;

    private Map<String, String> empleadosMap = new HashMap<>();
    private Map<String, String> obrasMap = new HashMap<>();

    @FXML
    public void initialize() {
        empleadosMap = FirebaseService.obtenerEmpleados();
        empleadosMap.forEach((uid, nombre) -> comboEmpleados.getItems().add(nombre + " (" + uid + ")"));

        obrasMap = FirebaseService.obtenerObras();
        obrasMap.forEach((obraId, nombre) -> comboObras.getItems().add(nombre + " (" + obraId + ")"));
    }

    @FXML
    private void asignarObra() {
        String empSeleccionado = comboEmpleados.getValue();
        String obraSeleccionada = comboObras.getValue();

        if (empSeleccionado == null || obraSeleccionada == null) return;

        String uid = empSeleccionado.substring(empSeleccionado.indexOf("(") + 1, empSeleccionado.indexOf(")"));
        String obraId = obraSeleccionada.substring(obraSeleccionada.indexOf("(") + 1, obraSeleccionada.indexOf(")"));

        FirebaseService.asignarObraAEmpleado(uid, obraId);

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Asignación completada");
        alert.setHeaderText(null);
        alert.setContentText("Obra asignada correctamente al empleado.");
        alert.showAndWait();
    }
}
