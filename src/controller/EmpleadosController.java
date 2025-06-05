package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Empleado;
import util.FirebaseService;

import java.util.List;
import java.util.Map;

public class EmpleadosController {

    @FXML private TableView<Empleado> tablaEmpleados;
    @FXML private TableColumn<Empleado, String> nombreColumna;
    @FXML private TableColumn<Empleado, String> cargoColumna;
    @FXML private TableColumn<Empleado, String> obraColumna;

    private Map<String, String> asignaciones; // UID → obraId
    private Map<String, String> obras;        // obraId → nombre de obra

    @FXML
    public void initialize() {
        // Configurar columnas visibles
        nombreColumna.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        cargoColumna.setCellValueFactory(new PropertyValueFactory<>("cargo"));

        // Cargar asignaciones y obras desde Firebase
        asignaciones = FirebaseService.obtenerAsignaciones();
        obras = FirebaseService.obtenerObras();

        // Configurar columna para mostrar la obra asignada
        obraColumna.setCellValueFactory(cellData -> {
            Empleado emp = cellData.getValue();
            String uid = emp.getId();
            String obraId = asignaciones.get(uid);
            String nombreObra = obras.getOrDefault(obraId, "Sin asignar");
            return new javafx.beans.property.SimpleStringProperty(nombreObra);
        });

        // Cargar empleados desde Firebase
        List<Empleado> lista = FirebaseService.leerEmpleados();
        ObservableList<Empleado> empleados = FXCollections.observableArrayList(lista);
        tablaEmpleados.setItems(empleados);
    }

    @FXML
    private void abrirFormularioNuevo() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FormEmpleado.fxml"));
            Parent root = loader.load();

            FormEmpleadoController controller = loader.getController();
            controller.setListaEmpleados(tablaEmpleados.getItems());

            Stage stage = new Stage();
            stage.setTitle("Nuevo Empleado");
            stage.setScene(new Scene(root));
            stage.initOwner(tablaEmpleados.getScene().getWindow());
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
